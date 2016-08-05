package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.MyLerunInToListViewAdapter;
import com.mengshitech.colorrun.bean.ImageEntity;
import com.mengshitech.colorrun.bean.OrderEntity;
import com.mengshitech.colorrun.bean.QrcodeBean;
import com.mengshitech.colorrun.customcontrols.AutoSwipeRefreshLayout;
import com.mengshitech.colorrun.customcontrols.ProgressDialog;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：wschenyongyin on 2016/8/2 19:11
 * 说明: 我的乐跑
 */
public class MyLeRunFragmentInTo extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    private int entry_number = 3;
    View mLeRunView;
    List<QrcodeBean> qrcode_list;
    final List<ImageEntity> bmlist = new ArrayList<ImageEntity>();
    private  Bitmap bm;
    private  ImageEntity ib = null;
    private  OrderEntity order_info;
    private ProgressDialog progressDialog;
    private ListView mylerun_listview;
    private MyLerunInToListViewAdapter adapter;
    private String userid;
    private Context context;
    FragmentManager mFragmentManager;
    private int lerun_id;
    private AutoSwipeRefreshLayout autoSwipeRefreshLayout;

    @Override
    public View initView() {

        mActivity = getActivity();
        lerun_id = getArguments().getInt("lerun_id");
        mLeRunView = View.inflate(mActivity, R.layout.me_mylerun, null);
        context=getActivity();
        MainActivity.rgMainBottom.setVisibility(View.GONE);


        MainBackUtility.MainBack(mLeRunView,"乐跑详情",getFragmentManager());
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences("user_type", Activity.MODE_PRIVATE);
        userid = sharedPreferences.getString("user_id", "");
        mFragmentManager=getFragmentManager();
        find();



        return mLeRunView;
    }

    private void find() {
        autoSwipeRefreshLayout=new AutoSwipeRefreshLayout(mActivity);
        autoSwipeRefreshLayout= (AutoSwipeRefreshLayout) mLeRunView.findViewById(R.id.id_swipe_ly);
        autoSwipeRefreshLayout.setColorSchemeColors(android.graphics.Color.parseColor("#87CEFA"));
        autoSwipeRefreshLayout.setOnRefreshListener(this);
        autoSwipeRefreshLayout.autoRefresh();


        mylerun_listview = (ListView) mLeRunView.findViewById(R.id.lv_me_mylerun);

    }



    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "lerun");
            map.put("user_id", userid);
            map.put("lerun_id",lerun_id+"");
            map.put("index", "8");

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Log.i("result", result);
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            Log.i("result111", result);
            if (result.equals("timeout")) {
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    autoSwipeRefreshLayout.setRefreshing(false);
                    qrcode_list = JsonTools.getQrCodeInfo(result);
                    adapter=new MyLerunInToListViewAdapter(context,qrcode_list,mylerun_listview,mFragmentManager);
                    mylerun_listview.setAdapter(adapter);
//

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    public void onRefresh() {
        new Thread(runnable).start();
    }
}
