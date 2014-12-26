package com.example.findme.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.findme.R;
import com.example.findme.db.FindMeDB;
import com.example.findme.model.Area;
import com.example.findme.util.Global;
import com.example.findme.util.HttpCallbackListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 2014/12/21.
 */
public class ChangeAreaActivity extends Activity{

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<String>();// 填充到areaListView的数据，从areaList获取名字

    private List<Area> areaList;// 行政区域列表

    private ListView areaListView;

    private Area selectedArea;// 用户选中的区域

    private final String initAddress = "http://apis.map.qq.com/ws/district/v1/list?key=QTVBZ-YRMHU-VBHVG-4ZCZR-LRPD6-TWF6G";// 首次启动时，查询网址

    private final String addressHeader = "http://apis.map.qq.com/ws/district/v1/getchildren?&id=";

    private final String addressEnd = "&key=QTVBZ-YRMHU-VBHVG-4ZCZR-LRPD6-TWF6G";

    private final int initCode = 100000;// 为省级数据添加父Code，为了跟其

    private ProgressDialog progressDialog;

    private FindMeDB findMeDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.area_layout);
        findMeDB = FindMeDB.getInstance(this);
//        initDatas();
        //sendRequestWithHttpClient();
        areaListView = (ListView) findViewById(R.id.area_listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);
        areaListView.setAdapter(adapter);
        areaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l) {
                selectedArea = areaList.get(index);
                queryAreas(selectedArea.getAreaSelfCode(), addressHeader+selectedArea.getAreaSelfCode()+addressEnd);
            }
        });
        queryAreas(initCode, initAddress);
    }

    /**
     * 查询省市区数据
     */
    private void queryAreas(final int areaCode,final String address) {
        // 清掉现在的查询列表，为新的数据做准备
        if( areaList != null && areaList.size() > 0 ){
            areaList.clear();
        }
        // 先从数据库从查询，如果没有就从网络上去取
        areaList = findMeDB.loadAreaList(areaCode);
        if( areaList == null || areaList.size()== 0 ){
            showProgereeDialog();
            // 从网络上GET取JSON数据回来，新开线程
            Global.sendHttpRequestWithHttpClient(address, new HttpCallbackListener() {
                @Override
                public void onFinish(String response) {
                    // 数据返回后，判断还有没有子城市数据，并处理返回数据
                    final boolean result = Global.handleAreaResponse(findMeDB, response, areaCode);
                    // 通过runOnUiThread方法回到主线程处理逻辑
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            // 如果有子城市数据就读取出来
                            if( result ){
                                queryAreas(areaCode, address);
                            }else{// 如果没有的话，就按照最后用户选择的城市返回PublishInfoActivity
                                backToPublishCaseActivity();
                            }

                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            });
        }
        dataList.clear();
        for( Area area : areaList ){
            dataList.add(area.getAreaName());
        }
        adapter.notifyDataSetChanged();
        areaListView.setSelection(0);

    }

    /**
     * 退回去发布信息，并把用户选的省市区信息返回
     */
    private void backToPublishCaseActivity(){
        String province, city, district;
        int code;
        // 如果父Code是000000，证明只有省级，没有省级以下的子城市
        if( selectedArea.getAreaFatherCode() == 100000 ){
            province = selectedArea.getAreaName();
            city = "";
            district = "";
        }else if( selectedArea.getAreaSelfCode()%100 == 0  ){// 如果自身code % 100求余得0，则证明是类似442000之类，是市级
            province = findMeDB.loadArea(selectedArea.getAreaFatherCode()).getAreaName();
            city = selectedArea.getAreaName();
            district = "";
        }else{//接下来就是有完整三级城市的
            district = selectedArea.getAreaName();
            // 向上查询对应的城市名
            city = findMeDB.loadArea(selectedArea.getAreaFatherCode()).getAreaName();
            // 向上两级查询对应的省名
            province = findMeDB.loadArea(findMeDB.loadArea(selectedArea.getAreaFatherCode()).getAreaFatherCode()).getAreaName();
        }
        code = selectedArea.getAreaSelfCode();
        Intent intentToPublishCase = new Intent();
        intentToPublishCase.putExtra("province",province);
        intentToPublishCase.putExtra("city",city);
        intentToPublishCase.putExtra("district",district);
        intentToPublishCase.putExtra("code",code);// 把最终选择的城市代码返回
        setResult(RESULT_OK, intentToPublishCase);
        finish();
    }

    /**
     * 显示弹窗
     */
    private void showProgereeDialog() {
        if( progressDialog == null ){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭弹窗
     */
    private void closeProgressDialog(){
        if( progressDialog != null ){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        // 先判断现在处于什么级别
        int nowCode = areaList.get(0).getAreaSelfCode();
        if( nowCode % 10000 == 0 ){// 用现在的列表的代码除以10000求余等于0则表示在省级，则销毁返回
            finish();
        }else if( nowCode % 10000 !=0 && nowCode % 100 == 0 ){// 表示在市级
            queryAreas(initCode, initAddress);
        }else{// 其他就是在区级
            queryAreas(selectedArea.getAreaFatherCode(), addressHeader+selectedArea.getAreaFatherCode()+addressEnd);
        }
    }
}
