package com.mengshitech.colorrun.fragment.lerun;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengshitech.colorrun.R;



/**
 * Created by kanghuicong on 2016/8/2  11:28.
 */
public class LeRunPayment extends Fragment implements View.OnClickListener{
    TextView payment_name,payment_title,payment_price;
    View payment_view;
    RelativeLayout rl_zfb,rl_wx;
    ImageView iv_zfb,iv_wx;
    String pay_way;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        payment_view = inflater.inflate(R.layout.lerun_enroll_payment, null);
        FindId();
        GetData();
        return payment_view;
    }

    private void GetData() {
        payment_name.setText("姓名："+getArguments().getString("user_name"));
        payment_title.setText(getArguments().getString("lerun_title"));
        payment_price.setText(getArguments().getInt("lerun_price")+"");
    }

    private void FindId() {
        payment_name = (TextView)payment_view.findViewById(R.id.tv_payment_name);
        payment_title = (TextView)payment_view.findViewById(R.id.tv_payment_title);
        payment_price = (TextView)payment_view.findViewById(R.id.tv_payment_price);
        rl_zfb = (RelativeLayout)payment_view.findViewById(R.id.rl_zfb);
        rl_zfb.setOnClickListener(this);
        iv_zfb = (ImageView)payment_view.findViewById(R.id.iv_payment_zfb);
        rl_wx = (RelativeLayout)payment_view.findViewById(R.id.rl_wxzf);
        rl_wx.setOnClickListener(this);
        iv_wx = (ImageView)payment_view.findViewById(R.id.iv_payment_wx);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_zfb:
                iv_zfb.setImageResource(R.mipmap.btn_play_checked);
                iv_wx.setImageResource(R.mipmap.btn_play_normal);
                pay_way = "zfb";
                break;
            case R.id.rl_wxzf:
                iv_zfb.setImageResource(R.mipmap.btn_play_normal);
                iv_wx.setImageResource(R.mipmap.btn_play_checked);
                pay_way = "wx";
        }
    }
}
