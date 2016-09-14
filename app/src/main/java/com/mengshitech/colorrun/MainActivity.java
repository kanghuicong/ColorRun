package com.mengshitech.colorrun;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mengshitech.colorrun.fragment.history.HistoryFragment;
import com.mengshitech.colorrun.fragment.lerun.LerunFragment;
import com.mengshitech.colorrun.fragment.me.MeFragment;
import com.mengshitech.colorrun.fragment.show.ShowFragment;
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
    public static RadioGroup rgMain;
    public static FrameLayout rgMainBottom;
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
    private ShowFragment mShowFragment;
    private HistoryFragment mHistoryFragment;
    private MeFragment mMeFragment;
    private long mExitTime;
    private ConnectivityManager connectivityManager;
    private Fragment fg;    // fg记录当前的Fragment
    public static ImageView iv_bullet_red;

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
        rgMainBottom = (FrameLayout) findViewById(R.id.rgMainBottom);
        rgMain = (RadioGroup) findViewById(R.id.rgMainbottom);
        rgMain.check(R.id.rbRun);
        // 进入主页面，初始页面pager为乐跑
        rbRun = (RadioButton) findViewById(R.id.rbRun);
        // rb乐跑
        rbShow = (RadioButton) findViewById(R.id.rbShow);
        // rb秀
        rbHistory = (RadioButton) findViewById(R.id.rbHistory);
        // rb历史
        rbMe = (RadioButton) findViewById(R.id.rbMe);
        // rb我的
        iv_bullet_red = (ImageView)findViewById(R.id.iv_bullet_red);

        rbHistory.setOnClickListener(this);
        rbMe.setOnClickListener(this);
        rbRun.setOnClickListener(this);
        rbShow.setOnClickListener(this);
        CheckState();
    }

    private void CheckState() {
        if (ContentCommon.myshowstate.equals("1")){
            iv_bullet_red.setVisibility(View.VISIBLE);
        }else {
            iv_bullet_red.setVisibility(View.GONE);
        }
    }

    private void initFragment() {
        //一开始先初始到lerunFragment
        fm = getSupportFragmentManager();
        lerunFragment = new LerunFragment();
        Utility.replace2MainFragment(fm, lerunFragment);

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
                break;
            case R.id.rbShow:
                if (mShowFragment == null) {
                    mShowFragment = new ShowFragment();
                    transaction.add(R.id.flMain, mShowFragment);
                } else {
                    transaction.show(mShowFragment);
                }
                break;
            case R.id.rbHistory:
                if (mHistoryFragment == null) {
                    mHistoryFragment = new HistoryFragment();
                    transaction.add(R.id.flMain, mHistoryFragment);
                } else {
                    transaction.show(mHistoryFragment);
                }
                break;
            case R.id.rbMe:
                if (mMeFragment == null) {
                    mMeFragment = new MeFragment();
                    transaction.add(R.id.flMain, mMeFragment);
                } else {
                    transaction.show(mMeFragment);
                }
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
        CheckState();
        IntentFilter filter = new IntentFilter();
        filter.addAction(connectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver, filter);
    }

    // 动态注销广播
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    //Edittext 点击空白位置隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }
    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }
    //多种隐藏软件盘方法的其中一种
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}