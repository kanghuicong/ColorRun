package com.mengshitech.colorrun.adapter;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.List;

/**
 * Created by kanghuicong on 2016/7/20  17:01.
 * 515849594@qq.com
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private List<String> titleList;

    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return (fragmentList == null || fragmentList.size() == 0) ? null : fragmentList.get(arg0);
//        return fragmentList.get(arg0);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(" "
                + titleList.get(position));
        ForegroundColorSpan fcs = new ForegroundColorSpan(android.graphics.Color.parseColor("#11cd6e"));//字体颜色设置为绿色
        ssb.setSpan(fcs, 1, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);//设置字体颜色
        return ssb;
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}
