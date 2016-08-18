package com.mengshitech.colorrun.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
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
public class ShowGridViewAdapter extends BaseAdapter {
    Context context;
    List<String> imagepath = new ArrayList<String>();
    int count;
    DisplayMetrics dm;

    public ShowGridViewAdapter(Context context, List<String> imagepath, int count){
        this.context = context;
        this.imagepath = imagepath;
        this.count = count;
        dm = context.getResources().getDisplayMetrics();
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

        ViewGroup.LayoutParams ps = holder.grid_image.getLayoutParams();
        ps.height = (dm.widthPixels/7*2);
        holder.grid_image.setLayoutParams(ps);

        String image_path = imagepath.get(position);
        Log.i("image_path:",image_path);
        Glide.with(context).load(image_path).transform(new GlideRoundTransform(context)).into(holder.grid_image);

        return convertView;
    }

    class ViewHolder {
        ImageView grid_image;
    }
}
