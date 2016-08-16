package com.mengshitech.colorrun.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.utils.MainBackUtility;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


/**
 * atenklsy
 */
public class RegisterActivity extends Activity {
    private Button btn_registered, btn_getcode;
    private ImageView vc_image;// 用于显示验证码
    private String getcode = null;// 获取验证码的值֤���ֵ
    private TextView tv_now, tv_title, tv_conment;
    private EditText et_code, et_phone;
    private String number;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // 获取布局控件
        btn_registered = (Button) findViewById(R.id.btn_registered);
        et_code = (EditText) findViewById(R.id.et_code);
        et_phone = (EditText) findViewById(R.id.registered_et_usernumber);
        btn_getcode = (Button) findViewById(R.id.btn_getcode);
        tv_now = (TextView) findViewById(R.id.timer1);
        tv_title = (TextView) findViewById(R.id.tv_register_title);
        tv_conment = (TextView) findViewById(R.id.tv_register_conment);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        type = bundle.getString("type");
        if ("find_pwd".equals(type)) {
            MainBackUtility.MainBackActivity(RegisterActivity.this, "忘记密码？");
            btn_registered.setText("找回密码");
            tv_title.setText("找回密码");
            tv_conment.setText("");
        }else if("register".equals(type)) {
            MainBackUtility.MainBackActivity(RegisterActivity.this, "注册账号");
        }

        SMSSDK.initSDK(this, "15634230a63de", "4985c0555608c03b5aabff91ea045ecb");
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handler.sendMessage(msg);
            }

        };
        SMSSDK.registerEventHandler(eh);

        // 实现获取验证码短信按钮功能
        btn_getcode.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                    if (et_phone.getText().toString().trim().length() == 11) {
                        // 发送短信
                        number = et_phone.getText().toString().trim();
                        iPhone = et_phone.getText().toString().trim();
                        SMSSDK.getVerificationCode("86", iPhone);
                        et_code.requestFocus();
                        btn_getcode.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入完整电话号码",
                                Toast.LENGTH_LONG).show();
                        et_phone.requestFocus();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入您的电话号码",
                            Toast.LENGTH_LONG).show();
                    et_phone.requestFocus();
                }
            }
        });

        btn_registered.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!TextUtils.isEmpty(et_code.getText().toString().trim())) {
                    if (et_code.getText().toString().trim().length() == 4) {
                        iCord = et_code.getText().toString().trim();
                        SMSSDK.submitVerificationCode("86", iPhone, iCord);
                        flag = false;
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入完整验证码",
                                Toast.LENGTH_LONG).show();
                        et_code.requestFocus();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入验证码",
                            Toast.LENGTH_LONG).show();
                    et_code.requestFocus();
                }
            }
        });

    }

    private void reminderText() {
        tv_now.setVisibility(View.VISIBLE);
        handlerText.sendEmptyMessageDelayed(1, 1000);
    }

    Handler handlerText = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (time > 0) {
                    tv_now.setText("验证码已发送" + time + "秒");
                    time--;
                    handlerText.sendEmptyMessageDelayed(1, 1000);
                } else {
                    tv_now.setText("提示信息");
                    time = 60;
                    tv_now.setVisibility(View.GONE);
                    btn_getcode.setVisibility(View.VISIBLE);
                }
            } else {
                et_code.setText("");
                tv_now.setText("提示信息");
                time = 60;
                tv_now.setVisibility(View.GONE);
                btn_getcode.setVisibility(View.VISIBLE);
            }
        }

        ;
    };

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 短信注册成功后返回MainActivity,Ȼ����ʾ�º���
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                    // 当验证码正确是跳转到下一个页面
                    Intent intent = new Intent(RegisterActivity.this,
                            RegisterPwd.class);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("number", number);
                    bundle.putString("type",type);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    handlerText.sendEmptyMessage(2);
                    finish();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {// 服务器验证码发送成
                    reminderText();
                    Toast.makeText(getApplicationContext(), "验证码已发送",
                            Toast.LENGTH_SHORT).show();
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {// 返回支持发送验证的国家列表
                    Toast.makeText(getApplicationContext(), "获取国家列表成功",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                if (flag) {
                    btn_getcode.setVisibility(View.VISIBLE);
                    Toast.makeText(RegisterActivity.this, "验证码获取失败",
                            Toast.LENGTH_SHORT).show();
                    et_phone.requestFocus();
                } else {
                    ((Throwable) data).printStackTrace();
                    // int resId = getStringRes(Registered_activity.this,
                    // "smssdk_network_error");
                    Toast.makeText(RegisterActivity.this, "验证码错误",
                            Toast.LENGTH_SHORT).show();
                    // et_code.selectAll();
                    // if (resId > 0) {
                    // Toast.makeText(Registered_activity.this, resId,
                    // Toast.LENGTH_SHORT).show();
                }
            }

        }

    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    private String iPhone;
    private String iCord;
    private int time = 60;
    private boolean flag = true;

}
