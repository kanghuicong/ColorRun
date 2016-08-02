package com.mengshitech.colorrun.fragment.lerun;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.MyPagerAdapter;
import com.mengshitech.colorrun.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghuicong on 2016/8/2  19:26.
 */
public class LeRunThemePager extends BaseFragment {

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> titleList = new ArrayList<String>();
    View historyview;
    Context context;

    @Override
    public View initView() {
        historyview = View.inflate(getActivity(), R.layout.lerun_theme, null);
        ViewPager vp = (ViewPager) historyview.findViewById(R.id.viewPager);
        context = getActivity();
        fragmentList.add(new LeRunTheme(getActivity(),"pop"));
        fragmentList.add(new LeRunTheme(getActivity(),"color"));
        fragmentList.add(new LeRunTheme(getActivity(),"marathon"));
        fragmentList.add(new LeRunTheme(getActivity(),"rainbow"));
        fragmentList.add(new LeRunTheme(getActivity(),"light"));
        titleList.add("卡乐泡泡跑");
        titleList.add("卡乐彩色跑");
        titleList.add("卡乐马拉松");
        titleList.add("卡乐水枪跑");
        titleList.add("卡乐荧光跑");
        vp.setAdapter(new MyPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
        return historyview;
    }
}