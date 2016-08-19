package com.mengshitech.colorrun.fragment.lerun;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.releaseshow.ImgsActivity;

/**
 * Created by kanghuicong on 2016/7/15.
 */
public class ShowMap extends Fragment {
    View showmap_view;
    Context context;
    String map_path;
   private FrameLayout frameLayout;
   private LinearLayout linearLayout;

    @SuppressLint("ValidFragment")
    public ShowMap(Context context, String map_path, FrameLayout frameLayout, LinearLayout linearLayout){
        this.context = context;
        this.map_path = map_path;
        this.frameLayout=frameLayout;
        this.linearLayout=linearLayout;
    }

    public ShowMap(){}

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        showmap_view = View.inflate(getActivity(), R.layout.lerun_into_showmap, null);
        ImageView showmap = (ImageView)showmap_view.findViewById(R.id.into_lerun_showmap);
        Log.i("map_path",map_path+"");
        Glide.with(context).load(map_path).into(showmap);
        showmap_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                getFragmentManager().popBackStack();
                frameLayout.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

            }
        });
        return showmap_view;
    }

    @Override
    public void onPause() {
        super.onPause();
        frameLayout.setVisibility(View.GONE);
    }
}
