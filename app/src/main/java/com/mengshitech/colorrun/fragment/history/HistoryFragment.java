package com.mengshitech.colorrun.fragment.history;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.MyPagerAdapter;
import com.mengshitech.colorrun.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
            final PagerTabStrip pagerTabStrip = (PagerTabStrip) historyview.findViewById(R.id.pts);
            pagerTabStrip.setTabIndicatorColorResource(R.color.green);

            // 正常文字颜色
            pagerTabStrip.setTextColor(Color.BLACK);
            context=getActivity();
            fragmentList.add(new HistoryTheme(getActivity(),"1"));
            fragmentList.add(new HistoryTheme(getActivity(),"2"));
            fragmentList.add(new HistoryTheme(getActivity(),"3"));
            fragmentList.add(new HistoryTheme(getActivity(),"4"));
            fragmentList.add(new HistoryTheme(getActivity(),"5"));
            titleList.add("卡乐泡泡跑");
            titleList.add("卡乐彩色跑");
            titleList.add("卡乐马拉松");
            titleList.add("卡乐水枪跑");
            titleList.add("卡乐荧光跑");
            vp.setAdapter(new MyPagerAdapter(getChildFragmentManager(), fragmentList, titleList));
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    Log.i("postion",""+position);

                }
                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
        ViewGroup parent = (ViewGroup) historyview.getParent();
        if (parent != null) {
            parent.removeView(historyview);
        }
        return historyview;
    }
}