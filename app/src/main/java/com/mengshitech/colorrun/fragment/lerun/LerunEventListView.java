package com.mengshitech.colorrun.fragment.lerun;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;
import org.json.JSONException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kanghuicong on 2016/7/15  16:12.
 */
public class LerunEventListView extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    LeRunEventListviewAdapter adapter;
    View lerunevent;
    TextView title_bar;
    ImageView title_back;
    Context context;
    private Handler mHandler;
    private ListView lerun_listView;
    Timer timer;
    TimerTask task;
    FragmentManager mFragmentManagr;
    private SwipeRefreshLayout mSwipeLayout;

    int count;//用来标记是否有新的数据


    @Override
    public View initView() {
        mFragmentManagr = getFragmentManager();
        context = getActivity();
        lerunevent = View.inflate(getActivity(), R.layout.lerun_event, null);

        MainActivity.rgMainBottom.setVisibility(View.GONE);
        MainBackUtility.MainBack(lerunevent, "活动", getFragmentManager());

        init();
        mSwipeLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
        return lerunevent;
    }

    private void init() {
        lerun_listView = (ListView) lerunevent.findViewById(R.id.lerun_listView);
        mSwipeLayout = (SwipeRefreshLayout)lerunevent.findViewById(R.id.id_swipe_ly);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));//设置刷新的颜色
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
                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    List<LeRunEntity> lerunlist = JsonTools.getLerunInfo("result", result);
                    count=lerunlist.size();
                    lerun_listView.setAdapter(new LeRunEventListviewAdapter(context, lerunlist,mFragmentManagr));
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
        timer.schedule(task, 1000);
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
            if(lerunlist.size()==count){
//                Toast.makeText(context,"没有更多数据",Toast.LENGTH_SHORT).show();
                mSwipeLayout.setRefreshing(false);
            }else{
                lerun_listView.setAdapter(new LeRunEventListviewAdapter(context, lerunlist,mFragmentManagr));
//                Toast.makeText(context,"加载成功",Toast.LENGTH_SHORT).show();
                mSwipeLayout.setRefreshing(false);
            }


        }
    };



}
