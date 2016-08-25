package com.mengshitech.colorrun.fragment.history;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.adapter.ShowDetailCommentAdpter;
import com.mengshitech.colorrun.bean.CommentEntity;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.bean.LeRunEvaluateEntity;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.customcontrols.AutoSwipeRefreshLayout;
import com.mengshitech.colorrun.customcontrols.BottomPullSwipeRefreshLayout;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.DateUtils;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * wschenyongyin
 */
public class HistoryContent extends Activity implements SwipeRefreshLayout.OnRefreshListener, BottomPullSwipeRefreshLayout.OnLoadListener, View.OnClickListener {
    private TextView history_title, history_time,history_content;
    private ImageView history_poster;
    private ListView history_listview;
    private View view;
    int lerun_id;
    private BottomPullSwipeRefreshLayout pullSwipeRefreshLayout;
    private double AverageStar;
    private RatingBar ratingBar;
    private TextView tv_score, tv_host, tv_peoplenum;
    private int pageSize = 10;
    private int currentPage = 1;
    private boolean onload = false;
    private boolean onRefresh = false;
    private List<CommentEntity> list;
    private ShowDetailCommentAdpter adapter;
    private String evaluate_content;
    private Button btn_send;
    private EditText et_content;
    private LinearLayout footview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_content);
        MainBackUtility.MainBackActivity(HistoryContent.this, "详情");
        view = View.inflate(HistoryContent.this, R.layout.historycontent_listview_headerview, null);
        GetData();
        FindId();
    }

    private void GetData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        lerun_id = bundle.getInt("lerun_id");
        Log.i("lerun_id",lerun_id+"");
    }

    private void FindId() {
        history_title = (TextView) view.findViewById(R.id.tv_history_content_title);
        history_time = (TextView) view.findViewById(R.id.tv_history_content_time);
        history_content = (TextView) view.findViewById(R.id.tv_history_content);
        history_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        history_poster = (ImageView) view.findViewById(R.id.iv_history_content_poster);
        history_listview = (ListView) findViewById(R.id.lv_history_content);
        ratingBar = (RatingBar) view.findViewById(R.id.rb_history_content_ratingbar);
        tv_score = (TextView) view.findViewById(R.id.tv_score);
        tv_host = (TextView) view.findViewById(R.id.tv_host);
        tv_peoplenum = (TextView) view.findViewById(R.id.tv_peoplenum);
        btn_send = (Button) findViewById(R.id.btn_send);
        et_content = (EditText) findViewById(R.id.et_content);
        footview = (LinearLayout) findViewById(R.id.footview);
        footview.setVisibility(View.GONE);

        new Thread(lerunInfoRunnable).start();
        new Thread(EvaluateRunnable).start();
        pullSwipeRefreshLayout = new BottomPullSwipeRefreshLayout(HistoryContent.this);
        pullSwipeRefreshLayout = (BottomPullSwipeRefreshLayout)findViewById(R.id.bottomRefesh);
        pullSwipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
//        pullSwipeRefreshLayout.autoRefresh();
        pullSwipeRefreshLayout.setOnRefreshListener(this);
        pullSwipeRefreshLayout.setOnLoadListener(this);
        btn_send.setOnClickListener(this);
    }

    Runnable lerunInfoRunnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "historylerun");
            map.put("index", "2");
            map.put("lerun_id", lerun_id + "");
            String result = HttpUtils.sendHttpClientPost(path, map, "utf-8");
            Message msg = new Message();
            msg.obj = result;
            lerunInfohandler.sendMessage(msg);
        }
    };

    Handler lerunInfohandler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(HistoryContent.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    LeRunEntity entity = JsonTools.getHistoryLerunDetail("datas", result);
                    Log.i("getHistoryLerunDetail",entity.getLerun_title());
                    history_title.setText(entity.getLerun_title());
                    history_time.setText(entity.getLerun_time());
                    history_content.setText(entity.getLerun_content());
                    tv_host.setText("承办方：" + entity.getLerun_host());
                    tv_peoplenum.setText("参与人数:" + entity.getLerun_maxuser() + "");
                    Glide.with(HistoryContent.this).load(ContentCommon.path + entity.getLerun_poster()).into(history_poster);
                    DecimalFormat df = new DecimalFormat("######0.0");
                    AverageStar = Double.valueOf(entity.getAverageStar().toString());
                    if ((int) AverageStar == 0) {
                        tv_score.setText("5.0分");
                        ratingBar.setRating(5);

                    } else {
                        tv_score.setText(df.format(AverageStar) + "分");
                        ratingBar.setRating((int) AverageStar);
                    }
                    footview.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    Runnable EvaluateRunnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "historylerun");
            map.put("index", "3");
            map.put("lerun_id", lerun_id + "");
            map.put("pageSize", pageSize + "");
            map.put("currentPage", currentPage + "");
            String result = HttpUtils.sendHttpClientPost(path, map, "utf-8");
            Message msg = new Message();
            msg.obj = result;
            Evaluatehandler.sendMessage(msg);
        }
    };

    Handler Evaluatehandler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(HistoryContent.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
                pullSwipeRefreshLayout.setRefreshing(false);
            } else {
                try {
                //上拉加载刷新listview
                    if (onload) {

                        int state = JsonTools.getState("state", result);
                        if (state == 1) {
                            List<CommentEntity> mlist = JsonTools.getLeRunEvaluate("datas", result);

                            for (int i = 0; i < mlist.size(); i++) {
                                list.add(mlist.get(i));
                            }
                            adapter.changeCount(list.size());
                            adapter.notifyDataSetChanged();
                            adapter.notifyDataSetInvalidated();
                            pullSwipeRefreshLayout.setLoading(false);

                        } else if (state == 0) {

                            pullSwipeRefreshLayout.setLoading(false);
                        }

                        onload = false;
                    }
                    //下拉刷新后刷新listview
                    else if (onRefresh) {

                        int state = JsonTools.getState("state", result);
                        if (state == 1) {
                            list.clear();
                            List<CommentEntity> mlist = JsonTools.getLeRunEvaluate("datas", result);

                            for (int i = 0; i < mlist.size(); i++) {
                                list.add(mlist.get(i));
                            }
                            adapter.changeCount(list.size());
                            adapter.notifyDataSetChanged();
                            adapter.notifyDataSetInvalidated();
                            pullSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            pullSwipeRefreshLayout.setRefreshing(false);
                        }
                    } else
                    //进入页面进行listview数据加载
                    {
                        int state = JsonTools.getState("state", result);
                        if (state == 1) {
                            list = JsonTools.getLeRunEvaluate("datas", result);
                            history_listview.addHeaderView(view);
                            adapter = new ShowDetailCommentAdpter(HistoryContent.this, list,history_listview);
                            history_listview.setAdapter(adapter);
                            pullSwipeRefreshLayout.setRefreshing(false);
                        } else if (state == 0) {
                            history_listview.addHeaderView(view);
                            list = new ArrayList<CommentEntity>();
                            adapter = new ShowDetailCommentAdpter(HistoryContent.this,list,history_listview);
                            history_listview.setAdapter(adapter);
                            pullSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //下拉刷新
    @Override
    public void onRefresh() {
        onRefresh = true;
        currentPage = 1;

        new Thread(EvaluateRunnable).start();
    }

    //上拉加载
    @Override
    public void onLoad() {

        currentPage = currentPage + 1;
        onload = true;
        new Thread(EvaluateRunnable).start();
    }

    @Override
    public void onClick(View v) {
        if (ContentCommon.login_state.equals("1")){
            if ("".equals(et_content.getText().toString())) {
                Toast.makeText(HistoryContent.this, "请输入评论内容...", Toast.LENGTH_SHORT).show();
            }else {
                evaluate_content = et_content.getText().toString();
                new Thread(evaluateRunnable).start();}
        } else{
            startActivity(new Intent(HistoryContent.this, LoginActivity.class));
            Toast.makeText(HistoryContent.this,"请先登录...",Toast.LENGTH_SHORT).show();
        }
    }


    //执行评论的线程
    Runnable evaluateRunnable = new Runnable() {
        @Override
        public void run() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "historylerun");
            map.put("index", "4");
            map.put("evaluate_content", evaluate_content);
            map.put("lerun_id", lerun_id + "");
            map.put("user_id", ContentCommon.user_id);
            String result = HttpUtils.sendHttpClientPost(ContentCommon.PATH, map, "utf-8");

            Message msg = new Message();
            msg.obj = result;
            evaluateHandler.sendMessage(msg);

        }
    };
    Handler evaluateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            try {
                int state = JsonTools.getState("state", result);
                if (state == 1) {
                    UserDao dao = new UserDao(HistoryContent.this);
                    UserEntiy modler =  dao.find(ContentCommon.user_id);

                    String time_now = DateUtils.getCurrentDate();
                    CommentEntity info = new CommentEntity();
                    info.setUser_name(modler.getUser_name());
                    info.setUser_header(modler.getUser_header());
                    info.setComment_time(time_now);
                    info.setComment_content(et_content.getText().toString());
                    list.add(0,info);
                    adapter.changeCount(list.size());
                    adapter.notifyDataSetChanged();

                    Toast.makeText(HistoryContent.this, "发表成功", Toast.LENGTH_SHORT).show();
                    et_content.setText("");

//                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };
}
