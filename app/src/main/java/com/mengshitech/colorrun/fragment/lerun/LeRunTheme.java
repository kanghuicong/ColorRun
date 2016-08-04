package com.mengshitech.colorrun.fragment.lerun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.MainBackUtility;


/**
 * Created by kanghuicong on 2016/8/2  19:32.
 */
public class LeRunTheme extends BaseFragment {
    private Activity mActivity;
    View lerun_theme_view;
    String type;
    WebView browser;

    @SuppressLint("ValidFragment")
    public LeRunTheme(Activity activity,String type) {
        super();
        mActivity = activity;
        this.type = type;
        initView();
    }

    public LeRunTheme()
    {}
    @Override
    public View initView() {
        lerun_theme_view = View.inflate(mActivity, R.layout.lerun_theme_type, null);
        browser=(WebView)lerun_theme_view.findViewById(R.id.web_type);
        WebSettings wb = browser.getSettings();
        wb.setDefaultTextEncodingName("utf-8");
        switch (type){
            case "pop":
                browser.loadUrl("file:///android_asset/popRuns.html");
                break;
            case "color":
                browser.loadUrl("file:///android_asset/ColorRun.html");
                break;
            case "marathon":
                browser.loadUrl("file:///android_asset/popRuns.html");
                break;
            case "rainbow":
                browser.loadUrl("file:///android_asset/popRuns.html");
                break;
            case "light":
                browser.loadUrl("file:///android_asset/popRuns.html");
                break;
        }
        return lerun_theme_view;
    }

}
