package com.mengshitech.colorrun.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kanghuicong on 2016/8/23  16:42.
 */
public class DateUtils {

    private static SimpleDateFormat sf = null;
    //获取系统时间 格式为："yyyy-MM-dd HH:mm:ss"
    public static String getCurrentDate() {
        Date d = new Date();
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    //时间戳转换成字符窜
    public static String getDateToString(long time) {
        Date d = new Date(time);
        sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    //将字符串转为时间戳
    public static long getStringToDate(String time) {
        sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try{
            date = sf.parse(time);
            }
        catch(ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getDate(String time){
        long m;
        String time_unit = null;
        long time_issuance = getStringToDate(time);
        long time_now = new Date().getTime();
        long time_difference = time_now - time_issuance;
        if (time_difference < 86400000*7) {
            if (time_difference < 60000) {
                time_unit = "刚刚";
            }
            if (time_difference >= 60000 && time_difference < 3600000) {
                m = time_difference / 60000;
                time_unit = m + "分钟前";
            }
            if (time_difference >= 3600000 && time_difference < 86400000) {
                m = time_difference / 3600000;
                time_unit = m + "小时前";
            }
            if (time_difference >= 86400000 && time_difference < 86400000 * 7) {
                m = time_difference / 86400000;
                time_unit = m+"天前";
            }
        }else {
            time_unit = time;
        }
        return time_unit;
    }
}
