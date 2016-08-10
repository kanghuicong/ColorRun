package com.mengshitech.colorrun.fragment.history;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanghuicong on 2016/8/9  17:26.
 */
public class HistoryContent extends Fragment{
    View history_content_view;
    TextView history_title,history_time,history_content;
    ImageView history_poster;
    ListView history_listview;
    int lerun_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        history_content_view = View.inflate(getActivity(), R.layout.history_content, null);
        MainBackUtility.MainBack(history_content_view,"详情",getFragmentManager());
        FindId();
        GetData();
        return history_content_view;
    }

    private void GetData() {
        lerun_id = getArguments().getInt("lerun_id");
    }

    private void FindId() {
        history_title = (TextView)history_content_view.findViewById(R.id.tv_history_content_title);
        history_time = (TextView)history_content_view.findViewById(R.id.tv_history_content_time);
        history_content = (TextView)history_content_view.findViewById(R.id.tv_history_content);
        history_poster = (ImageView)history_content_view.findViewById(R.id.iv_history_content_poster);
        history_listview = (ListView)history_content_view.findViewById(R.id.lv_history_content);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String ,String> map = new HashMap<String, String>();
//            map.put("flag",);
//            map.put("index",);
            map.put("lerun_id",lerun_id+"");
            String result = HttpUtils.sendHttpClientPost(path,map,"utf-8");
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
//                progressDialog.dismiss();
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {

            }
        }
    };
}
