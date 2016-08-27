package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.MyLerunListViewAdapter;
import com.mengshitech.colorrun.bean.ImageEntity;
import com.mengshitech.colorrun.bean.OrderEntity;
import com.mengshitech.colorrun.customcontrols.AutoSwipeRefreshLayout;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：wschenyongyin on 2016/8/2 19:11
 * 说明: 我的乐跑
 */
public class myLeRunFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private int entry_number = 3;
    View mLeRunView;
    List<OrderEntity> order_list;
    final List<ImageEntity> bmlist = new ArrayList<ImageEntity>();

    private ListView mylerun_listview;
    private MyLerunListViewAdapter adapter;
    private String userid;
    private Context context;
    private AutoSwipeRefreshLayout autoSwipeRefreshLayout;
    FragmentManager mFragmentManager;
    private Activity activity;


    @Override
    public View initView() {

        activity = getActivity();
        context = getActivity();
        mFragmentManager = getFragmentManager();
        if (mLeRunView == null) {
            mLeRunView = View.inflate(mActivity, R.layout.me_mylerun, null);
            find();
        }
        ViewGroup parent = (ViewGroup) mLeRunView.getParent();
        if (parent != null) {
            parent.removeView(mLeRunView);
        }
        MainActivity.rgMainBottom.setVisibility(View.GONE);
        MainBackUtility.MainBack(mLeRunView, "我的乐跑", mFragmentManager);

        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("user_type", Activity.MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", "");

        return mLeRunView;
    }

    private void find() {
        autoSwipeRefreshLayout = new AutoSwipeRefreshLayout(mActivity);
        autoSwipeRefreshLayout = (AutoSwipeRefreshLayout) mLeRunView.findViewById(R.id.id_swipe_ly);
        autoSwipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
        autoSwipeRefreshLayout.setOnRefreshListener(this);
        autoSwipeRefreshLayout.autoRefresh();
        mylerun_listview = (ListView) mLeRunView.findViewById(R.id.lv_me_mylerun);
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "lerun");
            map.put("user_id", userid);
            map.put("index", "6");

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
                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
                autoSwipeRefreshLayout.setRefreshing(false);
            } else {
                try {
                    autoSwipeRefreshLayout.setRefreshing(false);
                    order_list = JsonTools.getOrderInfo("result", result);
                    adapter = new MyLerunListViewAdapter(entry_number, getActivity(), order_list, mylerun_listview, mFragmentManager);
                    mylerun_listview.setAdapter(adapter);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        new Thread(runnable).start();
    }

    
}
