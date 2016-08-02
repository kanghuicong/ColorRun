package com.mengshitech.colorrun.adapter;

import android.content.Context;
import android.content.pm.LauncherApps;
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

import java.util.HashMap;
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
    private HashMap<String, Object> map;
    int []tags ={0,0,0};
    CallBack callback;

    public LerunEnrollListViewAdapter(Context context, List<EnrollEntity> mEnrollList, ListView listView, CallBack callback) {
        this.context = context;
        this.mEnrollList = mEnrollList;
        this.listView = listView;
        map = new HashMap<String, Object>();
        this.callback = callback;
    }

    public interface CallBack{
        public void returnInfo(int price);

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

        map.put("" + position, holder.image);

        if (tags[position] == 0) {
            holder.image.setBackgroundResource(R.mipmap.selected_no);
        } else {
            holder.image.setBackgroundResource(R.mipmap.selected_yes);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resetBackGround();
                ((ImageView) (map.get("" + position))).setBackgroundResource(R.mipmap.selected_yes);
                tags[position] = 1;
                EnrollEntity mEnrollEntity = getItem(position);
                callback.returnInfo(mEnrollEntity.getPrice());
                Log.i("1EnrollEntity",mEnrollEntity.getPrice()+"");
            }
            public void resetBackGround() {
                for (int i = 0; i < map.size(); i++) {
                    ((ImageView) (map.get("" + i))).setBackgroundResource(R.mipmap.selected_no);
                    tags[i] = 0;
                }
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
