package com.mengshitech.colorrun.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.LikeEntity;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.JsonTools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghuicong on 2016/8/10  14:52.
 */
public class ShowDetailGridViewAdapter extends BaseAdapter {
    Context context;
    List<LikeEntity> likelist;
    LikeEntity likeEntity;
    int count;
    DisplayMetrics dm;

    public ShowDetailGridViewAdapter(Context context, List<LikeEntity> likelist, int count){
        this.context = context;
        this.likelist = likelist;
        this.count = count;
        dm = context.getResources().getDisplayMetrics();
        Log.i("点赞头像数",count+"");
        Log.i("点赞头像数likelist",likelist+"");
    }

    @Override
    public int getCount() {
        if (count <=6) {
            return count;
        }else return 6;
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
        likeEntity = likelist.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.show_detail_gridview, null);
            holder = new ViewHolder();
            holder.grid_image= (ImageView) convertView.findViewById(R.id.iv_show_detail_gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewGroup.LayoutParams ps = holder.grid_image.getLayoutParams();
        ps.height = (dm.widthPixels/8);
        ps.width = (dm.widthPixels/8);
        holder.grid_image.setLayoutParams(ps);
        if (position<=6) {
            String header_path = ContentCommon.path + likeEntity.getUser_header();
            Glide.with(context).load(header_path).transform(new GlideCircleTransform(context)).into(holder.grid_image);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView grid_image;
    }
}
