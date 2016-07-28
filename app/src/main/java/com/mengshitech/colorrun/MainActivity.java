package com.mengshitech.colorrun;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mengshitech.colorrun.fragment.history.HistoryFragment;
import com.mengshitech.colorrun.fragment.lerun.LerunFragment;
import com.mengshitech.colorrun.fragment.me.meFragment;
import com.mengshitech.colorrun.fragment.show.showFragment;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //监听返回按钮
        exit();
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
            //如果2秒内没有退出，则变成false。重新按两次才能退出。
        } else {
            finish();
            System.exit(0);
        }
    }
}