package com.mengshitech.colorrun;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mengshitech.colorrun.activity.AdvertisementActivity;
import com.mengshitech.colorrun.activity.SplashActivity;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.fragment.history.HistoryFragment;
import com.mengshitech.colorrun.fragment.lerun.LerunEventListView;
import com.mengshitech.colorrun.fragment.lerun.LerunFragment;
import com.mengshitech.colorrun.fragment.me.meFragment;
import com.mengshitech.colorrun.fragment.show.showFragment;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.Utility;


/**
 * 主页面
 *
 * @author atenklsy
 * @time 2016.7.7
 */

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity implements OnClickListener {
    public static RadioGroup rgMainBottom;
    private static boolean isExit = false;// 定义一个变量，来标识是否退出
    private RadioButton rbMe, rbHistory, rbRun, rbShow;
    private FragmentManager fm;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private LerunFragment lerunFragment;
    private showFragment mShowFragment;
    private HistoryFragment mHistoryFragment;
    private meFragment mMeFragment;
    private long mExitTime;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        initFragment();

        rgMainBottom = (RadioGroup) findViewById(R.id.rgMainBottom);
        rgMainBottom.check(R.id.rbRun);
        // 进入主页面，初始页面pager为乐跑
        rbRun = (RadioButton) findViewById(R.id.rbRun);
        // rb乐跑
        rbShow = (RadioButton) findViewById(R.id.rbShow);
        // rb秀
        rbHistory = (RadioButton) findViewById(R.id.rbHistory);
        // rb历史
        rbMe = (RadioButton) findViewById(R.id.rbMe);
        // rb我的
        rbHistory.setOnClickListener(this);
        rbMe.setOnClickListener(this);
        rbRun.setOnClickListener(this);
        rbShow.setOnClickListener(this);

    }

    private void initFragment() {
        //一开始先初始到lerunFragment
        fm = getSupportFragmentManager();
        Utility.replace2MainFragment(fm, new LerunFragment());

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = fm.beginTransaction();
        hideAllFragment(transaction);
        switch (v.getId()) {
            case R.id.rbRun:
                if (lerunFragment == null) {
                    lerunFragment = new LerunFragment();
                    transaction.add(R.id.flMain, lerunFragment);
                } else {
                    transaction.show(lerunFragment);
                }
//                Utility.replace2MainFragment(fm, new LerunFragment());
                break;
            case R.id.rbShow:
                if (mShowFragment == null) {
                    mShowFragment = new showFragment();
                    transaction.add(R.id.flMain, mShowFragment);
                } else {
                    transaction.show(mShowFragment);
                }
                //Utility.replace2MainFragment(fm, new showFragment());
                break;
            case R.id.rbHistory:
                if (mHistoryFragment == null) {
                    mHistoryFragment = new HistoryFragment();
                    transaction.add(R.id.flMain, mHistoryFragment);
                } else {
                    transaction.show(mHistoryFragment);
                }
//                Utility.replace2MainFragment(fm, new HistoryFragment());
                break;
            case R.id.rbMe:
                if (mMeFragment == null) {
                    mMeFragment = new meFragment();
                    transaction.add(R.id.flMain, mMeFragment);
                } else {
                    transaction.show(mMeFragment);
                }
//                Utility.replace2MainFragment(fm, new meFragment());
                break;
            default:
                break;
        }
        transaction.commit();
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if (lerunFragment != null) {
            transaction.hide(lerunFragment);
        }
        if (mShowFragment != null) {
            transaction.hide(mShowFragment);
        }
        if (mHistoryFragment != null) {
            transaction.hide(mHistoryFragment);
        }
        if (mMeFragment != null) {
            transaction.hide(mMeFragment);
        }
    }

    //检测是否有网络连接
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            NetworkInfo phoneinfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiinfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (!(phoneinfo.isConnected()) && !(wifiinfo.isConnected())) {
                Toast.makeText(MainActivity.this, "无网络连接，内容加载失败", Toast.LENGTH_SHORT).show();
                ContentCommon.INTENT_STATE = false;

            } else {
                ContentCommon.INTENT_STATE = true;
            }
        }
    };

    // 动态注册广播
    protected void onResume() {

        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(connectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }

    ;

    // 动态注销广播
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }


}