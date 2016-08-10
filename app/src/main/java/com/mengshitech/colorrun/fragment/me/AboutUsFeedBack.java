package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
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
public class AboutUsFeedBack extends BaseFragment {
    View feedback_view;
    EditText feedback_content,feedback_phone;
    Button feedback_refer;

    @Override
    public View initView() {
        mActivity = getActivity();
        feedback_view = View.inflate(mActivity, R.layout.me_aboutus_feedback, null);
        MainBackUtility.MainBack(feedback_view,"反馈",getFragmentManager());

        FindId();

        feedback_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(runnable).start();
            }
        });

        return feedback_view;

    }

    private void FindId() {
        feedback_content = (EditText)feedback_view.findViewById(R.id.tv_aboutus_feedback_content);
        feedback_phone = (EditText)feedback_view.findViewById(R.id.tv_aboutus_feedback_phone);
        feedback_refer = (Button)feedback_view.findViewById(R.id.bt_aboutus_feedback_refer);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String,String> map = new HashMap<String,String>();
            map.put("user_id",ContentCommon.user_id);
            map.put("",feedback_content.getText().toString());
            map.put("",feedback_phone.getText().toString());

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
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int stype = JsonTools.getState("",result);
                    String data = JsonTools.getDatas(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };

}
