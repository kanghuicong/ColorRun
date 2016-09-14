package com.mengshitech.colorrun.fragment.show;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.ShowAdapter;
import com.mengshitech.colorrun.adapter.ShowSearchGridAdapter;
import com.mengshitech.colorrun.bean.SearchEntity;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.customcontrols.ProgressDialog;
import com.mengshitech.colorrun.dao.SearchDao;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by kk on 2016/9/6.
 */
public class Show_search extends Fragment implements TextWatcher {
    View searchView;
    List<ShowEntity> mShowList;
    ShowAdapter mShowAdapter;
    Context context;
    String search_content;
    FragmentManager fm;
    Activity mActivity;
    List<String> list;
    InputMethodManager imm;
    List<Map<String, String>> mlist;

    @InjectView(R.id.title_back)
    ImageView titleBack;
    @InjectView(R.id.ed_search_content)
    EditText edSearchContent;
    @InjectView(R.id.gv_show_search)
    GridView gvShowSearch;
    @InjectView(R.id.ll_show_search)
    LinearLayout llShowSearch;
    @InjectView(R.id.lv_show_search)
    ListView lvShowSearch;
    @InjectView(R.id.ll_show_search_nothing)
    LinearLayout llShowSearchNothing;
    @InjectView(R.id.bt_show_search)
    Button btShowSearch;
    @InjectView(R.id.lv_show_search_history)
    ListView lvShowSearchHistory;
    @InjectView(R.id.ll_show_search_history)
    LinearLayout llShowSearchHistory;

    ExecutorService signleThreadPool;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        context = getActivity();
        mActivity = getActivity();
        fm = getFragmentManager();
        searchView = View.inflate(mActivity, R.layout.show_search, null);
        ButterKnife.inject(this, searchView);
        signleThreadPool= Executors.newSingleThreadExecutor();
        progressDialog=ProgressDialog.show(context,"正在加载");
        edSearchContent.addTextChangedListener(this);
        lvShowSearch.setOnItemClickListener(new ItemClickListener());
        gvShowSearch.setOnItemClickListener(new MySearchHotItemLongClickListener());
        signleThreadPool.execute(hotsearch_runnable);
        GetSearchHistory();
        GetClick();
        return searchView;
    }

    private void GetSearchHistory() {
        llShowSearchHistory.setVisibility(View.GONE);
        if (ContentCommon.user_id !=null) {
            List<SearchEntity> search_list = new ArrayList<SearchEntity>();
            mlist = new ArrayList<Map<String, String>>();
            SearchDao dao = new SearchDao(context);
            search_list = dao.find(ContentCommon.user_id);
            Log.i("llShowSearchHistory",search_list+"1111111");
            if (search_list.size()!=0) {
                llShowSearchHistory.setVisibility(View.VISIBLE);
                Log.i("llShowSearchHistory","llShowSearchHistory");
                for (int i = 0; i < search_list.size(); i++) {
                    SearchEntity modler = search_list.get(i);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("content", modler.getUser_search());
                    mlist.add(map);
                }
                SimpleAdapter adapter = new SimpleAdapter(context, mlist,
                        R.layout.show_search_history_listview, new String[]{"content"},
                        new int[]{R.id.tv_show_search_history});
                lvShowSearchHistory.setAdapter(adapter);
                lvShowSearchHistory.setOnItemClickListener(new MySearchHistoryItemLongClickListener());
            }
        }
    }

    //自动弹出键盘
    private void GetClick() {

        // 获取编辑框焦点
        edSearchContent.setFocusable(true);
        edSearchContent.setFocusableInTouchMode(true);
        edSearchContent.requestFocus();
        //打开软键盘
        imm = (InputMethodManager) edSearchContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnClick({R.id.title_back, R.id.bt_show_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                imm.hideSoftInputFromWindow(edSearchContent.getWindowToken(), 0);
                fm.popBackStack();
                break;
            case R.id.bt_show_search:
                search_content = edSearchContent.getText().toString();
                progressDialog.show();
                signleThreadPool.execute(search_runnable);
                if (ContentCommon.user_id != null) {
                    SearchDao dao = new SearchDao(context);
                    dao.add(ContentCommon.user_id, search_content);
                }
                break;
        }
    }

    //热门搜索点击事件
    private class MySearchHotItemLongClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            progressDialog.show();
            SearchEntity mSearchEntity = (SearchEntity) parent.getAdapter().getItem(position);
            search_content = mSearchEntity.getUser_search();
            signleThreadPool.execute(search_runnable);
        }
    }

    // 获取search点击事件
    private  class ItemClickListener implements AdapterView.OnItemClickListener {
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
        }
    }

    //历史搜索点击事件
    public class MySearchHistoryItemLongClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            progressDialog.show();
            Map<String, String> map = mlist.get(position);
            search_content = map.get("content");
            signleThreadPool.execute(search_runnable);
        }
    }


    //搜索查询
    Runnable search_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "10");
            map.put("search_content", search_content);
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
            progressDialog.dismiss();
            if (result.equals("timeout")) {

                Toast.makeText(mActivity, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int state = JsonTools.getState("state", result);
                    if (state == 1) {
                        mShowList = JsonTools.getShowInfo("datas", result);
                        llShowSearchHistory.setVisibility(View.GONE);
                        llShowSearchNothing.setVisibility(View.GONE);
                        llShowSearch.setVisibility(View.GONE);
                        lvShowSearch.setVisibility(View.VISIBLE);
                        mShowAdapter = new ShowAdapter(mShowList.size(), getActivity(), getActivity(), getFragmentManager(), mShowList, lvShowSearch);
                        lvShowSearch.setAdapter(mShowAdapter);
                    } else {
                        llShowSearchNothing.setVisibility(View.VISIBLE);
                        llShowSearchHistory.setVisibility(View.GONE);
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

    //热门搜索
    Runnable hotsearch_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "hotsearch");
            map.put("index", "2");
            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            hotsearch_handler.sendMessage(msg);

        }
    };

    Handler hotsearch_handler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            try{
                List<SearchEntity> list = JsonTools.getHotSearch(result);
                ShowSearchGridAdapter adapter = new ShowSearchGridAdapter(context, list);
                gvShowSearch.setAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    //监听Edittext内容变化
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(edSearchContent.getText().toString())) {
            btShowSearch.setVisibility(View.VISIBLE);
        } else {
            btShowSearch.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
