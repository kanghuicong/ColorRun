package com.mengshitech.colorrun.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by kanghuicong on 2016/8/17  10:18.
 */
public class FontTextSize {

    public int getFontSize(Context context, int textSize) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;

        int rate = (int) (textSize * (float) screenHeight / 1280);
        return rate;
    }
}
