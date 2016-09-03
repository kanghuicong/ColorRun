package com.mengshitech.colorrun.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.GuideActivity;
import com.mengshitech.colorrun.activity.SplashActivity;

public class EntryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_entry, null);

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);


        int mScreenHeight = wm.getDefaultDisplay().getHeight();
        FrameLayout.LayoutParams lytp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lytp.gravity = Gravity.CENTER_HORIZONTAL;

        int height = (int) ((float) mScreenHeight * 0.7F);
        lytp.setMargins(0,height,0,0);
        Button btn = (Button) v.findViewById(R.id.btn_entry);
        btn.setLayoutParams(lytp);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                GuideActivity activity = (GuideActivity) getActivity();
//                activity.entryApp();
                SharedPreferences mySharedPreferences = getActivity().getSharedPreferences("user_type", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("user_type", "0");
                editor.commit();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();

            }
        });
        return v;
    }
}
