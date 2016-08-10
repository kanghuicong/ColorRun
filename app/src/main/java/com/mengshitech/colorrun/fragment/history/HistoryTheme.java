package com.mengshitech.colorrun.fragment.history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.adapter.HistoryAdapter;
import com.mengshitech.colorrun.bean.HistoryEntity;
import com.mengshitech.colorrun.customcontrols.ProgressDialog;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.fragment.lerun.IntoLerunEvent;
import com.mengshitech.colorrun.fragment.me.AboutUsFragment;
import com.mengshitech.colorrun.fragment.me.meFragment;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * atenklsy
 */
@SuppressLint("ValidFragment")
public class HistoryTheme extends BaseFragment {
    private Activity mActivity;
    List<HistoryEntity> mHistoryList;
    ListView theme_listview;
    FragmentManager mFragmentManagr;
    private ProgressDialog dialog;
    String type;
    int lerun_id;

    public HistoryTheme(Activity activity, String type) {
        mActivity = activity;
        this.type = type;
        // TODO Auto-generated constructor stub
        initView();
    }

    View rainbowView;

    @Override
    public View initView() {
        dialog = ProgressDialog.show(mActivity, "正在加载数据");
//		dialog.show();
        rainbowView = View.inflate(mActivity, R.layout.history_theme, null);
        findById();
        theme_listview.setOnItemClickListener(new ItemClickListener());
        return rainbowView;
    }

    private void findById() {
        theme_listview = (ListView) rainbowView.findViewById(R.id.history_theme_listview);
        mFragmentManagr = getFragmentManager();
        initDatas();
    }

    private void initDatas() {
        new Thread(getLeRunRunnable).start();
    }

    //获取数据的请求
    private String getData() {
        String Path = ContentCommon.PATH;
        Map<String, String> map = new HashMap<String, String>();
        map.put("flag", "lerun");
        map.put("index", "0");
        String result = HttpUtils.sendHttpClientPost(Path, map, "utf-8");
        Log.i("获取主题信息:", result);
        return result;
    }

    private final class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("ItemClickListener", "ItemClickListener");
            HistoryContent historyContent = new HistoryContent();
            Bundle bundle = new Bundle();
            bundle.putInt("lerun_id", 999);
            historyContent.setArguments(bundle);
            Utility.replace2DetailFragment(getParentFragment().getFragmentManager(), historyContent);


        }
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
                dialog.dismiss();
                Toast.makeText(mActivity, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    dialog.dismiss();
                    List<HistoryEntity> lerunlist = JsonTools.getHistoryInfo("result", result);
//                    count=lerunlist.size();
                    theme_listview.setAdapter(new HistoryAdapter(mActivity, lerunlist, theme_listview));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
