package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.fragment.lerun.IntoLerunEvent;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.GlideRoundTransform;
import com.mengshitech.colorrun.utils.Utility;

import java.util.List;

/**
 * Created by atenklsy on 2016/7/15 10:55.
 * E-address:atenk@qq.com.
 */
public class LeRunGridViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    List<LeRunEntity> mLeRunList;
    FragmentManager mFragmentManagr;
    GridView mLeRunGridView;
    Activity mActivity;
    LeRunEntity mLeRunEntity;
    List<LeRunEntity> gideviewlist;//测试用的
    private int count;
    private Boolean flag=true;


    public LeRunGridViewAdapter(Activity activity, List<LeRunEntity> gideviewlist, FragmentManager fm, GridView gridView) {
        mActivity = activity;
        this.gideviewlist = gideviewlist;
        mFragmentManagr = fm;
        mLeRunGridView = gridView;

//        Toast.makeText(mActivity,"gideviewlist:"+gideviewlist.size()+"",Toast.LENGTH_SHORT).show();
        if(gideviewlist==null){
            count=2;
            flag=false;
        }else {
            count=gideviewlist.size();
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


           if (convertView == null) {
               convertView = View.inflate(mActivity, R.layout.item_lerun_gridview, null);
               holder = new ViewHolder();
               holder.ivBackground = (ImageView) convertView.findViewById(R.id.ivBackground);
               convertView.setTag(holder);
           } else {
               holder = (ViewHolder) convertView.getTag();
           }
        if (flag){
           LeRunEntity entity=gideviewlist.get(position);
           Glide.with(mActivity).load(ContentCommon.path+entity.getLerun_poster()).into(holder.ivBackground);
           mLeRunGridView.setOnItemClickListener(this);
       }else {
//            holder.ivBackground.setImageResource(R.mipmap.defaut_error_square);
            Glide.with(mActivity).load(R.mipmap.defaut_error_long).transform(new GlideRoundTransform(mActivity)).into(holder.ivBackground);
       }
    return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int clickPosition, long id) {
        LeRunEntity mclickLeRunEntity = gideviewlist.get(clickPosition);

        Bundle bundle = new Bundle();

        bundle.putInt("lerun_id", mclickLeRunEntity.getLerun_id());
        IntoLerunEvent mIntoLerunEvent = new IntoLerunEvent();
        mIntoLerunEvent.setArguments(bundle);
        Utility.replace2DetailFragment(mFragmentManagr, mIntoLerunEvent);
    }

    class ViewHolder {

        ImageView ivBackground;

    }
}
