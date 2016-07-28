package com.mengshitech.colorrun.fragment.history;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Toast;

/**
 * Created by kanghuicong on 2016/7/20  16:35.
 * 515849594@qq.com
 */
public class HistoryFragment extends BaseFragment{

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String>   titleList    = new ArrayList<String>();
    View historyview;
Context context;

    @Override
    public View initView() {
        historyview = View.inflate(getActivity(), R.layout.history_fragment, null);
        ViewPager vp = (ViewPager)historyview.findViewById(R.id.viewPager);
        context=getActivity();
        fragmentList.add(new colorFragment(getActivity()));
        fragmentList.add(new popFragment(getActivity()));
        fragmentList.add(new rainbowFragment(getActivity()));
        titleList.add("卡乐泡泡跑");
        titleList.add("卡乐彩色跑");
        titleList.add("卡乐荧光跑");
        vp.setAdapter(new myPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
        return historyview;
    }


}