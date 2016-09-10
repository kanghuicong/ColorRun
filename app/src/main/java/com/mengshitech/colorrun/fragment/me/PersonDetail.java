package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by kk on 2016/9/10.
 */
public class PersonDetail extends Activity {

    @InjectView(R.id.iv_person_header)
    ImageView ivPersonHeader;
    @InjectView(R.id.tv_person_name)
    TextView tvPersonName;
    @InjectView(R.id.iv_person_sex)
    ImageView ivPersonSex;
    @InjectView(R.id.tv_person_address)
    TextView tvPersonAddress;
    @InjectView(R.id.tv_person_id)
    TextView tvPersonId;
    @InjectView(R.id.tv_person_height)
    TextView tvPersonHeight;
    @InjectView(R.id.tv_person_weight)
    TextView tvPersonWeight;
    @InjectView(R.id.tv_person_sign)
    TextView tvPersonSign;
    String user_id;
    @InjectView(R.id.bt_person_make_friends)
    Button btPersonMakeFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_detail);
        ButterKnife.inject(this);
        MainBackUtility.MainBackActivity(PersonDetail.this, "个人信息");
        GetData();
    }

    @OnClick(R.id.bt_person_make_friends)
    public void onClick() {
        Toast.makeText(this, "对方拒绝了您的请求", Toast.LENGTH_SHORT).show();
    }

    private void GetData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        user_id = bundle.getString("user_id");
        new Thread(runnable).start();
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "user");
            map.put("user_id", user_id);
            map.put("index", "4");

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(PersonDetail.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
//                progressDialog.dismiss();
                try {
                    UserEntiy userEntiy = JsonTools.getUserInfo("result", result);
                    String header_path = ContentCommon.path + userEntiy.getUser_header();
                    Glide.with(PersonDetail.this).load(header_path).transform(new GlideCircleTransform(PersonDetail.this)).error(R.mipmap.default_avtar).into(ivPersonHeader);
                    tvPersonName.setText(userEntiy.getUser_name());
                    tvPersonId.setText(user_id);
                    CheckData(tvPersonAddress, userEntiy.getUser_address(), "");
                    CheckData(tvPersonHeight, userEntiy.getUser_height(), "cm");
                    CheckData(tvPersonWeight, userEntiy.getUser_weight(), "kg");
                    CheckData(tvPersonSign, userEntiy.getUser_sign(), "");

                    if (userEntiy.getUser_sex().equals("男")) {
                        ivPersonSex.setBackgroundResource(R.mipmap.sex_male);
                    } else if (userEntiy.getUser_sex().equals("女")) {
                        ivPersonSex.setBackgroundResource(R.mipmap.sex_female);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    private void CheckData(TextView tv, String data, String nothing) {
        if (!data.equals("") && data != null) {
            tv.setText(data + nothing);
            tv.setTextColor(this.getResources().getColor(R.color.black));
        } else {
            tv.setText("未填写");
        }
    }
}
