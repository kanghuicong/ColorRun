package com.mengshitech.colorrun.fragment.show;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.ShowAdapter;
import com.mengshitech.colorrun.bean.ImageEntity;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;
import org.json.JSONException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class showFragment extends Fragment implements View.OnClickListener {
    View showView;
    ImageView ivShow_CreateShow;
    ShowAdapter mShowAdapter;
    ListView lvShowContent;
    List<ShowEntity> mShowList;
    FragmentManager fm;
    private Activity mActivity;
    int entry_number = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        showView = View.inflate(mActivity, R.layout.fragment_show, null);
        findById();

//        initDatas();
        new Thread(runnable).start();
        return showView;
    }


    /**
     * 初始化控件
     */
    private void findById() {
//        initShow();
        lvShowContent = (ListView) showView.findViewById(R.id.lvShowContent);
        ivShow_CreateShow = (ImageView) showView.findViewById(R.id.ivShow_CreateShow);

    }


//    /**
//     * 加载数据
//     */
//    private void initDatas() {
//        fm = getActivity().getSupportFragmentManager();
//        lvShowContent.setAdapter(new ShowAdapter(mActivity, mShowList, lvShowContent, fm));
//        ivShow_CreateShow.setOnClickListener(this);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivShow_CreateShow:
                CreateShowFragment mCreateShowFragment = new CreateShowFragment();
                Utility.replace2DetailFragment(fm, mCreateShowFragment);
                break;
            default:
                break;
        }
    }

    private void loadData() {

        int count = mShowAdapter.getCount();
        int length = mShowList.size() / entry_number;
        System.out.println("list" + mShowList.size());
        System.out.println("length" + length);
        // 判断有没有数据
        if (count < length * entry_number) {
            mShowAdapter.addItem(count + entry_number);
        } else if (length * entry_number < count + 1
                && count + 1 < mShowList.size() + 1) {
            mShowAdapter.addItem(mShowList.size());
        } else {
            Toast.makeText(getActivity(), "没有更多show了", Toast.LENGTH_SHORT)
                    .show();
        }
    }


    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            String path = IPAddress.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "2");
            map.put("pageSize","4");
            map.put("currentPage","1");

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
//                progressDialog.dismiss();
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
//                progressDialog.dismiss();
                try {

                    mShowList = JsonTools.getShowInfo("result",result);
                    mShowAdapter = new ShowAdapter(mShowList.size(), getActivity(),getFragmentManager(),mShowList,lvShowContent);
                    lvShowContent.setAdapter(mShowAdapter);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };
}
