package com.example.findme.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.example.findme.activity.LoginActivity;
import com.example.findme.db.FindMeDB;
import com.example.findme.model.Area;
import com.example.findme.model.PopupTypeItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mark on 2014/12/18.
 */
public class Global {

    public static final int LOGIN = 1;// 登录返回的requestCode

    public static final int CHANGE_USER_INFO = 2;//启动更改用户信息requestCode

    public static final int GRALLERY = 3;//启动相册的requestCode

    public static final int TAKE_PHOTO = 4;// 启动相机的requestCode

    public static final int GET_PHOTO = 5;//启动获取图片的requestCode

    public static final int PUBLISH = 6;// 去发布信息返回的requestCode

    public static final int AREA = 7;// 去选择行政区域的requestCode
                                                // 11       12        13       14         15          16
    public static final String[] CASE_TYPE = {"寻人启事", "寻宠启事", "寻物启事", "失人招领", "失宠招领", "失物招领"};
                                                 // 21      22
    public static final String[] PLACE_TYPE = {"地点范围", "交通工具"};
                                            // 1101             1102              1103
    public static final String[] PERSON = {"(男)婴幼儿0-2岁", "(女)婴幼儿0-2岁", "(男)儿童3-6岁", "(女)儿童3-6岁", "(男)少年7-14岁", "(女)少年7-14岁", "(男)青年15-35岁", "(女)青年15-35岁", "(男)中年36-60岁", "(女)中年36-60岁", "(男)老年61岁+", "(女)老年61岁+"};
                                        //1201 1202
    public static final String[] PET = {"狗", "猫", "乌龟", "其他"};

    public static final String[] THING = {"钱包", "钥匙", "手机", "证件", "文件", "其他"};

    public static final String[] PLACE = {"住宅/社区","公交站牌", "饭馆", "酒店", "公园", "商场", "火车站", "机场", "汽车站", "其他"};

    public static final String[] TRANSPORTATION = {"公共汽车", "地铁", "长途汽车", "的士", "飞机", "船", "其他"};
    /**
     * 当用户没登录时点击了某些需要登录的功能，则跳转去登录界面
     */
    public static void toLogin(Fragment fragment){
        Intent intent = new Intent(fragment.getActivity(), LoginActivity.class);
        // 要先获取宿主getActivity，再用宿主的startActivityFromFragment方法去启动跳转，不然MyFragment的onActivityResult不会执行，只会执行宿主的
        fragment.getActivity().startActivityFromFragment(fragment, intent, LOGIN);
    }

    public static List<PopupTypeItem> getPopupTypeList(int i){
        List<PopupTypeItem> list = new ArrayList<PopupTypeItem>();
        switch ( i ){
            case 11:
                for( int j=0 ; j<CASE_TYPE.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(CASE_TYPE[j]);
                    list.add(item);
                }
                break;
            case 21:
                for( int j=0 ; j<PLACE_TYPE.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(PLACE_TYPE[j]);
                    list.add(item);
                }
                break;
            case 1101:
                for( int j=0 ; j<PERSON.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(PERSON[j]);
                    list.add(item);

                }
                break;
            case 1201:
                for( int j=0 ; j<PET.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(PET[j]);
                    list.add(item);
                }
                break;
            case 1301:
                for( int j=0 ; j<THING.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(THING[j]);
                    list.add(item);
                }
                break;
            case 1401:
                for( int j=0 ; j<PERSON.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(PERSON[j]);
                    list.add(item);
                }
                break;
            case 1501:
                for( int j=0 ; j<PET.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(PET[j]);
                    list.add(item);
                }
                break;
            case 1601:
                for( int j=0 ; j<THING.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(THING[j]);
                    list.add(item);
                }
                break;
            case 2101:
                for( int j=0 ; j<PLACE.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(PLACE[j]);
                    list.add(item);
                }
                break;
            case 2201:
                for( int j=0 ; j<TRANSPORTATION.length ; j++ ){
                    PopupTypeItem item = new PopupTypeItem();
                    item.setId(i+j);
                    item.setItemName(TRANSPORTATION[j]);
                    list.add(item);
                }
                break;
            default:
                break;
        }

        return list;
    }

    /**
     * 使用URLConnection向网络发送请求并监听
     */
    public static void sendHttpRequestWithURLConnection(final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try{
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while( (line = reader.readLine())!=null ){
                        response.append(line);
                    }
                    if( listener != null ){
                        //回调onFinish方法
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                        if( listener != null ){
                            listener.onError(e);
                        }
                }finally{
                    if( connection != null ){
                        connection.disconnect();
                    }

                }
            }
        }).start();
    }

    /**
     * 使用HTTPCLIENT向网络发送请求
     */
    public static void sendHttpRequestWithHttpClient(final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(address);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if(httpResponse.getStatusLine().getStatusCode() == 200){
                        //请求和响应都成功了
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "utf-8");
                        if( listener != null ){
                            listener.onFinish(response);
                        }
                    }
                }catch(Exception e){
                    if( listener != null ){
                        listener.onError(e);
                    }
                }
            }
        }).start();

    }

    /**
     * 处理从网络回传回来的省市区数据，并存入数据库
     */
    public static boolean handleAreaResponse(FindMeDB findMeDB, String response, int fatherCode){
        try{
            JSONArray jsonArray = new JSONObject(response).getJSONArray("result");
            jsonArray = jsonArray.getJSONArray(0);
            if( jsonArray.length()== 0 ){
                return false;
            }else{
                // 先判断是否直辖市，是的话，补一个city的资料
                int tmpCode1 = fatherCode % 10000;// 如果是类似110000的省级则求余数等于0
                int tmpCode2 = Integer.parseInt(jsonArray.getJSONObject(0).getString("id"))%10;// 如果是类似110101的区级则求余不等于0
                int tmpCode = fatherCode;
                if( tmpCode1 == 0 && tmpCode2 != 0 ){// 如果tmpCode1是省级，且tmpCode2是区级，此条件成立则是直辖市
                    // 如果是直辖市，没有市级，则我们自己手动添加一个跟省级同名的市级
                    Area area = new Area();
                    area.setAreaFatherCode(fatherCode);
                    area.setAreaName(findMeDB.loadArea(fatherCode).getAreaName());
                    // 比如110000，改成110100当作自身Code
                    tmpCode = tmpCode+100;
                    area.setAreaSelfCode(tmpCode);
                    findMeDB.saveArea(area);
                }
                for( int i=0 ;i < jsonArray.length() ; i++ ){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Area area = new Area();
                    area.setAreaFatherCode(tmpCode);
                    area.setAreaName(jsonObject.getString("fullname"));
                    area.setAreaSelfCode(Integer.parseInt(jsonObject.getString("id")));
                    findMeDB.saveArea(area);
                }
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
