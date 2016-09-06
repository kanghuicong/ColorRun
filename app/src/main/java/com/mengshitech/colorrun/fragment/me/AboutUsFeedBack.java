package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.fragment.lerun.ShowMap;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanghuicong on 2016/8/4  16:10.
 */
public class AboutUsFeedBack extends BaseFragment implements TextWatcher{
    View feedback_view;
    EditText feedback_content,feedback_phone;
    Button feedback_refer;
    private Context context;
    int content_number = 400;
    int max_number = 450;


    @Override
    public View initView() {
        context=getActivity();
        feedback_view = View.inflate(mActivity, R.layout.me_aboutus_feedback, null);
        MainBackUtility.MainBack(feedback_view,"反馈",getFragmentManager());

        FindId();

        feedback_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(feedback_content.getText().toString())) {
                   Toast.makeText(context,"请填写反馈信息...",Toast.LENGTH_SHORT).show();
                }else {
                    if (feedback_content.length() < content_number) {
                        new Thread(runnable).start();
                    }else {
                        Toast.makeText(context, "您的意见太长啦，无法发表...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return feedback_view;
    }

    private void FindId() {
        feedback_content = (EditText)feedback_view.findViewById(R.id.tv_aboutus_feedback_content);
        feedback_content.addTextChangedListener(this);
        feedback_content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(max_number)});
        feedback_phone = (EditText)feedback_view.findViewById(R.id.tv_aboutus_feedback_phone);
        feedback_refer = (Button)feedback_view.findViewById(R.id.bt_aboutus_feedback_refer);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String,String> map = new HashMap<String,String>();
            map.put("flag","aboutus");
            map.put("user_id",ContentCommon.user_id);
            map.put("index","2");
            map.put("feedback_content",feedback_content.getText().toString());
            map.put("user_telphone",feedback_phone.getText().toString());

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
//                progressDialog.dismiss();
                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int stype = JsonTools.getState("state",result);
                    if (stype==1){
                        Toast.makeText(context,"反馈成功！",Toast.LENGTH_SHORT).show();
                    }else if (stype==0){
                        Toast.makeText(context,"反馈失败！",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //监听Editext内容控制按钮变化
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    @Override
    public void afterTextChanged(Editable s) {
        int i = 1;
        if (i==1) {
            if (feedback_content.length() > content_number) {
                Toast.makeText(context, "意见内容太长啦，无法发表...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
