package com.mengshitech.colorrun.fragment.lerun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;

/**
 * Created by kanghuicong on 2016/7/15.
 */
public class ShowMap extends Fragment{
    View showmap_view;
    Context context;
    String map_path;

    @SuppressLint("ValidFragment")
    public ShowMap(Context context, String map_path){
        this.context = context;
        this.map_path = map_path;
    }

    public ShowMap(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showmap_view = View.inflate(getActivity(), R.layout.lerun_into_showmap, null);
        ImageView showmap = (ImageView)showmap_view.findViewById(R.id.into_lerun_showmap);
        Glide.with(getActivity()).load(map_path).into(showmap);
        showmap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                getFragmentManager().popBackStack();
            }
        });
        return showmap_view;
    }
}
