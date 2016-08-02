package com.mengshitech.colorrun.adapter;

import android.content.Context;
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
import com.mengshitech.colorrun.bean.EnrollEntity;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.utils.IPAddress;

import java.util.List;

/**
 * Created by kanghuicong on 2016/8/1  14:22.
 */
public class LerunEnrollListViewAdapter extends BaseAdapter {
    View view;
    Context context;
    List<EnrollEntity> mEnrollList;
    ListView listView;
    Holder holder = null;
               int flag = 0;

    public LerunEnrollListViewAdapter(Context context, List<EnrollEntity> mEnrollList, ListView listView) {
        this.context = context;
        this.mEnrollList = mEnrollList;
        this.listView = listView;
    }

    @Override
    public int getCount() {
        return mEnrollList.size();
    }

    @Override
    public EnrollEntity getItem(int position) {
        return mEnrollList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EnrollEntity mEnrollEntity = getItem(position);

        Log.i("mLeRunList.size():",mEnrollList.size()+"");
        Log.i("mEnrollEntity",mEnrollEntity.getPrice()+"");
        if (convertView == null) {
            view = View.inflate(context, R.layout.lerun_enroll_listview,
                    null);
            holder = new Holder();
            holder.price = (TextView)view.findViewById(R.id.tv_enroll_price);
            holder.image = (ImageView)view.findViewById(R.id.iv_enroll_image);
            holder.equipment = (TextView)view.findViewById(R.id.tv_enroll_equipment);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (Holder) view.getTag();
        }

        holder.equipment.setText(mEnrollEntity.getEnroll_equipment());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                holder.image.setImageResource(R.mipmap.selected_yes);
            }
        });

        if (mEnrollEntity.getPrice()==0){
            holder.price.setText("免费");
        }else {
            holder.price.setText(mEnrollEntity.getPrice() + "");
        }

        return view;
    }

    class Holder {
        TextView price;
        ImageView image;
        TextView equipment;
    }
}
