package com.mengshitech.colorrun.fragment;


import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.me.MyLeRunFragment;
import com.mengshitech.colorrun.utils.Utility;

/**
 * 作者：wschenyongyin on 2016/8/24 09:22
 * 说明:
 */
public class PaySuccessFragment extends BaseFragment{
    Context context;
    View view;
    TextView textView,title;
    FragmentManager fm;
    @Override
    public View initView() {
        context=getActivity();
        fm=getFragmentManager();
        MainActivity.rgMainBottom.setVisibility(View.GONE);
       view=View.inflate(context,R.layout.activity_register_success,null);
        textView = (TextView)view.findViewById(R.id.tv_register_success);
        title=(TextView)view.findViewById(R.id.title_barr);
        title.setText("结果");
        textView.setText("报名成功");
        handle.sendEmptyMessageDelayed(0,2000);
        return view;
    }


    Handler handle=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MyLeRunFragment fragment=new MyLeRunFragment();
            getFragmentManager().popBackStack();
            Utility.replace2DetailFragment(fm,fragment);


        }
    };
}
