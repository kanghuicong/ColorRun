package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.UserLogActivity;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


/**
 * atenklsy
 */
public class myDetailFragment extends BaseFragment implements View.OnClickListener{
    View mDeatilView;
    FragmentManager fm;
    ImageView iv_head;
    LinearLayout me_head,me_nickname,me_phone,me_email,me_sex,me_height,me_weight,me_address,me_sign;
    TextView tv_nickname,tv_phone,tv_sex,tv_height,tv_weight,tv_sign,tv_email,tv_address;
    String userid ;
    String header_path;

    private Context context;
    @Override
    public View initView() {
        context = getActivity();
        mDeatilView = View.inflate(context, R.layout.me_detail, null);
        MainBackUtility.MainBack(mDeatilView,"个人信息",getFragmentManager());
        SharedPreferences sharedPreferences = context
                .getSharedPreferences("user_type", Activity.MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", "");

        FindId();
        Click();
        GetData();
        return mDeatilView;
    }

    private void GetData() {
        UserDao dao = new UserDao(getActivity());
        UserEntiy modler = new UserEntiy();
        modler = dao.find(ContentCommon.user_id);
        if (modler == null) {
            new Thread(runnable).start();
        }else {
            tv_nickname.setText(modler.getUser_name());
            tv_phone.setText(modler.getUser_phone());
            tv_email.setText(modler.getUser_email());
            tv_sex.setText(modler.getUser_sex());
            tv_height.setText(modler.getUser_height());
            tv_weight.setText(modler.getUser_weight());
            tv_address.setText(modler.getUser_address());
            tv_sign.setText(modler.getUser_sign());

            if (modler.getUser_header()!=null){
                header_path = ContentCommon.path+modler.getUser_header();
                ContentCommon.user_log=header_path;
                Glide.with(context).load(header_path).transform(new GlideCircleTransform(mActivity)).into(iv_head);
            }
        }

    }

    private void Click() {
        me_nickname.setOnClickListener(this);
        me_phone.setOnClickListener(this);
        me_sex.setOnClickListener(this);
        me_email.setOnClickListener(this);
        me_height.setOnClickListener(this);
        me_weight.setOnClickListener(this);
        me_address.setOnClickListener(this);
        me_sign.setOnClickListener(this);
        me_head.setOnClickListener(this);
    }

    private void FindId() {
        me_head = (LinearLayout)mDeatilView.findViewById(R.id.ll_me_head);
        iv_head = (ImageView)mDeatilView.findViewById(R.id.iv_me_head);
        me_nickname = (LinearLayout)mDeatilView.findViewById(R.id.ll_me_nickname);
        tv_nickname = (TextView)mDeatilView.findViewById(R.id.tv_me_nickname);
        me_phone = (LinearLayout)mDeatilView.findViewById(R.id.ll_me_phone);
        tv_phone = (TextView)mDeatilView.findViewById(R.id.tv_me_phone);
        me_email = (LinearLayout)mDeatilView.findViewById(R.id.ll_me_email);
        tv_email = (TextView)mDeatilView.findViewById(R.id.tv_me_email);
        me_sex = (LinearLayout)mDeatilView.findViewById(R.id.ll_me_sex);
        tv_sex = (TextView) mDeatilView.findViewById(R.id.tv_me_sex);
        me_height = (LinearLayout)mDeatilView.findViewById(R.id.ll_me_height);
        tv_height = (TextView)mDeatilView.findViewById(R.id.tv_me_height);
        me_weight = (LinearLayout)mDeatilView.findViewById(R.id.ll_me_weight);
        tv_weight = (TextView)mDeatilView.findViewById(R.id.tv_me_weight);
        me_address = (LinearLayout)mDeatilView.findViewById(R.id.ll_me_land);
        tv_address = (TextView)mDeatilView.findViewById(R.id.tv_me_land);
        me_sign = (LinearLayout)mDeatilView.findViewById(R.id.ll_me_autograph);
        tv_sign = (TextView) mDeatilView.findViewById(R.id.tv_me_autograph);


    }

    @Override
    public void onClick(View v) {
        fm =getFragmentManager();
        switch (v.getId()){
            case R.id.ll_me_head:
                context.startActivity(new Intent(context, UserLogActivity.class));
                break;
            case R.id.ll_me_nickname:
                DialogUtility.DialogNickname(context,tv_nickname,userid);
                break;
            case R.id.ll_me_phone:
                DialogUtility.DialogPhone(context,tv_phone,userid);
                break;
            case R.id.ll_me_email:
                DialogUtility.DialogAutograph("email",context,tv_email,userid);
                break;
            case R.id.ll_me_sex:
                DialogUtility.DialogSex(context,tv_sex,userid);
                break;
            case R.id.ll_me_height:
                DialogUtility.DialogPhysique("height",context,tv_height,userid);
                break;
            case R.id.ll_me_weight:
                DialogUtility.DialogPhysique("weight",context,tv_weight,userid);
                break;
            case R.id.ll_me_land:
                DialogUtility.DialogAutograph("address",context,tv_address,userid);
                break;
            case R.id.ll_me_autograph:
                DialogUtility.DialogAutograph("sign",context,tv_sign,userid);
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
            map.put("user_id", userid);
            map.put("index", "4");

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            if (result.equals("timeout")) {

                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
//                progressDialog.dismiss();
                try {
                    UserEntiy userEntiy = JsonTools.getUserInfo("result",result);
                    tv_nickname.setText(userEntiy.getUser_name());
                    tv_phone.setText(userEntiy.getUser_phone());
                    tv_email.setText(userEntiy.getUser_email());
                    tv_sex.setText(userEntiy.getUser_sex());
                    tv_height.setText(userEntiy.getUser_height());
                    tv_weight.setText(userEntiy.getUser_weight());
                    tv_address.setText(userEntiy.getUser_address());
                    tv_sign.setText(userEntiy.getUser_sign());
                    if (userEntiy.getUser_header()!=null){
                        header_path = ContentCommon.path+userEntiy.getUser_header();
                        ContentCommon.user_log=header_path;
                        Glide.with(context).load(header_path).transform(new GlideCircleTransform(mActivity)).into(iv_head);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        GetData();
    }
}
