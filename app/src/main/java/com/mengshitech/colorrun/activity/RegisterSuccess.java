package com.mengshitech.colorrun.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.mengshitech.colorrun.R;

/**
 * Created by kanghuicong on 2016/7/26  12:35.
 * 515849594@qq.com
 */
public class RegisterSuccess extends Activity {
    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user_id = bundle.getString("user_id");
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
            SharedPreferences mySharedPreferences = getSharedPreferences("user_type", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("user_type", "1");
            editor.putString("user_id",user_id);
            editor.commit();
            finish();
        }
    };
}
