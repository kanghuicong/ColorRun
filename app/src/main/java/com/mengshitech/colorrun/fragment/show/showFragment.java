package com.mengshitech.colorrun.fragment.show;


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

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.adapter.ShowAdapter;
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

    FragmentManager fm;
    private Activity mActivity;
    Context context;
    int entry_number = 3;
    BottomPullSwipeRefreshLayout swipeRefreshLayout;
    int pageSize = 5;
    int currentPage = 1;
    List<ShowEntity> AllShowList = new ArrayList<ShowEntity>();


    @Override
    public View initView() {
        mActivity = getActivity();
        showView = View.inflate(mActivity, R.layout.fragment_show, null);
        findById();
        new Thread(runnable).start();
        lvShowContent.setOnItemClickListener(new ItemClickListener());
        context = getActivity();

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

        swipeRefreshLayout = new BottomPullSwipeRefreshLayout(mActivity);
        swipeRefreshLayout = (BottomPullSwipeRefreshLayout) showView.findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.autoRefresh();
        swipeRefreshLayout.setOnLoadListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivShow_CreateShow:

                if(ContentCommon.login_state.equals("1")){
                    context.startActivity(new Intent(context, ReleaseShowActivity.class));
                }else {

                    Toast.makeText(context,"您还没有登陆哦！",Toast.LENGTH_SHORT).show();
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
                swipeRefreshLayout.setRefreshing(false);
            } else {
                try {

                    mShowList = JsonTools.getShowInfo("datas", result);

                    Log.i("mshowlist", mShowList.toString());
                    mShowAdapter = new ShowAdapter(mShowList.size(), getActivity(), getFragmentManager(), mShowList, lvShowContent);

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
        Log.i("自动执行", "sss");
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
            Log.i("loadresult", result);
            Message msg = new Message();
            msg.obj = result;
            loadhandler.sendMessage(msg);
        }
    };

    Handler loadhandler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            Log.i("loadresult", result);
            if (result.equals("timeout")) {

                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    int state = JsonTools.getState("state", result);
                    if (state == 1) {
                        List<ShowEntity> list = JsonTools.getShowInfo("datas", result);

                        for (int i = 0; i < list.size(); i++) {
                            Log.i("list的循环", list.get(i) + "");
                            mShowList.add(list.get(i));
                            Log.i("list的循环", list.get(i) + "");
                        }
                        Log.i("mshowlist", mShowList.size() + "");
                        mShowAdapter.addItem(mShowList.size());
                        mShowAdapter.notifyDataSetChanged();
                        mShowAdapter.notifyDataSetInvalidated();
                        swipeRefreshLayout.setLoading(false);
                    } else {
                        Log.i("取消加载","sss");
                        swipeRefreshLayout.setLoading(false);
                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

}
