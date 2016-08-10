package com.mengshitech.colorrun.fragment.lerun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.MainBackUtility;

/**
 * Created by kanghuicong on 2016/8/3  11:54.
 */
public class LerunVideo extends BaseFragment {
    private Activity mActivity;
    View lerun_theme_view;
    WebView browser;
    String uri;

    @SuppressLint("ValidFragment")
    public LerunVideo(Activity activity,String uri) {
        mActivity = activity;
        this.uri = uri;
        initView();
    }

    public  LerunVideo(){}

    @Override
    public View initView() {
        if (lerun_theme_view == null) {
            lerun_theme_view = View.inflate(mActivity, R.layout.lerun_hotvideo, null);
            MainBackUtility.MainBack(lerun_theme_view, "热门视频", getFragmentManager());
            init();
        }
        ViewGroup parent = (ViewGroup) lerun_theme_view.getParent();
        if (parent != null) {
            parent.removeView(lerun_theme_view);
        }
        return lerun_theme_view;
    }

    private void init(){
        browser=(WebView)lerun_theme_view.findViewById(R.id.web_video);
        //WebView加载web资源
        browser.loadUrl("http://baidu.com");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        browser.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
}
