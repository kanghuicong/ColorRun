package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

/**
 * atenklsy
 */
public class meFragment extends Fragment implements OnClickListener {
    View meView;
    LinearLayout llUserHead, llMyLeRun, llMyShow, llAboutUs, llCancel;
    ImageView ivUserHead;
    FragmentManager fm;
    TextView tvUserName, tvUserID;
    private Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.rgMainBottom.setVisibility(View.VISIBLE);
        mActivity = getActivity();
        initView();
        GetDate();

        return meView;
    }

    private void GetDate() {
        UserDao dao = new UserDao(getActivity());
        UserEntiy modler = new UserEntiy();
        modler = dao.find(ContentCommon.user_id);
        if (modler == null) {
            if (ContentCommon.user_id != null) {
                new Thread(runnable).start();
            }
        } else {
            tvUserName.setText(modler.getUser_name());
            tvUserID.setText(modler.getUser_id());
            Log.i("tvUserID",modler.getUser_id());
            if (modler.getUser_header()!=null){
                String header_path = ContentCommon.path+modler.getUser_header();
                Glide.with(getActivity()).load(header_path).transform(new GlideCircleTransform(mActivity)).into(ivUserHead);
            }
        }
    }

    private void initView() {
        meView = View.inflate(getActivity(), R.layout.fragment_me, null);
        llUserHead = (LinearLayout) meView.findViewById(R.id.llUserHead);
        ivUserHead = (ImageView) meView.findViewById(R.id.ivUserHead);
        tvUserName = (TextView) meView.findViewById(R.id.tvUserName);
        tvUserID = (TextView) meView.findViewById(R.id.tvUserID);
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
        llCancel = (LinearLayout) meView.findViewById(R.id.llCancel);
        llCancel.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        fm = getFragmentManager();
        //fragmentManager（碎片管理器）
        switch (v.getId()) {
            case R.id.llUserHead:
                //点击头像事件
                if (ContentCommon.login_state.equals("1")) {
                    Utility.replace2DetailFragment(fm, new myDetailFragment());
                } else {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            // 点击我的乐跑事件
            case R.id.llMyLeRun:
                if (ContentCommon.login_state.equals("1")) {
                    Utility.replace2DetailFragment(fm, new myLeRunFragment());
                } else {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                }
                break;
            //我的show
            case R.id.llMyShow:
                if (ContentCommon.login_state.equals("1")) {
                    Utility.replace2DetailFragment(fm, new myShowFragment());
                } else {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                }

                break;
            case R.id.llAboutUs:
                // 点击关于我们事件
                Utility.replace2DetailFragment(fm, new AboutUsFragment());
                break;
            case R.id.llCancel:
                if (ContentCommon.login_state.equals("1")) {
                    DialogUtility.DialogCancel(mActivity, ivUserHead, tvUserName, tvUserID);
                } else {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
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
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "user");
            map.put("user_id",ContentCommon.user_id);
            map.put("index","4");
            String result = HttpUtils.sendHttpClientPost(path,map,"utf-8");

            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    UserEntiy userEntiy = JsonTools.getUserInfo("result", result);
                    if ("".equals(userEntiy.getUser_name())) {
                        tvUserName.setText("还没写昵称呢~");
                    } else {
                        tvUserName.setText(userEntiy.getUser_name());
                    }
                    tvUserID.setText(userEntiy.getUser_id());
                    if (userEntiy.getUser_header().equals("")){
                    }else{
                        String header_path = ContentCommon.path+userEntiy.getUser_header();
                        Glide.with(getActivity()).load(header_path).transform(new GlideCircleTransform(mActivity)).into(ivUserHead);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        mActivity = getActivity();
        initView();
        GetDate();
    }
}
