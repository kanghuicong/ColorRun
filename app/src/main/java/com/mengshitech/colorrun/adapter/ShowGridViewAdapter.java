package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.SpaceImageDetailActivity;
import com.mengshitech.colorrun.fragment.lerun.ShowMap;
import com.mengshitech.colorrun.fragment.show.showDetail;
import com.mengshitech.colorrun.utils.GlideRoundTransform;
import com.mengshitech.colorrun.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghuicong on 2016/7/25  13:41.
 * 515849594@qq.com
 */
public class ShowGridViewAdapter extends BaseAdapter  {
    Context context;
    List<String> imagepath = new ArrayList<String>();
    int count;
    Activity activity;


    public ShowGridViewAdapter(Context context,Activity activity, List<String> imagepath, int count) {
        this.context = context;
        this.imagepath = imagepath;
        this.count = count;
        this.activity=activity;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.show_gridview, null);
            holder = new ViewHolder();
            holder.grid_image = (ImageView) convertView.findViewById(R.id.iv_show_gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final String image_path = imagepath.get(position);
        Log.i("image_path:", image_path);
        Glide.with(context).load(image_path).transform(new GlideRoundTransform(context)).into(holder.grid_image);
        holder.grid_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SpaceImageDetailActivity.class);
                intent.putExtra("image_path", image_path);
                intent.putExtra("position", position);
                int[] location = new int[2];
                holder.grid_image.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);

                intent.putExtra("width", holder.grid_image.getWidth());
                intent.putExtra("height", holder.grid_image.getHeight());
                context.startActivity(intent);
                activity.overridePendingTransition(0, 0);


            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView grid_image;
    }
}
