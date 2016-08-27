package com.mengshitech.colorrun.fragment.lerun;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.fragment.me.myLeRunFragment;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.Utility;

/**
 * 作者：wschenyongyin on 2016/8/24 11:46
 * 说明:
 */
public class DisplayQRcodeFragment extends BaseFragment {

    View view;
    Context context;
    private TextView title;
    private TextView tv_state;
    private TextView tv_countdown;
    private ImageView qrcodeImage;
    private int type;
    private String codeimage;
    private int time=3;
    private ImageView btn_back;
    FragmentManager fm;

    @Override
    public View initView() {
        context = getContext();
        fm=getFragmentManager();
        view = View.inflate(context, R.layout.activity_displayqrcode, null);
        title = (TextView) view.findViewById(R.id.title_barr);
        tv_state = (TextView) view.findViewById(R.id.tv_state);
        tv_countdown = (TextView) view.findViewById(R.id.tv_countdown);
        qrcodeImage = (ImageView) view.findViewById(R.id.qrcodeImage);
        btn_back = (ImageView) view.findViewById(R.id.title_back);
        getDatas();
        return view;
    }

    private void getDatas() {
        type = getArguments().getInt("type");
        codeimage = getArguments().getString("qrcode_image");
        Glide.with(context).load(ContentCommon.path+codeimage).error(R.mipmap.defaut_error_square).into(qrcodeImage);
        switch (type) {
            //主页查看二维码

            case 1:
                Log.i("codeimage",codeimage+"");

                tv_state.setText("凭此二维码进行签到");
                btn_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fm.popBackStack();
                    }
                });
                break;
            case 2:
                title.setText("报名结果");
                tv_state.setText("报名成功");
                handler.sendEmptyMessageDelayed(0,1000);
                break;
            case 3:
                tv_state.setText("正在审核中，请耐心等待！");
                break;
            case 4:
                tv_state.setText("凭此二维码到现场签到！");
                break;
            case 5:
                handler.sendEmptyMessageDelayed(0,1000);
                tv_state.setText("凭此二维码到现场签到！");
                break;
        }
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(time>0){
                tv_countdown.setText(time+"秒后关闭页面");
                time--;
                handler.sendEmptyMessageDelayed(0,1000);
            }else {
                fm.popBackStack();
                Utility.replace2DetailFragment(fm,new myLeRunFragment());
            }

        }
    };
}
