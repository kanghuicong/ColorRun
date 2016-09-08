package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.activity.RegisterPwd;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.customcontrols.AutoSwipeRefreshLayout;
import com.mengshitech.colorrun.customcontrols.LogOutDiaLog;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.fragment.lerun.IntoLeRunEnroll;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;
import com.nostra13.universalimageloader.utils.L;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

/**
 * atenklsy
 */
public class meFragment extends Fragment implements OnClickListener {
    View meView;
    LinearLayout llUserHead, llMyLeRun, llMyShow, llAboutUs, llCancel,llId;
    ImageView ivUserHead;
    FragmentManager fm;
    TextView tvUserName, tvUserID;
    private Activity mActivity;
    Context context;
    LogOutDiaLog diaLog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.rgMainBottom.setVisibility(View.VISIBLE);
        mActivity = getActivity();
        context = getActivity();
        initView();
        GetDate();

        return meView;
    }

    private void GetDate() {
        if (ContentCommon.login_state == "1"){
            llId.setVisibility(View.VISIBLE);
        }else {
            llId.setVisibility(View.GONE);
        }
        UserDao dao = new UserDao(context);
        UserEntiy modler = new UserEntiy();
        modler = dao.find(ContentCommon.user_id);
        if (modler == null) {
            if (ContentCommon.user_id != null) {
//                llId.setVisibility(View.VISIBLE);
                new Thread(runnable).start();
            }

        } else {
            Log.i("用户姓名", modler.getUser_name() + "");
            tvUserName.setText(modler.getUser_name());
            tvUserID.setText(modler.getUser_id());
            Log.i("tvUserID", modler.getUser_id());
            if (modler.getUser_header() != null) {
                String header_path = ContentCommon.path + modler.getUser_header();
                ContentCommon.user_log=header_path;
                Glide.with(getActivity()).load(header_path).transform(new GlideCircleTransform(mActivity)).error(R.mipmap.default_avtar).into(ivUserHead);

            }

        }

    }

    private void initView() {
        meView = View.inflate(getActivity(), R.layout.fragment_me, null);
        meView.setFocusable(true);//这个和下面的这个命令必须要设置了，才能监听back事件。
        meView.setFocusableInTouchMode(true);
        meView.setOnKeyListener(backlistener);

        llUserHead = (LinearLayout) meView.findViewById(R.id.llUserHead);
        llId = (LinearLayout)meView.findViewById(R.id.ll_me_id);
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
//                    DialogUtility.DialogCancel(mActivity, ivUserHead, tvUserName, tvUserID);
                    LogOut();
                } else {
                    Intent intent = new Intent(mActivity, LoginActivity.class);
                    mActivity.startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "user");
            map.put("user_id", ContentCommon.user_id);
            map.put("index", "4");
            String result = HttpUtils.sendHttpClientPost(path, map, "utf-8");

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
                    if (userEntiy.getUser_header().equals("")) {
                    } else {
                        String header_path = ContentCommon.path + userEntiy.getUser_header();
                        Glide.with(getActivity()).load(header_path).transform(new GlideCircleTransform(mActivity)).into(ivUserHead);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {


            refreshhandler.sendEmptyMessage(0);
        }
    };


    //注册广播
    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
        filter.addAction("refresh");
        mActivity.registerReceiver(receiver, filter);
        Log.i("onStart", "zhixingle");
    }

    //注销广播
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            try {
                mActivity.unregisterReceiver(receiver);
            } catch (Exception e) {
                // already unregistered
            }
        }
    }

    Handler refreshhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GetDate();
        }
    };

    private void LogOut(){
        diaLog=new LogOutDiaLog(context, R.layout.dialog_logout, R.style.dialog, new LogOutDiaLog.LeaveMyDialogListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_logout:
                        ivUserHead.setImageResource(R.mipmap.default_avtar);
                        tvUserName.setText("未登录");
                        tvUserID.setText("");
                        llId.setVisibility(View.GONE);
                        SharedPreferences mySharedPreferences = context.getSharedPreferences("user_type", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mySharedPreferences.edit();
                        editor.putString("user_type", "0");
                        editor.putString("user_id","");
                        editor.commit();
                        ContentCommon.login_state="0";
                        diaLog.dismiss();
                        break;
                    case R.id.btn_cancel:
                        diaLog.dismiss();
                        break;

                    default:
                        break;
                }
            }
        });

        Window window = diaLog.getWindow();
        diaLog.show();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    long mPressedTime = 0;
    private View.OnKeyListener backlistener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                if (i == KeyEvent.KEYCODE_BACK) {  //表示按返回键 时的操作
                    long mNowTime = System.currentTimeMillis();
                    if ((mNowTime - mPressedTime) > 2000) {// 比较两次按键时间差
                        Toast.makeText(getActivity(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().addToBackStack(null).commit();
                        mPressedTime = mNowTime;
                    }
                }
            }
            return false;
        }
    };



}
