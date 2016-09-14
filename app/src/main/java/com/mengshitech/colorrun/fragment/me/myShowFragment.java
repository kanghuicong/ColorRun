package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
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
import com.mengshitech.colorrun.adapter.ShowAdapter;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.customcontrols.BottomPullSwipeRefreshLayout;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.fragment.show.showDetail;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class myShowFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    View myshowView;
    ImageView iv_bullet_red;
    ShowAdapter mShowAdapter;
    ListView lv_myshow;
    TextView myshow_text;
    List<ShowEntity> mShowList;
    FragmentManager fm;
    BottomPullSwipeRefreshLayout swipeRefreshLayout;
    private Activity activity;
    List<String> list = new ArrayList<String>();

    @Override
    public View initView() {
        activity=getActivity();
        myshowView = View.inflate(mActivity, R.layout.me_myshow, null);
        MainBackUtility.MainBack_Show(activity,myshowView, "我的show", getFragmentManager());
        findById();
        new Thread(runnable).start();
        lv_myshow.setOnItemClickListener(new ItemClickListener());
        return myshowView;
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

        lv_myshow = (ListView) myshowView.findViewById(R.id.lv_myshow);
        myshow_text = (TextView)myshowView.findViewById(R.id.tv_me_myshow_text);
        swipeRefreshLayout = new BottomPullSwipeRefreshLayout(mActivity);
        swipeRefreshLayout = (BottomPullSwipeRefreshLayout) myshowView.findViewById(R.id.swipe_layout_myshow);
        swipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.autoRefresh();
    }


    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "3");
            map.put("user_id", ContentCommon.user_id);

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
            } else if (result.equals("failuer") || result.equals("empty")) {
                swipeRefreshLayout.setRefreshing(false);

            } else {
                try {
                    mShowList = JsonTools.getShowInfo("result", result);
                    if (mShowList == null || mShowList.equals("")) {
                        myshow_text.setVisibility(View.VISIBLE);
                    }
                    mShowAdapter = new ShowAdapter(mShowList.size(),activity,getActivity(), getFragmentManager(), mShowList, lv_myshow);
                    lv_myshow.setAdapter(mShowAdapter);
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
        new Thread(runnable).start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (RefreshfReceiver != null) {
            try {
                mActivity.unregisterReceiver(RefreshfReceiver);
            } catch (Exception e) {
                // already unregistered
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("refreshShow");
        LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(mActivity);
        broadcastManager.registerReceiver(RefreshfReceiver, filter);
    }

    private BroadcastReceiver RefreshfReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            swipeRefreshLayout.autoRefresh();
        }
    };
}

