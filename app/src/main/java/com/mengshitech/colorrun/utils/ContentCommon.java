package com.mengshitech.colorrun.utils;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghuicong on 2016/7/22  11:19.
 * 515849594@qq.com
 */
public class ContentCommon {


    public static final String PATH="http://121.43.172.150:8080/LeRun/servlet/LeRunServlet";

    public static final String ImagePath ="http://121.43.172.150:8080/LeRun/servlet/UploadServlet";

    public static final String path="http://121.43.172.150:8080/LeRun/";


//    public static final String PATH="http://192.168.188.245:8080/LeRun/servlet/LeRunServlet";
//
//    public static final String ImagePath ="http://192.168.188.245:8080/LeRun/servlet/UploadServlet";
//
//    public static final String path="http://192.168.188.245:8080/LeRun/";


    public static String login_state="0";
    public static String user_id=null;
    public static String user_log=null;
    public static String user_name=null;
    public static Boolean INTENT_STATE=true;
    public static int into_lerun_type = 0;

    public static ArrayList<String> ShowImageList=null;

}
