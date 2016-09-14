package com.mengshitech.colorrun.activity;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
//import android.app.ProgressDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.customcontrols.ProgressDialog;
import com.mengshitech.colorrun.utils.MainBackUtility;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 作者：wschenyongyin on 2016/8/22 15:06
 * 说明:
 */
public class VideoActivity extends Activity {
    private WebView webView;
    private FrameLayout video_fullView;// 全屏时视频加载view
    private View xCustomView;
    private ProgressDialog waitdialog = null;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    private myWebChromeClient xwebchromeclient;

    private ImageView back;
    private TextView title;
    String video_url;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉应用标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video);

//        waitdialog = ProgressDialog.show(VideoActivity.this, "正在加载");


        Intent intent = getIntent();
        video_url = intent.getStringExtra("video_url");

        if (video_url == null || video_url.equals("")) {
            video_url = "http://www.jxkuafu.com/";
        }

//        waitdialog.show();
        webView = (WebView) findViewById(R.id.webView);
        video_fullView = (FrameLayout) findViewById(R.id.video_fullView);
        back = (ImageView) findViewById(R.id.title_back);
        title = (TextView) findViewById(R.id.title_barr);
        title.setText("热门视频");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        WebSettings ws = webView.getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        // ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕

        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。

        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);// 新加
        xwebchromeclient = new myWebChromeClient();
        webView.setWebChromeClient(xwebchromeclient);
        webView.setWebViewClient(new myWebViewClient());
        webView.loadUrl(video_url);
    }

    public class myWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        //设置加载时间
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            timer = new Timer();
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
//                    waitdialog.dismiss();
                }
            };
            timer.schedule(tt, 10000, 1);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            waitdialog.dismiss();
        }
    }

    public class myWebChromeClient extends WebChromeClient {
        private View xprogressvideo;

        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            webView.setVisibility(View.INVISIBLE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            video_fullView.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            video_fullView.setVisibility(View.VISIBLE);
        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {
            if (xCustomView == null)// 不是全屏播放状态
                return;

            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            video_fullView.removeView(xCustomView);
            xCustomView = null;
            video_fullView.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            webView.setVisibility(View.VISIBLE);
        }

        // 视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater
                        .from(VideoActivity.this);
                xprogressvideo = inflater.inflate(
                        R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }
    }

    /**
     * 判断是否是全屏
     *
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        xwebchromeclient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        webView.onResume();
        webView.resumeTimers();

        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
            webView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
            webView.freeMemory();
            webView.pauseTimers();
            webView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (inCustomView()) {
                // webViewDetails.loadUrl("about:blank");
                hideCustomView();
                return true;
            } else {
//                webView.loadUrl("about:blank");
                VideoActivity.this.finish();
            }
        }
        return false;
    }
}