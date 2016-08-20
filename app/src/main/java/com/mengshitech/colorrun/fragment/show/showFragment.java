package com.mengshitech.colorrun.fragment.show;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.adapter.ShowAdapter;
import com.mengshitech.colorrun.bean.CommentEntity;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.customcontrols.AutoSwipeRefreshLayout;
import com.mengshitech.colorrun.customcontrols.BottomPullSwipeRefreshLayout;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.releaseshow.ReleaseShowActivity;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class showFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, BottomPullSwipeRefreshLayout.OnLoadListener {
    View showView;
    ImageView ivShow_CreateShow;
    ShowAdapter mShowAdapter;
    ListView lvShowContent;
    List<ShowEntity> mShowList;
    private static ConnectivityManager connectivityManager;
    FragmentManager fm;
    private Activity mActivity;
    Context context;
    BottomPullSwipeRefreshLayout swipeRefreshLayout;
    int pageSize = 5;
    int currentPage = 1;
    List<ShowEntity> AllShowList = new ArrayList<ShowEntity>();
    LinearLayout no_network;


    @Override
    public View initView() {

        context = getActivity();
        mActivity = getActivity();
        fm = getFragmentManager();
        setRetainInstance(true);

        connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (showView == null) {
            showView = View.inflate(mActivity, R.layout.fragment_show, null);
            findById();
            Log.i("执行了这里","11是的");
        }
        ViewGroup parent = (ViewGroup) showView.getParent();
        if (parent != null) {
            parent.removeView(showView);
            Log.i("执行了这里","222是的");
        }


        lvShowContent.setOnItemClickListener(new ItemClickListener());
        Log.i("执行了这里","333是的");
        return showView;
    }

    // 获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ShowEntity mShowEntity = (ShowEntity) parent.getAdapter().getItem(position);
            Intent intent = new Intent(getActivity(), showDetail.class);
            Bundle bundle = new Bundle();
            bundle.putString("show_id", mShowEntity.getShow_id());
            bundle.putString("comment_userid", mShowEntity.getUser_id());
            bundle.putString("user_name", mShowEntity.getUser_name());
            bundle.putString("show_content", mShowEntity.getShow_content());
            bundle.putString("show_time", mShowEntity.getShow_time());
            bundle.putString("show_comment_num", mShowEntity.getComment_num());
            bundle.putString("show_like_num", mShowEntity.getLike_num());
            bundle.putString("user_header", mShowEntity.getUser_header());
            bundle.putString("show_image", mShowEntity.getShow_image());
            intent.putExtras(bundle);
            getActivity().startActivity(intent);
        }
    }


    private void findById() {
//        initShow();
//        new Thread(runnable).start();
        lvShowContent = (ListView) showView.findViewById(R.id.lvShowContent);
        ivShow_CreateShow = (ImageView) showView.findViewById(R.id.ivShow_CreateShow);
        ivShow_CreateShow.setOnClickListener(this);

        no_network = (LinearLayout) showView.findViewById(R.id.layout_no_network);
        ivShow_CreateShow.setOnClickListener(this);

        swipeRefreshLayout = new BottomPullSwipeRefreshLayout(mActivity);
        swipeRefreshLayout = (BottomPullSwipeRefreshLayout) showView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setOnLoadListener(this);

        if (ContentCommon.INTENT_STATE) {
            no_network.setVisibility(View.GONE);
            swipeRefreshLayout.autoRefresh();
            lvShowContent.setVisibility(View.VISIBLE);
        } else {
            no_network.setVisibility(View.VISIBLE);
            lvShowContent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivShow_CreateShow:

                if (ContentCommon.login_state.equals("1")) {
                    context.startActivity(new Intent(context, ReleaseShowActivity.class));


                } else {
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
                break;
            default:
                break;
        }
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "2");
            map.put("pageSize", pageSize + "");
            map.put("user_id", ContentCommon.user_id);
            map.put("currentPage", currentPage + "");

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
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                try {

                    mShowList = JsonTools.getShowInfo("datas", result);

                    mShowAdapter = new ShowAdapter(mShowList.size(), getActivity(), getActivity(), getFragmentManager(), mShowList, lvShowContent);

                    lvShowContent.setAdapter(mShowAdapter);
                    swipeRefreshLayout.setRefreshing(false);
//                    swipeRefreshLayout.setLoading(false);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        currentPage = 1;
        new Thread(runnable).start();
    }

    @Override
    public void onLoad() {
        currentPage = currentPage + 1;
        new Thread(loadrunnable).start();
    }


    Runnable loadrunnable = new Runnable() {

        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "2");
            map.put("pageSize", pageSize + "");
            map.put("user_id", ContentCommon.user_id);
            map.put("currentPage", currentPage + "");

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            loadhandler.sendMessage(msg);
        }
    };

    Handler loadhandler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            if (result.equals("timeout")) {

                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    int state = JsonTools.getState("state", result);
                    if (state == 1) {
                        List<ShowEntity> list = JsonTools.getShowInfo("datas", result);

                        for (int i = 0; i < list.size(); i++) {
                            mShowList.add(list.get(i));
                        }
                        mShowAdapter.addItem(mShowList.size());
                        mShowAdapter.notifyDataSetChanged();
                        mShowAdapter.notifyDataSetInvalidated();
                        swipeRefreshLayout.setLoading(false);
                    } else {
                        swipeRefreshLayout.setLoading(false);
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };


    //当没有网络连接时将listview隐藏  并动态加入一个提示的TextView


    private BroadcastReceiver Receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            NetworkInfo phoneinfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiinfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if ((phoneinfo.isConnected()) || (wifiinfo.isConnected())) {

                no_network.setVisibility(View.GONE);
//                swipeRefreshLayout.autoRefresh();
                lvShowContent.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    public void onPause() {
        super.onPause();
        if (Receiver != null) {

            try {
                mActivity.unregisterReceiver(Receiver);
            } catch (Exception e) {
                // already unregistered
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(connectivityManager.CONNECTIVITY_ACTION);
        mActivity.registerReceiver(Receiver, filter);
    }
}
