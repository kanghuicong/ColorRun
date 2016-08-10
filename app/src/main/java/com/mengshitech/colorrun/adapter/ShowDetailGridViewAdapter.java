package com.mengshitech.colorrun.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.LikeEntity;
import com.mengshitech.colorrun.utils.ContentCommon;
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
    List<String> ImageList;
    List<String> imagepath = new ArrayList<String>();


    public ShowDetailGridViewAdapter(Context context, List<LikeEntity> likelist, int count){
        this.context = context;
        this.likelist = likelist;
        this.count = count;
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
        likeEntity = likelist.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.show_detail_gridview, null);
            holder = new ViewHolder();
            holder.grid_image= (ImageView) convertView.findViewById(R.id.iv_show_detail_gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String header_path =    ContentCommon.path+likeEntity.getUser_header();

        Log.i("header_path:",header_path);
        Glide.with(context).load(header_path).into(holder.grid_image);

        return convertView;
    }

    class ViewHolder {
        ImageView grid_image;
    }
}
