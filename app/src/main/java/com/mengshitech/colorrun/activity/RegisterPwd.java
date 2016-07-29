package com.mengshitech.colorrun.activity;

import android.app.Activity;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;

/**
 * Created by kanghuicong on 2016/7/26  12:25.
 * 515849594@qq.com
 */
public class RegisterPwd extends Activity {
    private EditText pwd, repwd;
    private Button commit;
    private TextView tvnumber;
    private String password, repassword, number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO �Զ���ɵķ������
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pwd);
        // 获取从上一个activity传过来的手机号码
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        tvnumber = (TextView) findViewById(R.id.tv_registered_3_number);
        tvnumber.setText(bundle.getString("number"));
        // 获取布局控件
        pwd = (EditText) findViewById(R.id.et_registered3_pwd);
        repwd = (EditText) findViewById(R.id.et_registered3_repwd);
        commit = (Button) findViewById(R.id.btn_registered3_commit);
        number = bundle.getString("number");

        // 为提交按钮设置监听事件
        commit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                password = pwd.getText().toString();
                repassword = repwd.getText().toString();
                // 先判断密码是否符合要求
                if (ispwd(password)) {
                    if (password.equals(repassword)) {
                        new Thread(runnable).start();
                    } else {
                        Toast.makeText(RegisterPwd.this,
                                "两次输入的密码不相同，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterPwd.this, "您输入的密码不符合规范",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            String path = IPAddress.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("user_id", number);
            map.put("user_pwd",repassword);
            map.put("index", "0");
            map.put("flag","user");
            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };

    // 执行runnable1
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            Log.i("result",result);
            // 判断从服务器端返回来的值是否为1，如果为1则注册成功，同时将用户信息存入sqlite数据库并跳转到注册成功页面
            if (result.equals("timeout")){
                Toast.makeText(RegisterPwd.this, "连接服务器失败！",
                        Toast.LENGTH_SHORT).show();
            }else if(result.equals("0")) {
                Toast.makeText(RegisterPwd.this, "注册失败！",
                        Toast.LENGTH_SHORT).show();
            }else if(result.equals("1")) {
                Intent intent = new Intent(RegisterPwd.this,RegisterSuccess.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id",number);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }else if(result.equals("2")) {
                Toast.makeText(RegisterPwd.this, "用户已经存在！",
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

        // ʵ实现密码必须由数字 大写字母 小写字母 特殊字符 至少两种或两种以上组成
        public static boolean ispwd(String pwd) {
            String pwdPattern = "^(?![A-Z]*$)(?![a-z]*$)(?![0-9]*$)(?![^a-zA-Z0-9]*$)\\S+$";

            boolean result = Pattern.matches(pwdPattern, pwd);
            return result;
        }
}
