package com.example.findme.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.PaintDrawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.example.findme.R;
import com.example.findme.db.FindMeDB;
import com.example.findme.model.CaseInfo;
import com.example.findme.model.PopupTypeItem;
import com.example.findme.util.Global;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PublishCaseActivity extends Activity implements View.OnClickListener, AMapLocationListener{

    private TextView publishName;//发布人名称

    private RelativeLayout changePublishName;//点击更改发布人名称Layout

    private TextView publishContact;//联系方式

    private RelativeLayout changePublishContact;//点击更改联系方式Layout

    private TextView caseType;//发布类型

    private RelativeLayout changeCaseType;//点击更改发布类型Layout

    private RelativeLayout changeArea;// 点击更改行政区域Layout

    private TextView areaProvince;// 行政区域-省

    private TextView areaCity;//行政区域-市

    private TextView areaDistrict;//行政区域-区

    private TextView placeType;//地点类型

    private RelativeLayout changePlaceType;//点击更改地点类型Layout

    private EditText placeDetail;//地点详细信息

    private Button btnGetPlaceDetail;//获取当前位置按钮

    private ProgressDialog progressDialog1, progressDialog2 ;// 进入本界面初始化时显示progressDialog1,用户点击界面上的获取地址按钮时显示2

    private EditText remark;//备注信息

    private RelativeLayout changePublishBonus;//点击更改酬金Layout

    private TextView bonus;//酬金信息

    private Button publishConfirm;//确认提交按钮

    private List<String> leftStringTypeList = new ArrayList<String>();

    private List<String> rightStringTypeList = new ArrayList<String>();

    private List<PopupTypeItem>  rightPopupTypeList;

    private PopupTypeItem leftSelectedTypeItem;

    private PopupWindow popupWindow;

    private ListView leftTypeListView, rightTypeListView;

    private LocationManagerProxy mLocationManagerProxy;//高德地图Manager

    private CaseInfo caseInfo = new CaseInfo();

    private FindMeDB findMeDB;//数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_publish_case);
        getActionBar().setTitle("发布信息");
        initViews();// 初始化控件
        initDatas();// 初始化用户信息
        initPopup();// 初始化弹窗

        initGaoDe();// 初始化高德地图定位信息 如果打开的时候就进行初始化，会造成打开的时候有一定慢
        findMeDB = FindMeDB.getInstance(this);// 初始化数据库
    }



    private void initViews() {

        changePublishName = (RelativeLayout) findViewById(R.id.change_publish_name);
        changePublishName.setOnClickListener(this);
        publishName = (TextView) findViewById(R.id.publish_name);
        changePublishContact = (RelativeLayout) findViewById(R.id.change_publish_contact);
        changePublishContact.setOnClickListener(this);
        publishContact = (TextView) findViewById(R.id.publish_contact);
        changeCaseType = (RelativeLayout) findViewById(R.id.change_publish_case_type);
        changeCaseType.setOnClickListener(this);
        changeArea = (RelativeLayout) findViewById(R.id.change_publish_area);
        changeArea.setOnClickListener(this);
        areaProvince = (TextView) findViewById(R.id.publish_area_province);
        areaProvince.setText("省");
        areaCity = (TextView) findViewById(R.id.publish_area_city);
        areaCity.setText("市");
        areaDistrict = (TextView) findViewById(R.id.publish_area_district);
        areaDistrict.setText("区");
        caseType = (TextView) findViewById(R.id.publish_case_type);
        caseType.setText("请点击选择");
        changePlaceType = (RelativeLayout) findViewById(R.id.change_publish_place_type);
        changePlaceType.setOnClickListener(this);
        placeType = (TextView) findViewById(R.id.publish_place_type);
        placeType.setText("请点击选择");
        placeDetail = (EditText) findViewById(R.id.publish_place_detail);
        btnGetPlaceDetail = (Button) findViewById(R.id.getAddress_btn);
        btnGetPlaceDetail.setOnClickListener(this);
        remark = (EditText) findViewById(R.id.publish_remark);
        changePublishBonus = (RelativeLayout) findViewById(R.id.change_publish_bonus);
        changePublishBonus.setOnClickListener(this);
        bonus = (TextView) findViewById(R.id.publish_bonus);
        publishConfirm = (Button) findViewById(R.id.publish_confirm);
        publishConfirm.setOnClickListener(this);
    }

    private void initDatas() {
        // 设置发布人名称
        // MainActivity.getUserInfo().getName() == null ? publishName.setText("未填写") : publishName.setText(MainActivity.getUserInfo().getName());
        if( MainActivity.getUserInfo().getName() == null ){
            publishName.setText("未填写");
        }else{
            publishName.setText(MainActivity.getUserInfo().getName());
        }
        if( MainActivity.getUserInfo().getPhoneNumber() == null ){
            publishContact.setText("未填写");
        }else{
            publishContact.setText(MainActivity.getUserInfo().getPhoneNumber());
        }
        bonus.setText("0");
    }

    private void initPopup() {

        popupWindow = new PopupWindow(this);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_layout, null);
        leftTypeListView = (ListView) view.findViewById(R.id.pop_listview_left);
        rightTypeListView = (ListView) view.findViewById(R.id.pop_listview_right);

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new PaintDrawable());
        popupWindow.setFocusable(true);

        popupWindow.setHeight(430);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                leftTypeListView.setSelection(0);
                rightTypeListView.setSelection(0);
            }
        });
    }

    private void initGaoDe(){
        //初始化定位，只采用网络定位
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(false);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 10, this);
        // 开启弹窗显示请稍后，等待定位
        progressDialog1 = ProgressDialog.show(this, "正在定位", "正在定位，请稍后...");
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_publish_name:
                final EditText editTextForName = new EditText(this);//设置获取对话框中内容
                new AlertDialog.Builder(this).setTitle("请输入发布人姓名：").setView(editTextForName).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if(editTextForName.getText().toString().equals("")){
                            Toast.makeText(PublishCaseActivity.this, "请输入发布人姓名...", Toast.LENGTH_SHORT).show();
                        }else{
                            publishName.setText(editTextForName.getText());
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.change_publish_contact:
                final EditText editTextForContact = new EditText(this);//设置获取对话框中内容
                new AlertDialog.Builder(this).setTitle("请输入联系方式：").setView(editTextForContact).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if(editTextForContact.getText().toString().equals("")){
                            Toast.makeText(PublishCaseActivity.this, "请输入联系方式...", Toast.LENGTH_SHORT).show();
                        }else{
                            publishContact.setText(editTextForContact.getText());
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.change_publish_case_type:
                if( popupWindow.isShowing() ){
                    popupWindow.dismiss();
                }else{
                    // 发布类型查询是11
                    // leftPopupTypeList = Global.getPopupTypeList(11);
                    addDataToPopupWindow(Global.getPopupTypeList(11));
                    popupWindow.showAsDropDown(view);
                }
                break;
            case R.id.change_publish_area:
                Intent intentToArea = new Intent(this, ChangeAreaActivity.class);
                startActivityForResult(intentToArea, Global.AREA);
                break;
            case R.id.change_publish_place_type:
                if( popupWindow.isShowing() ){
                    popupWindow.dismiss();
                }else{
                    // 地点类型查询是21
                    // leftPopupTypeList = Global.getPopupTypeList(21);
                    addDataToPopupWindow(Global.getPopupTypeList(21));
                    popupWindow.showAsDropDown(view);
                }
                break;
            case R.id.getAddress_btn:

                progressDialog2 = ProgressDialog.show(this, "获取当前地址", "正在获取当前地址，请稍后...");
                mLocationManagerProxy.setGpsEnable(false);
                // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
                // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
                // 在定位结束后，在合适的生命周期调用destroy()方法
                // 其中如果间隔时间为-1，则定位只定一次,
                // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
                // provider - 注册监听的 provider 名称。 用户需根据定位需求来设定provider。有三种定位Provider供用户选择，分别是:LocationManagerProxy.GPS_PROVIDER，代表使用手机GPS定位；LocationManagerProxy.NETWORK_PROVIDER，代表使用手机网络定位；LocationProviderProxy.AMapNetwork，代表高德网络定位服务。
                // minTime - 位置变化的通知时间，单位为毫秒，实际时间有可能长于或短于设定值 。参数设置为-1，为单次定位；反之为每隔设定的时间，都会触发定位请求。
                // minDistance - 位置变化通知距离，单位为米。
                // listener - 监听listener。
                mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 10, this);
                break;
            case R.id.change_publish_bonus:
                final EditText editTextForBonus = new EditText(this);//设置获取对话框中内容
                editTextForBonus.setText("100");
                new AlertDialog.Builder(this).setTitle("请输入酬金：").setView(editTextForBonus).setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                        if(editTextForBonus.getText().toString().equals("")){
                            bonus.setText("0");
                        }else{
                            bonus.setText(editTextForBonus.getText());
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                }).show();
                break;
            case R.id.publish_confirm:

                // 检查发布人填写了姓名没
                if( publishName.getText().toString().equals("未填写") ){
                    new AlertDialog.Builder(this).setTitle("提示").setMessage("请输入发布人姓名").setPositiveButton("确定", null).show();
                }else{
                    caseInfo.setName(publishName.getText().toString());
                    // 联系方式本来是有的，可以不用检查
                    caseInfo.setContact(publishContact.getText().toString());
                    // 检查发布类型选择了没
                    if( caseType.getText().toString().equals("请点击选择") ){
                        new AlertDialog.Builder(this).setTitle("提示").setMessage("请选择发布类型").setPositiveButton("确定", null).show();
                    }else{
                        // 用户如果选择了，在addDataToPopupWindow方法里面已经把选择好的ID存入caseInfo里面去了
                        // areaType行政区域，在进入本界面时，已经自动获取并存入caseInfo中，如果用户再自行选择了，返回时也会把对应的code回传回来并存入caseInfo中

                        // 检查地点类型选择了没
                        if( placeType.getText().toString().equals("请点击选择") ){
                            new AlertDialog.Builder(this).setTitle("提示").setMessage("请选择地点类型").setPositiveButton("确定", null).show();
                        }else{
                            // 用户如果选择了，在addDataToPopupWindow方法里面已经把选择好的ID存入caseInfo里面去了
                            // 检查用户详细地点信息写了没
                            if( placeDetail.getText().toString().equals("") ){
                                new AlertDialog.Builder(this).setTitle("提示").setMessage("请填写详细地点信息").setPositiveButton("确定", null).show();
                            }else{
                                caseInfo.setPlaceInfo(placeDetail.getText().toString());
                                caseInfo.setUserId(MainActivity.getUserInfo().getUserId());
                                Date date = new Date();
                                // 保存用户点下按钮时的年月日时分秒，用作比较排序
                                caseInfo.setPublishTime(date.getTime());
                                caseInfo.setRemark(remark.getText().toString());
                                caseInfo.setBonus(bonus.getText().toString());
                                // 设置caseInfo状态，1-新发布，2-已完成，3-已放弃
                                caseInfo.setStatus(1);
                                findMeDB.createCaseInfo(caseInfo);
                                Intent intentToFocusFragment = new Intent();
                                setResult(RESULT_OK, intentToFocusFragment);
                                finish();
                            }
                        }

                    }

                }
                break;
            default:
                break;
        }
    }

    private void addDataToPopupWindow(final List<PopupTypeItem> leftPopupTypeList) {
        //加载一级菜单
//        final LeftTypeAdapter leftTypeAdapter = new LeftTypeAdapter(this, 0, mLeftTypeItemList);
        leftStringTypeList.clear();
        for( int i=0; i<leftPopupTypeList.size() ;i++ ){
            leftStringTypeList.add(leftPopupTypeList.get(i).getItemName());
        }
        final ArrayAdapter leftTypeAdapter = new ArrayAdapter(this, R.layout.popup_list_item_1, leftStringTypeList);
        leftTypeListView.setAdapter(leftTypeAdapter);
        // 加载左侧第一行对应右侧二级菜单
        //rightTypeItemList = new ArrayList<RightTypeItem>();
        // 发布类型是11，地点类型是21，子类型为1101 1102 1201这样类似，所以需要先进行转换去查询
        rightPopupTypeList = Global.getPopupTypeList(leftPopupTypeList.get(0).getId()*100+1);
        rightStringTypeList.clear();
        for( int i=0; i<rightPopupTypeList.size() ; i++ ){
            rightStringTypeList.add(rightPopupTypeList.get(i).getItemName());
        }
        final ArrayAdapter rightTypeAdapter = new ArrayAdapter(this, R.layout.popup_list_item_1, rightStringTypeList);
        //final RightTypeAdapter rightTypeAdapter = new RightTypeAdapter(this, 0, rightTypeItemList);
        rightTypeListView.setAdapter(rightTypeAdapter);
        // 当用户第一次点开弹窗时，默认选中左侧第一项
        leftSelectedTypeItem = leftPopupTypeList.get(0);
        //左侧ListView点击事件
        leftTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 点击左侧项目之后，根据项目去查询对应的右侧项目，并显示出来

                // 先清空右侧数据列表
                rightStringTypeList.clear();
                // 查询左侧对应右侧列表
                rightPopupTypeList = Global.getPopupTypeList(leftPopupTypeList.get(position).getId()*100+1);
                // 循环复制到数据列表
                for( int i=0; i<rightPopupTypeList.size() ;i++ ){
                    rightStringTypeList.add(rightPopupTypeList.get(i).getItemName());
                }
                // 通知适配器更改了列表
                rightTypeAdapter.notifyDataSetChanged();
                // 把左边的选项记录起来，等右边的也选择了以后，显示到UI上
                leftSelectedTypeItem = leftPopupTypeList.get(position);

            }
        });

        //右侧ListView点击事件
        rightTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //关闭popupWindow,显示用户选择的分类
                popupWindow.dismiss();
                // 把用户的选择显示在UI上
                if( leftSelectedTypeItem.getId()/10 == 1 ){
                    caseType.setText(leftSelectedTypeItem.getItemName()+":"+rightPopupTypeList.get(position).getItemName());
                    // 把最终 发布类型 选项ID复制到caseInfo去
                    caseInfo.setCaseType(rightPopupTypeList.get(position).getId());
                }else{
                    placeType.setText(leftSelectedTypeItem.getItemName()+":"+rightPopupTypeList.get(position).getItemName());
                    // 把最终 地点类型 选项ID复制到caseInfo去
                    caseInfo.setPlaceType(rightPopupTypeList.get(position).getId());
                }

            }
        });
    }

    /**
     * 下面是定位监听器方法
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if( aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0 ){
            // 定位成功回调信息，设置相关信息

            // caseAreaName.setText(aMapLocation.getProvince()+aMapLocation.getCity()+aMapLocation.getDistrict());
            // 初次进入的时候把省市区初始化，不初始化地址，点击获取当前位置时再次获取省市区，地址
            if( progressDialog1.isShowing() ){
                areaProvince.setText(aMapLocation.getProvince());
                areaCity.setText(aMapLocation.getCity());
                areaDistrict.setText(aMapLocation.getDistrict());
                caseInfo.setAreaType(Integer.parseInt(aMapLocation.getCityCode()));
                progressDialog1.dismiss();
            }else if( progressDialog2.isShowing() ){
                placeDetail.setText(aMapLocation.getAddress());
                caseInfo.setPlaceInfo(aMapLocation.getAddress());
                areaProvince.setText(aMapLocation.getProvince());
                areaCity.setText(aMapLocation.getCity());
                areaDistrict.setText(aMapLocation.getDistrict());
                caseInfo.setAreaType(Integer.parseInt(aMapLocation.getCityCode()));
                progressDialog2.dismiss();

            }

        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 移除定位请求
        //mLocationManagerProxy.removeUpdates(this);
        // 销毁定位
        mLocationManagerProxy.destroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch ( requestCode ){
            case Global.AREA:
                if( resultCode == RESULT_OK ){
                    areaProvince.setText(data.getStringExtra("province"));
                    // 当省级和市级是同名时，市级不显示
                    if( data.getStringExtra("province").equals(data.getStringExtra("city")) ){
                        areaCity.setText("");
                    }else{
                        areaCity.setText(data.getStringExtra("city"));
                    }
                    areaDistrict.setText(data.getStringExtra("district"));
                    // 把返回来的选中城市code存入caseInfo
                    caseInfo.setAreaType(data.getIntExtra("code", 100000));
                }
                break;
            default:
                break;
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_publish_case, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


}
