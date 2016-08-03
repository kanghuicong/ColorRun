package com.mengshitech.colorrun.fragment.history;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.MyPagerAdapter;
import com.mengshitech.colorrun.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;

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
        if (historyview == null) {
            historyview = View.inflate(getActivity(), R.layout.history_fragment, null);
            ViewPager vp = (ViewPager)historyview.findViewById(R.id.viewPager);
            context=getActivity();
            fragmentList.add(new HistoryTheme(getActivity(),"pop"));
            fragmentList.add(new HistoryTheme(getActivity(),"color"));
            fragmentList.add(new HistoryTheme(getActivity(),"marathon"));
            fragmentList.add(new HistoryTheme(getActivity(),"rainbow"));
            fragmentList.add(new HistoryTheme(getActivity(),"light"));
            titleList.add("卡乐泡泡跑");
            titleList.add("卡乐彩色跑");
            titleList.add("卡乐马拉松");
            titleList.add("卡乐水枪跑");
            titleList.add("卡乐荧光跑");
            vp.setAdapter(new MyPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
        }
        ViewGroup parent = (ViewGroup) historyview.getParent();
        if (parent != null) {
            parent.removeView(historyview);
        }
        return historyview;
    }


}