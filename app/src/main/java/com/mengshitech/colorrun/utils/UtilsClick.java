package com.mengshitech.colorrun.utils;

/**
 * Created by kanghuicong on 2016/8/19  15:34.
 */
public class UtilsClick {
    private static long lastClickTime;
    public synchronized static boolean isFastClick(int wait_time) {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < wait_time) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
