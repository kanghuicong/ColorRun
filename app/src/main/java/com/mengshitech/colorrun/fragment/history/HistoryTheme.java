package com.mengshitech.colorrun.fragment.history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.adapter.HistoryAdapter;
import com.mengshitech.colorrun.bean.HistoryEntity;
import com.mengshitech.colorrun.customcontrols.BottomPullSwipeRefreshLayout;
import com.mengshitech.colorrun.customcontrols.ProgressDialog;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.fragment.lerun.IntoLerunEvent;
import com.mengshitech.colorrun.fragment.me.AboutUsFragment;
import com.mengshitech.colorrun.fragment.me.meFragment;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;
import com.mengshitech.colorrun.view.MyListView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wschenyongyin
 */
@SuppressLint("ValidFragment")
public class HistoryTheme extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private Activity mActivity;
    List<HistoryEntity> mHistoryList;
    MyListView theme_listview;
    FragmentManager mFragmentManagr;
    private ProgressDialog dialog;
    String type;
    int lerun_id;
    BottomPullSwipeRefreshLayout swipeRefreshLayout;

    public HistoryTheme(Activity activity, String type) {
        mActivity = activity;
        this.type = type;
        // TODO Auto-generated constructor stub
        initView();
    }

    View rainbowView;

    @Override
    public View initView() {MainActivity.rgMainBottom.setVisibility(View.VISIBLE);
       if(rainbowView==null){
           rainbowView = View.inflate(mActivity, R.layout.history_theme, null);
           init();
       }
        ViewGroup parent = (ViewGroup) rainbowView.getParent();
        if (parent != null) {
            parent.removeView(rainbowView);
        }


        theme_listview.setOnItemClickListener(new ItemClickListener());
        return rainbowView;
    }

    private void init() {

        theme_listview = (MyListView) rainbowView.findViewById(R.id.history_theme_listview);
        mFragmentManagr = getFragmentManager();
        swipeRefreshLayout = new BottomPullSwipeRefreshLayout(mActivity);
        swipeRefreshLayout = (BottomPullSwipeRefreshLayout) rainbowView.findViewById(R.id.swipe_history);
        swipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.autoRefresh();

    }



    //获取数据的请求
    private String getData() {
        String Path = ContentCommon.PATH;
        Map<String, String> map = new HashMap<String, String>();
        map.put("flag", "historylerun");
        map.put("index", "1");
        map.put("lerun_theme", type);
        String result = HttpUtils.sendHttpClientPost(Path, map, "utf-8");
        return result;
    }



    private final class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            int lerun_id = Integer.parseInt(parent.getItemAtPosition(position).toString());
            Intent intent = new Intent(mActivity,HistoryContent.class);
            Bundle bundle = new Bundle();
            bundle.putInt("lerun_id", lerun_id);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);
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

                Toast.makeText(mActivity, "连接服务器超时", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                try {

                    int state=JsonTools.getState("state",result);
                    if(state==1){
                        List<HistoryEntity> lerunlist = JsonTools.getHistoryInfo("datas", result);
                        theme_listview.setAdapter(new HistoryAdapter(mActivity, lerunlist, theme_listview));
                        swipeRefreshLayout.setRefreshing(false);
                    }else{
                        swipeRefreshLayout.setRefreshing(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        new Thread(getLeRunRunnable).start();
    }
}
