package com.mengshitech.colorrun.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.ToolKits;

/**
 * atenklsy
 */
public class SplashActivity extends Activity {
    /**
     * 3秒闪屏页，用来判断网络、初始化数据、确认用户名是否登录等
     */
    public static final String IS_FIRST = "is_first";
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {

        new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message arg0) {

                if (!ToolKits
                        .fetchBooble(SplashActivity.this, IS_FIRST, false)) {
                    startActivity(new Intent(SplashActivity.this,
                            GuideActivity.class));
                    ToolKits.putBooble(SplashActivity.this, IS_FIRST, true);
                    finish();
                } else {
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            judgeHandler.sendEmptyMessageDelayed(0, 0);

                        }
                    }).start();

                }
                ToolKits.putBooble(SplashActivity.this, IS_FIRST, true);
                return true;
            }
        }).sendEmptyMessageDelayed(0, 2000);
    }

    private Handler judgeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            NetworkInfo phoneinfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiinfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            SharedPreferences sharedPreferences =getSharedPreferences("user_type", Activity.MODE_PRIVATE);
            String type = sharedPreferences.getString("user_type", "");

            ContentCommon.user_id= sharedPreferences.getString("user_id", "");

            Log.i("登陆状态",type+"");
            ContentCommon.login_state = type;

            if (!(phoneinfo.isConnected()) && !(wifiinfo.isConnected())) {
                startActivity(new Intent(SplashActivity.this,
                        MainActivity.class));
                finish();
            } else {

                startActivity(new Intent(SplashActivity.this,
                        AdvertisementActivity.class));
                finish();
            }
        }

    };


}
