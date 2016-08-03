package com.mengshitech.colorrun.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * atenklsy
 */
public class LoginActivity extends Activity implements OnClickListener {
    EditText etUserId, etUserPwd;
    Button btnLogin, btnRegister;
    SharedPreferences spAccount;
    String userId;
    String userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView() {

        spAccount = getSharedPreferences("sp_account", MODE_PRIVATE);
        // sharedpreferences
        etUserId = (EditText) findViewById(R.id.et_name);
        etUserId.setOnClickListener(this);
        // 用户名输入框
        etUserPwd = (EditText) findViewById(R.id.et_pwd);
        etUserPwd.setOnClickListener(this);
        // 密码输入框
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        // 登录按钮
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(this);
        // 注册按钮
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.et_name:
                etUserId.setCursorVisible(true);
                break;
            case R.id.et_pwd:
                etUserPwd.setCursorVisible(true);
                break;
            case R.id.btn_login:
                // 登录按钮的点击事件
                userId = etUserId.getText().toString();
                userPwd = etUserPwd.getText().toString();
                if (TextUtils.isEmpty(userId) || TextUtils.isEmpty(userPwd)) {
                    // 账号或密码为空
                    Toast.makeText(LoginActivity.this, "请输入正确的账号和密码", Toast.LENGTH_SHORT).show();
                    etUserId.setText("");
                    etUserPwd.setText("");
                } else {
                    new Thread(runnable).start();
                }

                break;
            case R.id.btn_register:
                // 注册按钮的点击事件
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;

            default:
                break;
        }
    }

    @SuppressLint("CommitPrefEdits")
    private void setAccount2SP(String userId, String userPwd) {
        SharedPreferences.Editor editor = spAccount.edit();
        editor.putString("userId", userId);
        editor.putString("userPwd", userPwd);
        editor.commit();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = IPAddress.PATH;
            Map<String,String> map= new HashMap<String,String>();
            map.put("flag","user");
            map.put("index","1");
            map.put("user_id",userId);
            map.put("user_pwd",userPwd);

            String result = HttpUtils.sendHttpClientPost(path,map,"utf-8");
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);

        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(LoginActivity.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                switch (result) {
                    case "0":
                        Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                        break;
                    case "1":
                        SharedPreferences mySharedPreferences = getSharedPreferences("user_type", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("user_type", "1");
                        editor.putString("user_id",userId);
                        IPAddress.login_state="1";
                        editor.commit();
                        Intent inetnt = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(inetnt);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        }
    };
}
