package com.mengshitech.colorrun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mengshitech.colorrun.R;

/**
 * Created by kanghuicong on 2016/7/26  12:35.
 * 515849594@qq.com
 */
public class RegisterSuccess extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                handler.sendEmptyMessage(0);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            finish();
        }
    };
}
