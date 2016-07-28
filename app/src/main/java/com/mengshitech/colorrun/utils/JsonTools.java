package com.mengshitech.colorrun.utils;

import android.util.Log;

import com.mengshitech.colorrun.bean.LunBoEntity;
import com.mengshitech.colorrun.bean.OrderEntity;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.bean.UserEntiy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghuicong on 2016/7/22  13:55.
 * 515849594@qq.com
 */
public class JsonTools {


    //解析返回的状态码
    public static int getState(String key, String jsonString) throws JSONException {
        JSONObject object = new JSONObject(jsonString);
        int state = object.getInt(key);
        return state;
    }


    //解析返回的数据
    public static String getDatas(String jsonString) throws JSONException {
        String result;
        int state = getState("state", jsonString);
        if (state == 1) {
            JSONObject object = new JSONObject(jsonString);
            result = object.getString("datas");
        } else {
            result = null;
        }

        return result;
    }


    public static UserEntiy getUserInfo(String key, String jsonString)
            throws JSONException {
        UserEntiy info = new UserEntiy();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject userObject = jsonObject.getJSONObject(key);

        info.setUser_address(userObject.getString("user_id"));
        info.setUser_email(userObject.getString("user_email"));
        info.setUser_header(userObject.getString("user_header"));
        info.setUser_height(userObject.getString("user_height"));
        info.setUser_id(userObject.getString("user_id"));
        info.setUser_name(userObject.getString("user_name"));
        info.setUser_pwd(userObject.getString("user_pwd"));
        info.setUser_sex(userObject.getString("user_sex"));
        info.setUser_state(userObject.getString("user_state"));
        info.setUser_weight(userObject.getString("user_weight"));
        info.setUser_sign(userObject.getString("user_sign"));
        info.setUser_phone(userObject.getString("user_phone"));

        return info;
    }

    public static List<OrderEntity> getOrderInfo(String key, String jsonString)
            throws JSONException {
        List<OrderEntity> list = new ArrayList<OrderEntity>();
        JSONObject jsonObject = new JSONObject(jsonString);

        JSONArray jsonArray = jsonObject.getJSONArray(key);
        for (int i = 0; i < jsonArray.length(); i++) {
            OrderEntity info = new OrderEntity();
            JSONObject orderObject = jsonArray.getJSONObject(i);

            info.setLerun_title(orderObject.getString("lerun_title"));
            info.setLerun_time(orderObject.getString("lerun_time"));
            info.setLerun_type(orderObject.getString("lerun_type"));
            info.setLerun_poster(orderObject.getString("lerun_poster"));
            info.setLerun_state(orderObject.getString("lerun_state"));
            info.setLerun_id(orderObject.getString("lerun_id"));
            info.setLerun_address(orderObject.getString("lerun_address"));
            list.add(info);
        }

        return list;
    }


    public static List<ShowEntity> getShowInfo(String key, String jsonString)
            throws JSONException {
        Log.i("jsonString", jsonString);
        List<ShowEntity> list = new ArrayList<ShowEntity>();

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("datas");

        for (int i = 0; i < jsonArray.length(); i++) {
            ShowEntity info = new ShowEntity();
            JSONObject showObject = jsonArray.getJSONObject(i);
            info.setUser_id(showObject.getString("user_id"));
            info.setUser_name(showObject.getString("user_name"));
            info.setShow_id(showObject.getString("show_id"));
            info.setUser_header(showObject.getString("user_header"));
            info.setShow_image(showObject.getString("show_image"));
            info.setShow_content(showObject.getString("show_content"));
            info.setComment_num(showObject.getString("comment_num"));
            info.setLike_num(showObject.getString("like_num"));
            info.setShow_time(showObject.getString("show_time"));

            list.add(info);
        }
        Log.i("list的大小", list.size() + "");
        return list;
    }

    public static List<String> getImageInfo(String jsonString)
            throws JSONException {

        Log.i("jsonString:", jsonString);

        JSONArray jsonArray = new JSONArray(jsonString);

        List<String> list = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String imagePath = jsonObject.getString("imagePath");
            list.add(IPAddress.path + imagePath);
        }
        Log.i("listdaixao:", list.size() + "");
        return list;
    }


    //获取轮播图u
    public static List<LunBoEntity> getLunboImageInfo(String key, String jsonString)
            throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("datas");
        List<LunBoEntity> lunbolist = new ArrayList<LunBoEntity>();
        for (int i = 0; i < jsonArray.length(); i++) {
            LunBoEntity info = new LunBoEntity();
            JSONObject showObject = jsonArray.getJSONObject(i);
            info.setLunbo_image(showObject.getString("lunbo_image"));
            lunbolist.add(info);
        }
        return lunbolist;
    }


}
