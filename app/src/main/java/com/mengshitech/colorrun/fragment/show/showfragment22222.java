package com.mengshitech.colorrun.fragment.show;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.BaseFragment;

/**
 * 作者：wschenyongyin on 2016/8/6 14:39
 * 说明:
 */
public class showfragment22222  extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    FragmentManager mFragmentManagr;
    Context context;
    View show;
    @Override
    public View initView() {
        mFragmentManagr = getFragmentManager();
        context = getActivity();
        show = View.inflate(getActivity(), R.layout.lerun_event, null);


        return show;
    }




    @Override
    public void onRefresh() {

    }
}
