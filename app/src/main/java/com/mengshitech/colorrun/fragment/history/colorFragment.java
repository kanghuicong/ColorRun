package com.mengshitech.colorrun.fragment.history;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ListView;


import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.HistoryAdapter;
import com.mengshitech.colorrun.bean.HistoryEntity;
import com.mengshitech.colorrun.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;
/**
 * atenklsy
 */
@SuppressLint("ValidFragment")
public class colorFragment extends BaseFragment {
    ListView lvColorFragment;
    List<HistoryEntity> mHistoryList;
    View colorView;
    private Activity mActivity;

    public colorFragment(Activity activity) {
        mActivity = activity;
        initView();
    }

    @Override
    public View initView() {
        // 获取数据源
        colorView = View.inflate(mActivity, R.layout.history_color, null);
        // 引入布局
        findById();
        // 初始化控件
        return colorView;
    }

    private void findById() {
        lvColorFragment = (ListView) colorView
                .findViewById(R.id.lvColorFragment);
        initDatas();
    }

    private void initDatas() {
//        HistoryAdapter mHistoryAdapter = new HistoryAdapter(mActivity,
//                mHistoryList, lvColorFragment);
//        lvColorFragment.setAdapter(mHistoryAdapter);
    }
}
