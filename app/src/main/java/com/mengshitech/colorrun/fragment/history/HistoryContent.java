package com.mengshitech.colorrun.fragment.history;

import android.app.Activity;
import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.ShowDetailCommentAdpter;
import com.mengshitech.colorrun.bean.CommentEntity;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.bean.LeRunEvaluateEntity;
import com.mengshitech.colorrun.customcontrols.AutoSwipeRefreshLayout;
import com.mengshitech.colorrun.customcontrols.BottomPullSwipeRefreshLayout;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kanghuicong on 2016/8/9  17:26.
 */
public class HistoryContent extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    View history_content_view;
    TextView history_title, history_time, history_content;
    ImageView history_poster;
    ListView history_listview;
    int lerun_id;
    BottomPullSwipeRefreshLayout pullSwipeRefreshLayout;
    Context context;
    private static Activity mActivity;
    private double AverageStar;
    private RatingBar ratingBar;
    private TextView tv_score,tv_host,tv_peoplenum;
private View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = getActivity();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        history_content_view = View.inflate(getActivity(), R.layout.history_content, null);
        MainBackUtility.MainBack(history_content_view, "详情", getFragmentManager());
        view=View.inflate(getActivity(),R.layout.historycontent_listview_headerview,null);
        GetData();
        FindId();

        return history_content_view;
    }

    private void GetData() {
        lerun_id = getArguments().getInt("lerun_id");
    }

    private void FindId() {
        history_title = (TextView) view.findViewById(R.id.tv_history_content_title);
        history_time = (TextView) view.findViewById(R.id.tv_history_content_time);
        history_content = (TextView) view.findViewById(R.id.tv_history_content);
        history_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        history_poster = (ImageView) view.findViewById(R.id.iv_history_content_poster);
        history_listview = (ListView) history_content_view.findViewById(R.id.lv_history_content);
        ratingBar = (RatingBar) view.findViewById(R.id.rb_history_content_ratingbar);
        tv_score = (TextView) view.findViewById(R.id.tv_score);
        tv_host= (TextView) view.findViewById(R.id.tv_host);
        tv_peoplenum= (TextView) view.findViewById(R.id.tv_peoplenum);

        pullSwipeRefreshLayout = new BottomPullSwipeRefreshLayout(mActivity);
        pullSwipeRefreshLayout = (BottomPullSwipeRefreshLayout) history_content_view.findViewById(R.id.bottomRefesh);
        pullSwipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
        pullSwipeRefreshLayout.setOnRefreshListener(this);
//        pullSwipeRefreshLayout.autoRefresh();
        pullSwipeRefreshLayout.setOnLoadListener(new BottomPullSwipeRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                Log.i("ssssssss", "vvvvvvv");
            }
        });
        new Thread(lerunInfoRunnable).start();
        new Thread(EvaluateRunnable).start();

    }

    Runnable lerunInfoRunnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "historylerun");
            map.put("index", "2");
            map.put("lerun_id", lerun_id + "");
            Log.i("lerun_id1:", lerun_id + "");
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
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Log.i("主题result:", result);
                    LeRunEntity entity = JsonTools.getHistoryLerunDetail("datas", result);
                    Log.i("主题Lerun_title:", entity.getLerun_title());
                    history_title.setText(entity.getLerun_title());
                    history_time.setText(entity.getLerun_time());
                    history_content.setText(entity.getLerun_content());
                    tv_host.setText("承办方："+entity.getLerun_host());
                    tv_peoplenum.setText("参与人数:"+entity.getLerun_maxuser()+"");
//                    history_poster
                    Glide.with(mActivity).load(ContentCommon.path + entity.getLerun_poster()).into(history_poster);
                    DecimalFormat df = new DecimalFormat("######0.0");
                    AverageStar = Double.valueOf(entity.getAverageStar().toString());
                    Log.i("AverageStar", df.format(AverageStar) );
                    if ((int) AverageStar==0){
                        tv_score.setText("5.0分");
                        Log.i("AverageStar", (int) AverageStar+"" );
                        ratingBar.setRating(5);

                    }else{
                        tv_score.setText(df.format(AverageStar) + "分");
                        Log.i("AverageStar", (int) AverageStar+"" );
                        ratingBar.setRating((int) AverageStar);
                    }



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
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    int state=JsonTools.getState("state",result);
                    Log.i("result:", "result");
                    if(state==1){
                        List<CommentEntity> list = JsonTools.getLeRunEvaluate("datas", result);
                        history_listview.addHeaderView(view);
                        ShowDetailCommentAdpter adapter = new ShowDetailCommentAdpter(mActivity, list, history_listview);
                        history_listview.setAdapter(adapter);
                    }else if(state==0){

                        history_listview.addHeaderView(view);
                        ShowDetailCommentAdpter adapter = new ShowDetailCommentAdpter(mActivity);
                        history_listview.setAdapter(adapter);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();


                }
            }
        }
    };


    @Override
    public void onRefresh() {
        Log.i("下拉刷新", "成功");
        pullSwipeRefreshLayout.setRefreshing(false);
    }
}
