package com.mengshitech.colorrun.fragment.lerun;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.LeRunEventListviewAdapter;
import com.mengshitech.colorrun.adapter.LeRunGridViewAdapter;
import com.mengshitech.colorrun.adapter.LeRunListViewAdapter;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.customcontrols.XListView;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kanghuicong on 2016/7/15  16:12.
 */
public class LerunEventListView extends BaseFragment implements AdapterView.OnItemClickListener, XListView.IXListViewListener {

    LeRunEventListviewAdapter adapter;
    View lerunevent;
    TextView title_bar;
    ImageView title_back;
    Context context;
    private Handler mHandler;
    private XListView lerun_listView;
    Timer timer;
    TimerTask task;


    @Override
    public View initView() {

        context = getActivity();
        lerunevent = View.inflate(getActivity(), R.layout.lerun_event, null);
        MainActivity.rgMainBottom.setVisibility(View.GONE);
        MainBackUtility.MainBack(lerunevent, "活动", getFragmentManager());

        init();
        return lerunevent;
    }

    private void init() {
        lerun_listView = (XListView) lerunevent.findViewById(R.id.lerun_listView);
        new Thread(getLeRunRunnable).start();
        lerun_listView.setXListViewListener(this);
        ;
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
                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    List<LeRunEntity> lerunlist = JsonTools.getLerunInfo("result", result);
                    lerun_listView.setAdapter(new LeRunEventListviewAdapter(context, lerunlist));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //下拉刷新
    @Override
    public void onRefresh() {
        timer = new Timer();
        if (task != null) {
            task.cancel();
        }
        task = new TimerTask() {
            public void run() {
                new Thread(RefreshRunnable).start();
            }
        };
        timer.schedule(task, 2000);
    }

    //下拉刷新执行的线程
    Runnable RefreshRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i("onLoad1", "执行了");
            String result = getData();
            if (!result.equals("timeout")) {
                List<LeRunEntity> lerunlist = null;
                try {
                    lerunlist = JsonTools.getLerunInfo("result", result);
                    Log.i("刷新线程得到的数据", lerunlist.size() + "");

                    Message msg = new Message();
                    msg.obj = lerunlist;
                    RefreshHandler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    };
    //下拉刷新执行的handler
    Handler RefreshHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<LeRunEntity> lerunlist = (List<LeRunEntity>) msg.obj;
            lerun_listView.setAdapter(new LeRunEventListviewAdapter(context, lerunlist));
            onLoad();
        }
    };


    //上拉加载
    @Override
    public void onLoadMore() {

        Toast.makeText(context, "没有更多内容了", Toast.LENGTH_SHORT).show();

    }

    private void onLoad() {
        Log.i("onLoad2", "执行了");
        lerun_listView.stopRefresh();
        lerun_listView.stopLoadMore();
        lerun_listView.setRefreshTime("刚刚刷新");

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int lerun_id = (int) parent.getItemIdAtPosition(position);

    }
}
