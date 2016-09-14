package com.mengshitech.colorrun.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.widget.Toast;
import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.GetSDKVersion;
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

        if (GetSDKVersion.getAndroidSDKVersion() >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION},
                        001);
            } else {
                initView();
                ContentCommon.PermissionsState=1;
            }
        } else {
            ContentCommon.PermissionsState=1;
            initView();
        }
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
            SharedPreferences sharedPreferences = getSharedPreferences("user_type", Activity.MODE_PRIVATE);
            String type = sharedPreferences.getString("user_type", "");

            ContentCommon.user_id = sharedPreferences.getString("user_id", "");


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 001) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                initView();
            } else {
              Toast.makeText(SplashActivity.this,"获取权限失败,部分功能将无法使用",Toast.LENGTH_LONG).show();
                initView();
            }

        }

    }




}
