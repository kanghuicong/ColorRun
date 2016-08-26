package com.mengshitech.colorrun.fragment.lerun;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.ChoseProvinceAdapter;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.MainBackUtility;

/**
 * 作者：wschenyongyin on 2016/8/25 11:17
 * 说明:
 */
public class ChoseProvinceFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    View view;
    Context context;
    ListView listview;
    FragmentManager fm;
    GetProvince callBack;


    public ChoseProvinceFragment(GetProvince callBack) {
        this.callBack = callBack;
    }




    private void init() {


        listview = (ListView) view.findViewById(R.id.listview_province);
        ChoseProvinceAdapter adapter = new ChoseProvinceAdapter(context);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String province_name = parent.getItemAtPosition(position).toString();
        callBack.getprovince(province_name);
        fm.popBackStack();
    }

    @Override
    public View initView() {
        fm = getFragmentManager();
        context = getActivity();
        view = View.inflate(context, R.layout.fragment_choseprovince, null);
        MainBackUtility.MainBack(view, "选择省份", fm);
        init();
        return view;
    }

    public interface GetProvince {
        public String getprovince(String provice);
    }
}
