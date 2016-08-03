package com.mengshitech.colorrun.fragment.lerun;

import android.app.Activity;
import android.view.View;
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

    public LeRunTheme(Activity activity,String type) {
        mActivity = activity;
        this.type = type;
        initView();
    }

    @Override
    public View initView() {
        lerun_theme_view = View.inflate(mActivity, R.layout.lerun_theme_type, null);
        browser=(WebView)lerun_theme_view.findViewById(R.id.web_type);
        switch (type){
            case "pop":
                browser.loadUrl("http://www.sina.com.cn");
                GetWeb();
                break;
            case "color":
                browser.loadUrl("http://www.sina.com.cn");
                GetWeb();
                break;
            case "marathon":
                browser.loadUrl("http://www.sina.com.cn");
                GetWeb();
                break;
            case "rainbow":
                browser.loadUrl("http://www.sina.com.cn");
                GetWeb();
                break;
            case "light":
                browser.loadUrl("http://www.sina.com.cn");
                GetWeb();
                break;
        }
//        webview.loadData(customHtml, "text/html", "UTF-8");

        return lerun_theme_view;
    }

    private void GetWeb() {
        //设置可自由缩放网页
        browser.getSettings().setSupportZoom(true);
        browser.getSettings().setBuiltInZoomControls(true);

        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，
        // 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象
        browser.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
    }

}
