package com.mengshitech.colorrun.fragment.show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.ShowAdapter;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.view.MyGridView;
import com.mengshitech.colorrun.view.MyListView;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by kk on 2016/9/6.
 */
public class Show_search extends Fragment {
    View searchView;
    List<ShowEntity> mShowList;
    ShowAdapter mShowAdapter;
    Context context;
    FragmentManager fm;
    Activity mActivity;

    @InjectView(R.id.title_back)
    ImageView titleBack;
    @InjectView(R.id.iv_show_search)
    ImageView ivShowSearch;
    @InjectView(R.id.ed_search_content)
    EditText edSearchContent;
    @InjectView(R.id.gv_show_search)
    MyGridView gvShowSearch;
    @InjectView(R.id.ll_show_search)
    LinearLayout llShowSearch;
    @InjectView(R.id.lv_show_search)
    MyListView lvShowSearch;
    @InjectView(R.id.ll_show_search_nothing)
    LinearLayout llShowSearchNothing;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        context = getActivity();
        mActivity = getActivity();
        fm = getFragmentManager();
        searchView = View.inflate(mActivity, R.layout.show_search, null);

        ButterKnife.inject(this, searchView);
        lvShowSearch.setOnItemClickListener(new ItemClickListener());
        return searchView;
    }

    @OnClick({R.id.title_back, R.id.iv_show_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                fm.popBackStack();
                break;
            case R.id.iv_show_search:
                if (ContentCommon.user_id == null) {
                    Toast.makeText(mActivity, "请先登录...", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(search_runnable).start();
                }
                break;
        }
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

    Runnable search_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "10");
            map.put("search_content", edSearchContent.getText().toString());
            map.put("user_id", ContentCommon.user_id);
            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            search_handler.sendMessage(msg);
        }
    };
    Handler search_handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(mActivity, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int state = JsonTools.getState("state", result);
                    if (state == 1) {
                        mShowList = JsonTools.getShowInfo("datas", result);
                        llShowSearch.setVisibility(View.GONE);
                        llShowSearchNothing.setVisibility(View.GONE);
                        lvShowSearch.setVisibility(View.VISIBLE);
                        mShowAdapter = new ShowAdapter(mShowList.size(), getActivity(), getActivity(), getFragmentManager(), mShowList, lvShowSearch);
                        lvShowSearch.setAdapter(mShowAdapter);
                    } else {
                        llShowSearchNothing.setVisibility(View.VISIBLE);
                        llShowSearch.setVisibility(View.VISIBLE);
                        lvShowSearch.setVisibility(View.GONE);
                        edSearchContent.setText("");
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
