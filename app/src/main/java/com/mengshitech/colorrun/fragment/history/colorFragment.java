package com.mengshitech.colorrun.fragment.history;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.mengshitech.colorrun.R;
import android.support.v4.app.FragmentManager;

import com.mengshitech.colorrun.adapter.HistoryAdapter;
import com.mengshitech.colorrun.adapter.LeRunEventListviewAdapter;
import com.mengshitech.colorrun.bean.HistoryEntity;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.JsonTools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * atenklsy
 */
@SuppressLint("ValidFragment")
public class colorFragment extends BaseFragment {
    ListView lvColorFragment;
    List<HistoryEntity> mHistoryList;
    View colorView;
    private Activity mActivity;
    FragmentManager mFragmentManagr;

    public colorFragment(Activity activity) {
        mActivity = activity;
        initView();
    }

    @Override
    public View initView() {
        // 获取数据源
        colorView = View.inflate(mActivity, R.layout.history_color, null);
        // 引入布局
        findById();
        // 初始化控件
        mFragmentManagr=getFragmentManager();
        return colorView;
    }

    private void findById() {
        lvColorFragment = (ListView) colorView
                .findViewById(R.id.lvColorFragment);
        initDatas();
    }

    private void initDatas() {
//        HistoryAdapter mHistoryAdapter = new HistoryAdapter(mActivity,
//                mHistoryList, lvColorFragment);
//        lvColorFragment.setAdapter(mHistoryAdapter);
        new Thread(getLeRunRunnable).start();
    }

    //获取数据的请求
    private String getData() {
        String Path = IPAddress.PATH;
        Map<String, String> map = new HashMap<String, String>();
        map.put("flag", "lerun");
        map.put("index", "0");
        String result = HttpUtils.sendHttpClientPost(Path, map, "utf-8");
        Log.i("获取主题信息:", result);
        return result;
    }


    //获取lerun主题信息的线程
    Runnable getLeRunRunnable = new Runnable() {
        @Override
        public void run() {
            String result = getData();
            Message msg = new Message();
            msg.obj = result;
            LerunInfohandler.sendMessage(msg);

        }
    };
    //获取lerun主题信息的handler
    Handler LerunInfohandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;

            if (result.equals("timeout")) {
                Toast.makeText(mActivity, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    List<HistoryEntity> lerunlist = JsonTools.getHistoryInfo("result", result);
//                    count=lerunlist.size();

                    lvColorFragment.setAdapter(new HistoryAdapter(mActivity,lerunlist,lvColorFragment));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
