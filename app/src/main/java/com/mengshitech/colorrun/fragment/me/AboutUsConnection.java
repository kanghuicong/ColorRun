package com.mengshitech.colorrun.fragment.me;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.MainBackUtility;

/**
 * Created by kanghuicong on 2016/8/4  16:23.
 */
public class AboutUsConnection extends Fragment {
    View us_connection_view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        us_connection_view = View.inflate(getActivity(), R.layout.me_aboutus_connection, null);
        MainBackUtility.MainBack(us_connection_view,"联系我们",getFragmentManager());
        return us_connection_view;
    }
}