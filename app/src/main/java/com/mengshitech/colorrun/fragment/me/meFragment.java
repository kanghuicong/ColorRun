package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
/**
 * atenklsy
 */
public class meFragment extends Fragment implements OnClickListener {
    View meView;
    LinearLayout llUserHead,llMyLeRun, llMyShow, llAboutUs,llCancel;
    ImageView ivUserHead;
    FragmentManager fm;
    TextView tvUserName;
    private Activity mActivity;
    String type,id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("user_type", Activity.MODE_PRIVATE);
        type = sharedPreferences.getString("user_type", "");
        id = sharedPreferences.getString("user_id", "");

        if (meView == null) {
            mActivity = getActivity();
            meView = View.inflate(getActivity(), R.layout.fragment_me, null);
            initView();
            new Thread(runnable).start();
        }
        ViewGroup parent = (ViewGroup) meView.getParent();
        if (parent != null) {
            parent.removeView(meView);
        }
        return meView;
    }

    private void initView() {
        meView = View.inflate(getActivity(), R.layout.fragment_me, null);
        llUserHead = (LinearLayout) meView.findViewById(R.id.llUserHead);
        ivUserHead = (ImageView)meView.findViewById(R.id.ivUserHead);
        tvUserName = (TextView)meView.findViewById(R.id.tvUserName);
        // 头像那一行
        llUserHead.setOnClickListener(this);
        llMyLeRun = (LinearLayout) meView.findViewById(R.id.llMyLeRun);
        // 我的乐跑那一行
        llMyLeRun.setOnClickListener(this);
        llMyShow = (LinearLayout) meView.findViewById(R.id.llMyShow);
        // 我的秀那一行
        llMyShow.setOnClickListener(this);
        llAboutUs = (LinearLayout) meView.findViewById(R.id.llAboutUs);
        // 关于我们那一行
        llAboutUs.setOnClickListener(this);
        //注销
        llCancel = (LinearLayout)meView.findViewById(R.id.llCancel);
        llCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        fm = getFragmentManager();
        //fragmentManager（碎片管理器）
        switch (v.getId()) {
            case R.id.llUserHead:
                //点击头像事件
                if (IPAddress.login_state.equals("0")){
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }else if (IPAddress.login_state.equals("1")){
                    Utility.replace2DetailFragment(fm, new myDetailFragment());
                }
                break;
            case R.id.llMyLeRun:
                Log.i("登陆状态",type);
                if (IPAddress.login_state.equals("1")){
                    Utility.replace2DetailFragment(fm, new myLeRunFragment());
                }else{
                    Toast.makeText(mActivity,"您还没有登陆哦,请先登录"+IPAddress.login_state,Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                }
                // 点击我的乐跑事件

                break;
            case R.id.llMyShow:
                if (IPAddress.login_state.equals("1")){
                    Utility.replace2DetailFragment(fm, new myShowFragment());
                }else{
                    Toast.makeText(mActivity,"您还没有登陆哦,请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                }

                break;
            case R.id.llAboutUs:
                // 点击关于我们事件
                Utility.replace2DetailFragment(fm, new AboutUsFragment());
                break;
            case R.id.llCancel:
                if (IPAddress.login_state.equals("1")){
                    DialogUtility.DialogCancel(getActivity(),ivUserHead,tvUserName);

                }else{
                    Toast.makeText(mActivity,"您还没有登陆哦,请先登录",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                }


                break;

            default:
                break;
        }
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            initView();
        }
    };


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = IPAddress.PATH;
            Map<String,String> map = new HashMap<String, String>();
            map.put("flag", "user");
            map.put("user_id",id);
            map.put("index","4");
            String result = HttpUtils.sendHttpClientPost(path,map,"utf-8");
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
//                progressDialog.dismiss();
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    UserEntiy userEntiy = JsonTools.getUserInfo("result",result);
                    tvUserName.setText(userEntiy.getUser_id());
                    if (userEntiy.getUser_header().equals("")){

                    }else{
                        String header_path = IPAddress.path+userEntiy.getUser_header();
                        Log.i("header_path:",header_path);
                        Glide.with(getActivity()).load(header_path).transform(new GlideCircleTransform(mActivity)).into(ivUserHead);
                    }
                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        //监听返回按钮
//        exit();
//    }
//
//    private void exit() {
//        if (!isExit) {
//            isExit = true;
//            Toast.makeText(getApplicationContext(), "再按一次退出程序",
//                    Toast.LENGTH_SHORT).show();
//            // 利用handler延迟发送更改状态信息
//            mHandler.sendEmptyMessageDelayed(0, 2000);
//            //如果2秒内没有退出，则变成false。重新按两次才能退出。
//        } else {
//            finish();
//            System.exit(0);
//        }
//    }



}
