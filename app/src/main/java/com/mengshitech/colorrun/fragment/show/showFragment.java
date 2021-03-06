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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.adapter.ShowAdapter;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.customcontrols.BottomPullSwipeRefreshLayout;
import com.mengshitech.colorrun.customcontrols.ProgressDialog;
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

public class ShowFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, BottomPullSwipeRefreshLayout.OnLoadListener {
    View showView;
    ImageView ivShow_CreateShow, iv_search,iv_bullet_red;
    ShowAdapter mShowAdapter;
    ListView lvShowContent;
    List<ShowEntity> mShowList;
    private  ConnectivityManager connectivityManager;
    FragmentManager fm;
    private Activity mActivity;
    Context context;
    BottomPullSwipeRefreshLayout swipeRefreshLayout;
    int pageSize = 10;
    int currentPage = 1;
    List<ShowEntity> AllShowList = new ArrayList<ShowEntity>();
    LinearLayout no_network;
    private ProgressDialog progressDialog;

    @Override
    public View initView() {
        context = getActivity();
        mActivity = getActivity();
        fm = getFragmentManager();
        setRetainInstance(true);
        MainActivity.rgMainBottom.setVisibility(View.VISIBLE);
        connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (showView == null) {
            showView = View.inflate(mActivity, R.layout.show_fragment, null);
            showView.setFocusable(true);
            showView.setFocusableInTouchMode(true);
            showView.setOnKeyListener(backlistener);
            findById();
//        }
//        ViewGroup parent = (ViewGroup) showView.getParent();
//        if (parent != null) {
//            parent.removeView(showView);
//        }

        lvShowContent.setOnItemClickListener(new ItemClickListener());
        return showView;
    }

    // 获取点击事件
    private final class ItemClickListener implements AdapterView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ShowEntity mShowEntity = (ShowEntity) parent.getAdapter().getItem(position);



            Intent intent = new Intent(getActivity(), ShowDetail.class);
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

            if (ContentCommon.MyshowStateList != null) {
                for (int i = 0; i < ContentCommon.MyshowStateList.size(); i++) {
                    if (mShowEntity.getShow_id().equals(ContentCommon.MyshowStateList.get(i))) {
                        iv_bullet_red = (ImageView)view.findViewById(R.id.show_bullet_red);
                        iv_bullet_red.setVisibility(View.GONE);
                        ContentCommon.MyshowStateList.remove(i);
                        if (ContentCommon.MyshowStateList.size() == 0) {
                            ContentCommon.MyshowState = "0";
                            ContentCommon.myshowstate = "0";
                            MainActivity.iv_bullet_red.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }

    private void findById() {
//        initShow();
//        new Thread(runnable).start();
        iv_search = (ImageView) showView.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);
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
        
        if (ContentCommon.INTENT_STATE ) {
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
            case R.id.iv_search:
                Utility.replace2DetailFragment(fm, new Show_search());
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
                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                try {
                    mShowList = JsonTools.getShowInfo("datas", result);

                    mShowAdapter = new ShowAdapter(mShowList.size(), getActivity(), getActivity(), getFragmentManager(), mShowList, lvShowContent);

                    lvShowContent.setAdapter(mShowAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        pageSize = pageSize*currentPage;
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

            Log.i("Receiver","true");
            if ((phoneinfo.isConnected()) || (wifiinfo.isConnected())) {

                no_network.setVisibility(View.GONE);
                lvShowContent.setVisibility(View.VISIBLE);
            }
        }
    };

    private BroadcastReceiver RefreshfReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            swipeRefreshLayout.autoRefresh();
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
        if (RefreshfReceiver != null) {
            try {
                mActivity.unregisterReceiver(RefreshfReceiver);
            } catch (Exception e) {

            }
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(connectivityManager.CONNECTIVITY_ACTION);
        mActivity.registerReceiver(Receiver, filter);

        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("refreshShow");
        LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(context);
        broadcastManager.registerReceiver(RefreshfReceiver, filter2);
    }


    //双击退出
    long mPressedTime = 0;
    private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_BACK) {  //表示按返回键 时的操作
                    long mNowTime = System.currentTimeMillis();
                    if ((mNowTime - mPressedTime) > 2000) {// 比较两次按键时间差
                        Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().addToBackStack(null).commit();
                        mPressedTime = mNowTime;
                    }
                }
            }
            return false;
        }
    };
}
