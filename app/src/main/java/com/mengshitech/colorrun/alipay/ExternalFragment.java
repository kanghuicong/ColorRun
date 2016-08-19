package com.mengshitech.colorrun.alipay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mengshitech.colorrun.R;
/**
 * 作者：wschenyongyin on 2016/8/4 10:53
 * 说明:
 */
public class ExternalFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.pay_external, container, false);
	}
}
