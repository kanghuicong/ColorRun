package com.mengshitech.colorrun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.SearchEntity;

import java.util.List;

/**
 * Created by kk on 2016/9/6.
 */
public class ShowSearchGridAdapter extends BaseAdapter {
    Context context;
    List<SearchEntity> list;
    SearchEntity molder;

    public ShowSearchGridAdapter(Context context,List<SearchEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public SearchEntity getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        molder = list.get(position);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.show_search_girdview, null);
            holder = new ViewHolder();
            holder.grid_text = (TextView)convertView.findViewById(R.id.tv_girdview_search);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.grid_text.setText(molder.getUser_search());

        return convertView;
    }

    class ViewHolder {
        TextView grid_text;
    }
}

