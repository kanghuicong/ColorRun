package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.fragment.lerun.IntoLerunEvent;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * atenklsy
 */
public class LeRunListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    List<LeRunEntity> mLeRunList;
    ListView mListView;
    FragmentManager mFragmentManager;
    private Activity mActivity;
    List<String> list = new ArrayList<String>();
    int pos;
    int lerun_id;
    private android.view.animation.Animation animation;

    public LeRunListViewAdapter(Activity activity, List<LeRunEntity> leRunList, FragmentManager fm,
                                ListView listView) {
        mActivity = activity;
        mLeRunList = leRunList;
        mListView = listView;
        mFragmentManager = fm;
        Log.i("mLeRunList.size():",mLeRunList.size()+"");
    }

    @Override
    public int getCount() {
        return mLeRunList.size();
    }

    @Override
    public LeRunEntity getItem(int position) {
        return mLeRunList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeRunEntity mLeRunEntity = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_lerun_listview,
                    null);
            holder = new ViewHolder();
            holder.ivLeRunBackground = (ImageView) convertView
                    .findViewById(R.id.ivLeRunBackground);
            holder.tvLeRunName = (TextView) convertView
                    .findViewById(R.id.tvLeRunName);
            holder.tvLeRunLocation = (TextView) convertView
                    .findViewById(R.id.tvLeRunLocation);
            holder.tvLeRunTime = (TextView) convertView
                    .findViewById(R.id.tvLeRunTime);
            holder.tvLeRunState = (TextView)convertView.findViewById(R.id.tvLeRunState);
            holder.tvLeRunLike = (TextView)convertView.findViewById(R.id.tvLeRunLike);
            holder.tvLeRunBrowsenum = (TextView)convertView.findViewById(R.id.tvLeRunBrowse);
            holder.tvLeRunShare = (ImageView)convertView.findViewById(R.id.ivLeRunShare) ;
            holder.like_anim = (TextView)convertView.findViewById(R.id.tv_one);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        animation= AnimationUtils.loadAnimation(mActivity,R.anim.lerun_like);
        Glide.with(mActivity).load(ContentCommon.path+mLeRunEntity.getLerun_poster()).into( holder.ivLeRunBackground);
        holder.tvLeRunName.setText(mLeRunEntity.getLerun_title());
        holder.tvLeRunLocation.setText(mLeRunEntity.getLerun_address());
        holder.tvLeRunTime.setText(mLeRunEntity.getLerun_time());
        holder.tvLeRunBrowsenum.setText("浏览量："+mLeRunEntity.getLerun_browsenum());
        holder.tvLeRunLike.setText(mLeRunEntity.getLerun_likenum());
        lerun_id = mLeRunEntity.getLerun_id();

        if (0 == mLeRunEntity.getLerun_state()){
            holder.tvLeRunState.setText("报名中");
            holder.tvLeRunState.setTextColor(mActivity.getResources().getColor(R.color.green));
        }else if(1 == mLeRunEntity.getLerun_state()){
            holder.tvLeRunState.setText("报名截止");
        }else if(2 == mLeRunEntity.getLerun_state()){
            holder.tvLeRunState.setText("活动中");
            holder.tvLeRunState.setTextColor(mActivity.getResources().getColor(R.color.red));
        }else if(3 == mLeRunEntity.getLerun_state()){
            holder.tvLeRunState.setText("活动结束");
            holder.tvLeRunState.setTextColor(mActivity.getResources().getColor(R.color.ViewLine));
        }

        Drawable drawable = mActivity.getResources().getDrawable(R.mipmap.show_heart);
        drawable.setBounds(0, 0, 28, 28);//必须设置图片大小，否则不显示
        holder.tvLeRunLike.setCompoundDrawables(drawable, null, null, null);

        holder.tvLeRunLike.setOnClickListener(new MyAdapterListener(position,holder.tvLeRunLike,holder.like_anim));
        mListView.setOnItemClickListener(this);
        return convertView;
    }
    class MyAdapterListener implements View.OnClickListener {
        private int position;
        private TextView lerun_like;
        private TextView lerun_ainm;
        public MyAdapterListener(int pos,TextView lerunlike,TextView lerunanim) {
            position = pos;
            lerun_like = lerunlike;
            lerun_ainm = lerunanim;
        }
        @Override
        public void onClick(View v) {
            if(v == lerun_like){
                lerun_ainm.setVisibility(View.VISIBLE);
                lerun_ainm.startAnimation(animation);
                new Handler().postDelayed(new Runnable(){
                    public void run() {
                        lerun_ainm.setVisibility(View.GONE);
                    }
                }, 1000);
            }
            pos = position;
            TextView lerun_like = (TextView) v.findViewById(R.id.tvLeRunLike);
            lerun_like.setText(Integer.valueOf(lerun_like.getText().toString()) + 1 + "");
            new Thread(lerun_like_runnable).start();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int clickPosition, long id) {
        LeRunEntity mclickLeRunEntity = getItem(clickPosition);
        Bundle bundle = new Bundle();
        bundle.putInt("lerun_id",mclickLeRunEntity.getLerun_id());
        IntoLerunEvent mIntoLerunEvent = new IntoLerunEvent();
        mIntoLerunEvent.setArguments(bundle);
        Utility.replace2DetailFragment(mFragmentManager, mIntoLerunEvent);
    }

    Runnable lerun_like_runnable = new Runnable() {
        @Override
        public void run() {
            LeRunEntity leRunEntity = mLeRunList.get(pos);
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "lerun");
            map.put("index", "11");
            map.put("lerun_id",leRunEntity.getLerun_id()+"");
            String result = HttpUtils.sendHttpClientPost(path, map, "utf-8");
            try {
                int lerun_like = JsonTools.getState("state", result);
            }catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    class ViewHolder {
        ImageView ivLeRunBackground;
        TextView tvLeRunName;
        TextView tvLeRunLocation;
        TextView tvLeRunTime;
        TextView tvLeRunState;
        TextView tvLeRunBrowsenum;
        TextView tvLeRunLike;
        ImageView tvLeRunShare;
        TextView like_anim;

    }

}
