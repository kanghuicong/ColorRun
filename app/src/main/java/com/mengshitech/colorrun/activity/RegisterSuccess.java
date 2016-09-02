package com.mengshitech.colorrun.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanghuicong on 2016/7/26  12:35.
 * 515849594@qq.com
 */
public class RegisterSuccess extends Activity {
    String type,userid,userpwd;
    TextView tv_register_success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_success);
        tv_register_success = (TextView)findViewById(R.id.tv_register_success);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        type = bundle.getString("type");
        userid = bundle.getString("userid");
        userpwd = bundle.getString("userpwd");
        if (type.equals("find_pwd")){
            tv_register_success.setText("修改密码成功！");
        }
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
            if (type.equals("register")){
                ContentCommon.login_state = "1";
                ContentCommon.user_id = userid;
                new Thread(user_runnable).start();
            }else {
                finish();
            }
        }
    };

    Runnable user_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "user");
            map.put("user_id", userid);
            Log.i("Userid", "" + userid);
            map.put("index", "4");
            String result = HttpUtils.sendHttpClientPost(path, map, "utf-8");
            Message msg = new Message();
            msg.obj = result;
            user_handler.sendMessage(msg);
        }
    };

    Handler user_handler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            try {
                UserEntiy userEntiy = JsonTools.getUserInfo("result", result);
                UserDao dao = new UserDao(RegisterSuccess.this);
                dao.add(userEntiy.getUser_id(),
                        userEntiy.getUser_name(),
                        userEntiy.getUser_header(),
                        userEntiy.getUser_phone(),
                        userEntiy.getUser_email(),
                        userEntiy.getUser_sex(),
                        userEntiy.getUser_height(),
                        userEntiy.getUser_weight(),
                        userEntiy.getUser_address(),
                        userEntiy.getUser_sign());

                Context context = RegisterSuccess.this;
                Intent intent = new Intent();
                intent.setAction("refresh");
                context.sendBroadcast(intent);
                finish();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
            // 监听到返回按钮点击事件
            return true;
        }
        return false;
    }
}
