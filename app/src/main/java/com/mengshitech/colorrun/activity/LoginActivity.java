package com.mengshitech.colorrun.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * atenklsy
 */
public class LoginActivity extends Activity implements OnClickListener {
    EditText etUserId, etUserPwd;
    Button btnLogin;
    TextView tvRegister,tvFindpwd;
    SharedPreferences spAccount;
    String userId;
    String userPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        MainBackUtility.MainBackActivity(LoginActivity.this, "登入");
        initView();
    }

    private void initView() {
        // sharedpreferences
        spAccount = getSharedPreferences("sp_account", MODE_PRIVATE);
        // 用户名输入框
        etUserId = (EditText) findViewById(R.id.et_name);
        etUserId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        etUserId.setOnClickListener(this);
        // 密码输入框
        etUserPwd = (EditText) findViewById(R.id.et_pwd);
        etUserPwd.setOnClickListener(this);
        etUserPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
        // 登录按钮
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        // 注册按钮
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvRegister.setOnClickListener(this);
        //找回密码
        tvFindpwd = (TextView)findViewById(R.id.tv_find_pwd);
        tvFindpwd.setOnClickListener(this);

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
                    if (userPwd.length()>=6&&userPwd.length()<=16){
                        new Thread(runnable).start();
                    }else {
                        Toast.makeText(LoginActivity.this, "请输入6~16位密码", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.tv_register:
                // 注册按钮的点击事件
                Intent intent_register = new Intent(LoginActivity.this,RegisterActivity.class);
                Bundle bundle_register = new Bundle();
                bundle_register.putString("type","register");
                intent_register.putExtras(bundle_register);
                startActivity(intent_register);
                break;

            case R.id.tv_find_pwd:
                //找回密码
                Intent intent_find_pwd = new Intent(LoginActivity.this,RegisterActivity.class);
                Bundle bundle_find_pwd = new Bundle();
                bundle_find_pwd.putString("type","find_pwd");
                intent_find_pwd.putExtras(bundle_find_pwd);
                startActivity(intent_find_pwd);
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
            String path = ContentCommon.PATH;
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
                        ContentCommon.login_state="1";
                        ContentCommon.user_id=userId;
                        editor.commit();

                        UserDao dao = new UserDao(LoginActivity.this);
                        UserEntiy modler = dao.find(userId);
                        Log.i("UserEntiy modler",modler+"");
                        Log.i("UserEntiy","UserEntiy");
                        if (modler == null) {
                            new Thread(user_runnable).start();
                        }

//                        Intent inetnt = new Intent(LoginActivity.this, MainActivity.class);
//                        startActivity(inetnt);
                        finish();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    Runnable user_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String,String> map = new HashMap<String, String>();
            map.put("flag", "user");
            map.put("user_id",userId);
            map.put("index","4");
            String result = HttpUtils.sendHttpClientPost(path,map,"utf-8");
            Message msg = new Message();
            msg.obj = result;
            user_handler.sendMessage(msg);
        }
    };

    Handler user_handler = new Handler(){
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
                try {
                    UserEntiy userEntiy = JsonTools.getUserInfo("result",result);
                    UserDao dao = new UserDao(LoginActivity.this);
//                    UserEntiy modler = new UserEntiy(userEntiy.getUser_bankid(),
//                            userEntiy.getUser_id(),
//                            userEntiy.getUser_pwd(),
//                            userEntiy.getUser_name(),
//                            userEntiy.getUser_birthday(),
//                            userEntiy.getUser_sex(),
//                            userEntiy.getUser_header(),
//                            userEntiy.getUser_identity(),
//                            userEntiy.getUser_address(),
//                            userEntiy.getUser_fullname(),
//                            userEntiy.getUser_level(),
//                            userEntiy.getUser_height(),
//                            userEntiy.getUser_health(),
//                            userEntiy.getUser_weight(),
//                            userEntiy.getUser_sign(),
//                            userEntiy.getUser_phone(),
//                            userEntiy.getUser_email(),
//                            userEntiy.getUser_state(),
//                            userEntiy.getUser_otherid());
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

                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
    };
}
