package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.fragment.lerun.IntoLerunEvent;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.Utility;

import java.util.List;
/**
 * atenklsy
 */
public class LeRunListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    List<LeRunEntity> mLeRunList;
    ListView mListView;
    FragmentManager mFragmentManager;
    private Activity mActivity;

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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
       Glide.with(mActivity).load(IPAddress.path+mLeRunEntity.getLerun_poster()).into( holder.ivLeRunBackground);
        holder.tvLeRunName.setText(mLeRunEntity.getLerun_title());
        holder.tvLeRunLocation.setText(mLeRunEntity.getLerun_address());
        holder.tvLeRunTime.setText(mLeRunEntity.getLerun_time());
        mListView.setOnItemClickListener(this);
        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int clickPosition, long id) {
//        lerunDetailFragment mLeRunFragment = new lerunDetailFragment();
        LeRunEntity mclickLeRunEntity = getItem(clickPosition);
        Bundle bundle = new Bundle();
        bundle.putInt("lerun_id",mclickLeRunEntity.getLerun_id());

//        mLeRunFragment.setArguments(bundle);
        IntoLerunEvent mIntoLerunEvent = new IntoLerunEvent();
        mIntoLerunEvent.setArguments(bundle);
        Utility.replace2DetailFragment(mFragmentManager, mIntoLerunEvent);

    }

    class ViewHolder {
        ImageView ivLeRunBackground;
        TextView tvLeRunName;
        TextView tvLeRunLocation;
        TextView tvLeRunTime;
    }

}
