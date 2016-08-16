package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.ShowAdapter;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.customcontrols.BottomPullSwipeRefreshLayout;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.fragment.show.CreateShowFragment;
import com.mengshitech.colorrun.fragment.show.showDetail;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * atenklsy
 */
public class myShowFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{
    View myshowView;
    ShowAdapter mShowAdapter;
    ListView lv_myshow;
    List<ShowEntity> mShowList;
    FragmentManager fm;
    BottomPullSwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View initView() {
        myshowView = View.inflate(mActivity, R.layout.me_myshow, null);
        MainBackUtility.MainBack(myshowView, "我的show", getFragmentManager());
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
        }
    }


    private void findById() {

        lv_myshow = (ListView) myshowView.findViewById(R.id.lv_myshow);
        swipeRefreshLayout = new BottomPullSwipeRefreshLayout(mActivity);
        swipeRefreshLayout = (BottomPullSwipeRefreshLayout) myshowView.findViewById(R.id.swipe_layout_myshow);
        swipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivShow_CreateShow:
                CreateShowFragment mCreateShowFragment = new CreateShowFragment();
                Utility.replace2DetailFragment(fm, mCreateShowFragment);
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
            map.put("index", "3");
            map.put("user_id", ContentCommon.user_id);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Log.i("result", result);
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            Log.i("result111", result);
            if (result.equals("timeout")) {
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    mShowList = JsonTools.getShowInfo("result", result);

                    Log.i("mshowlist", mShowList.toString());
                    mShowAdapter = new ShowAdapter(mShowList.size(), getActivity(), getFragmentManager(), mShowList, lv_myshow);
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
}

