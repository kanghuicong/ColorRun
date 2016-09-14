package com.mengshitech.colorrun.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import com.mengshitech.colorrun.alipay.AlipayFragment;
import com.mengshitech.colorrun.bean.ImageEntity;
import com.mengshitech.colorrun.bean.QrcodeBean;
import com.mengshitech.colorrun.customcontrols.QrcodeDialog;
import com.mengshitech.colorrun.fragment.history.HistoryContent;
import com.mengshitech.colorrun.fragment.history.HistoryFragment;
import com.mengshitech.colorrun.fragment.lerun.DisplayQRcodeFragment;
import com.mengshitech.colorrun.fragment.lerun.LeRunPayment;
import com.mengshitech.colorrun.fragment.me.LerunEvaluateFragment;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.Utility;

import java.io.FileNotFoundException;
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
    private QrcodeDialog dialog;

    public MyLerunInToListViewAdapter(Context context) {
        this.context = context;
    }

    public MyLerunInToListViewAdapter(Context context, List<QrcodeBean> list, ListView mylerun_listview, FragmentManager mFragmentManager) {
        super();

        this.context = context;
        this.list = list;
        this.mylerun_listview = mylerun_listview;
        this.mFragmentManager = mFragmentManager;

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
        if (info.getImagePath() != null && !info.getImagePath().equals("")) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.defaut_error_square);
            FileNotFoundException e;
            Glide.with(context).load(ContentCommon.path + info.getImagePath()).error(R.mipmap.defaut_error_square).into(holder.mylerun_qrcode);
        }
        holder.mylerun_username.setText(info.getPersonal_name());
        holder.mylerun_title.setText(info.getLerun_title());
        holder.mylerun_payment.setText(info.getPayment() + "");
        int evaluate_state = info.getEvaluate_state();
        int charge_state = info.getCharge_state();
        int sign_state = info.getSign_state();
        int check_state = info.getCheck_state();
        switch (check_state) {
            case 0:
                holder.charge_state.setText("审核中");
                break;
            case 1:
                if (charge_state == 1) {
                    if (sign_state == 0) {
                        holder.charge_state.setText("未签到");
                    } else if (sign_state == 1) {

                        if (evaluate_state == 1) {
                            holder.charge_state.setText("已评价");
                        } else {
                            holder.charge_state.setText("未评价");
                        }
                    }
                } else {
                    holder.charge_state.setText("未付款");
                }

                break;
            case 2:
                holder.charge_state.setText("审核未通过");
                break;
            default:
                break;
        }

//        lerun_id = Integer.parseInt(info.get);

        mylerun_listview.setOnItemClickListener(this);


        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        QrcodeBean info = list.get(position);
        String qr_image = info.getImagePath();
        lerun_id = info.getLerun_id();
        int evaluate_state = info.getEvaluate_state();
        int charge_state = info.getCharge_state();
        int sign_state = info.getSign_state();
        int check_state = info.getCheck_state();
        String user_telphone = info.getUser_telphone();
        String order_id=info.getOrder_id();

        if(check_state==0){
            Bundle bundle=new Bundle();
            bundle.putString("qrcode_image",qr_image+"");
            bundle.putInt("type",3);
            DisplayQRcodeFragment displayQRcodeFragment=new DisplayQRcodeFragment();
            displayQRcodeFragment.setArguments(bundle);
            Utility.replace2DetailFragment(mFragmentManager, displayQRcodeFragment);

        }else if(check_state==1){
            if (charge_state == 0) {
                Bundle bundle = new Bundle();
                bundle.putString("user_name", info.getPersonal_name());
                bundle.putString("lerun_title", info.getLerun_title());
                bundle.putInt("lerun_price", info.getPayment());
                bundle.putInt("lerun_id",info.getLerun_id());
                bundle.putString("user_telphone",info.getUser_telphone());
                bundle.putString("qrcode_image",info.getImagePath());
                bundle.putString("order_id", order_id);

                AlipayFragment alipayFragment = new AlipayFragment();
                alipayFragment.setArguments(bundle);
                Utility.replace2DetailFragment(mFragmentManager, alipayFragment);
            } else if (charge_state == 1) {


                if (sign_state == 0) {
                    Bundle bundle=new Bundle();
                    bundle.putString("qrcode_image",qr_image+"");
                    bundle.putInt("type",4);
                    DisplayQRcodeFragment displayQRcodeFragment=new DisplayQRcodeFragment();
                    displayQRcodeFragment.setArguments(bundle);
                    Utility.replace2DetailFragment(mFragmentManager, displayQRcodeFragment);
                }
                //已经签到
                else if (sign_state == 1) {
                    //评价了
                    if (evaluate_state == 1) {

                        Intent intent = new Intent(context, HistoryContent.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("lerun_id", lerun_id);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    //未评价
                    else {
                        LerunEvaluateFragment lerunEvaluateFragment = new LerunEvaluateFragment();
                        Bundle bundle = new Bundle();
                        bundle.putInt("lerun_id", lerun_id);
                        bundle.putString("user_telphone", user_telphone);
                        lerunEvaluateFragment.setArguments(bundle);
                        Utility.replace2DetailFragment(mFragmentManager, lerunEvaluateFragment);
                    }
                }


            }

        }else if(check_state==2){

        }






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
