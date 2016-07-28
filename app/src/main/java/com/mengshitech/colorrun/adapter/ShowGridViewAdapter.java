package com.mengshitech.colorrun.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghuicong on 2016/7/25  13:41.
 * 515849594@qq.com
 */
public class ShowGridViewAdapter extends BaseAdapter {
    Context context;
    List<String> imagepath = new ArrayList<String>();
    int count;


    public ShowGridViewAdapter(Context context,List<String> imagepath,int count){
        this.context = context;
        this.imagepath = imagepath;
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

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.show_gridview, null);
            holder = new ViewHolder();
            holder.grid_image= (ImageView) convertView.findViewById(R.id.iv_show_gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String image_path = imagepath.get(position);
        Log.i("header_path:",image_path);

        Glide.with(context).load(image_path).into(holder.grid_image);
        return convertView;
    }

    class ViewHolder {
        ImageView grid_image;
    }
}
