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
import com.mengshitech.colorrun.bean.QrcodeBean;
import com.mengshitech.colorrun.fragment.me.MyLeRunFragmentInTo;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.Utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kanghuicong on 2016/7/21  9:16.
 * 515849594@qq.com
 */
public class MyLerunInToListViewAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
    View view;
    int count;
    private static Map<Integer, View> map = new HashMap<Integer, View>();
    Context context;
    ImageEntity Ibm;
    List<QrcodeBean> list;
    ListView mylerun_listview;
    int lerun_id;
    String user_id;
    FragmentManager mFragmentManager;

    public MyLerunInToListViewAdapter(Context context) {
        this.context = context;
    }

    public MyLerunInToListViewAdapter( Context context, List<QrcodeBean> list, ListView mylerun_listview, FragmentManager mFragmentManager) {
        super();

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
        QrcodeBean info = list.get(position);
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QrcodeBean info = list.get(position);

        Holder holder = null;
        if (convertView == null) {
            view = View.inflate(context, R.layout.me_myleruninto_listview,
                    null);
            holder = new Holder();
            holder.mylerun_qrcode = (ImageView) view.findViewById(R.id.iv_mylerun_qrcode);
            holder.mylerun_username = (TextView) view.findViewById(R.id.tv_mylerun_username);
            holder.charge_state = (TextView) view.findViewById(R.id.charge_state);

            holder.mylerun_title = (TextView) view.findViewById(R.id.tv_mylerun_title);
            holder.mylerun_payment = (TextView) view.findViewById(R.id.tv_mylerun_payment);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (Holder) view.getTag();
        }
        Glide.with(context).load(IPAddress.path + info.getImagePath()).into(holder.mylerun_qrcode);
        holder.mylerun_username.setText(info.getPersonal_name());
        holder.mylerun_title.setText(info.getLerun_title());
        holder.mylerun_payment.setText(info.getPayment()+"");
        switch (info.getCharge_state()) {
            case 0:
                holder.charge_state.setText("未付款");
                break;
            case 1:
                holder.charge_state.setText("已付款");
                break;

            default:
                break;
        }

//        lerun_id = Integer.parseInt(info.get);

//        mylerun_listview.setOnItemClickListener(this);


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Bundle bundle = new Bundle();
        bundle.putInt("lerun_id",lerun_id);
        MyLeRunFragmentInTo myLeRunFragmentInTo = new MyLeRunFragmentInTo();
        myLeRunFragmentInTo.setArguments(bundle);
        Utility.replace2DetailFragment(mFragmentManager, myLeRunFragmentInTo);
    }


    class Holder {
        ImageView mylerun_qrcode;
        TextView charge_state;
        TextView mylerun_username;
        TextView mylerun_type;
        TextView mylerun_title;
        TextView mylerun_payment;
    }
}
