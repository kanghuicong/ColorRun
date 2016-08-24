package com.mengshitech.colorrun.utils;

/**
 * 作者：wschenyongyin on 2016/8/24 16:40
 * 说明:
 */
public class GetSDKVersion {
    public GetSDKVersion(){

    }
    public static int getAndroidSDKVersion() {
        int version = 0;
        try {
            version = Integer.valueOf(android.os.Build.VERSION.SDK);
        } catch (NumberFormatException e) {

        }
        return version;
    }
}
