package com.mengshitech.colorrun.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.ImageEntity;
import com.mengshitech.colorrun.bean.OrderEntity;
import com.mengshitech.colorrun.fragment.me.MyLeRunFragmentInTo;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kanghuicong on 2016/7/21  9:16.
 * 515849594@qq.com
 */
public class MyLerunListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    View view;
    int count;
    private static Map<Integer, View> map = new HashMap<Integer, View>();
    Context context;
    ImageEntity Ibm;
    List<OrderEntity> list;
    ListView mylerun_listview;
    int lerun_id;
    String user_id;
    FragmentManager mFragmentManager;

    public MyLerunListViewAdapter(Context context) {
        this.context = context;
    }

    public MyLerunListViewAdapter(int count, Context context, List<OrderEntity> list, ListView mylerun_listview,FragmentManager mFragmentManager) {
        super();
        this.count = count;
        this.context = context;
        this.list = list;
        this.mylerun_listview = mylerun_listview;
        this.mFragmentManager=mFragmentManager;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        OrderEntity info = list.get(position);
        return info.getLerun_id();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderEntity info = list.get(position);

        Holder holder = null;
        if (convertView == null) {
            view = View.inflate(context, R.layout.me_mylerun_listview,
                    null);
            holder = new Holder();
            holder.mylerun_poster = (ImageView) view.findViewById(R.id.iv_mylerun_poster);
            holder.mylerun_name = (TextView) view.findViewById(R.id.tv_mylerun_title);
            holder.mylerun_state = (TextView) view.findViewById(R.id.lerun_state);

            holder.mylerun_time = (TextView) view.findViewById(R.id.tv_mylerun_time);
            holder.mylerun_address = (TextView) view.findViewById(R.id.tv_mylerun_location);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (Holder) view.getTag();
        }
        Glide.with(context).load(ContentCommon.path + info.getLerun_poster()).error(R.mipmap.defaut_error_long).into(holder.mylerun_poster);
        holder.mylerun_name.setText(info.getLerun_title());
        holder.mylerun_time.setText(info.getLerun_time());
        holder.mylerun_address.setText(info.getLerun_address());
        switch (info.getLerun_state()) {
            case 0:
                holder.mylerun_state.setText("报名中");
                holder.mylerun_state.setTextColor(context.getResources().getColor(R.color.green));
                break;
            case 1:
                holder.mylerun_state.setText("报名截止");
                break;
            case 2:
                holder.mylerun_state.setText("活动进行中");
                holder.mylerun_state.setTextColor(context.getResources().getColor(R.color.red));
                break;
            case 3:
                holder.mylerun_state.setText("活动结束");
                holder.mylerun_state.setTextColor(context.getResources().getColor(R.color.orange));
                break;
            default:
                break;
        }



        mylerun_listview.setOnItemClickListener(this);


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OrderEntity info = list.get(position);
        lerun_id = Integer.parseInt(info.getLerun_id());
        Bundle bundle = new Bundle();
        bundle.putInt("lerun_id",lerun_id);
        MyLeRunFragmentInTo myLeRunFragmentInTo = new MyLeRunFragmentInTo();
        myLeRunFragmentInTo.setArguments(bundle);
        Utility.replace2DetailFragment(mFragmentManager, myLeRunFragmentInTo);
    }


    class Holder {
        ImageView mylerun_poster;
        TextView mylerun_state;
        TextView mylerun_name;
        TextView mylerun_type;
        TextView mylerun_time;
        TextView mylerun_address;
    }
}
