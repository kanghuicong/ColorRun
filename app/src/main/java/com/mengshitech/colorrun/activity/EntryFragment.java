package com.mengshitech.colorrun.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.GuideActivity;
import com.mengshitech.colorrun.activity.SplashActivity;

public class EntryFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entry, null);
        v.findViewById(R.id.btn_entry).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
//                GuideActivity activity = (GuideActivity) getActivity();
//                activity.entryApp();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        return v;
    }
}
