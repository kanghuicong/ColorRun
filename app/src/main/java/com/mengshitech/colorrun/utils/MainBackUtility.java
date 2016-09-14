package com.mengshitech.colorrun.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.releaseshow.ReleaseShowActivity;

/**
 * Created by kanghuicong on 2016/7/21  14:46.
 * 515849594@qq.com
 */
public class MainBackUtility {

    public static void MainBack(View view, String title, final FragmentManager fm){
        TextView title_bar = (TextView)view.findViewById(R.id.title_barr);
        title_bar.setText(title);
        ImageView title_back = (ImageView)view.findViewById(R.id.title_back);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
            }
        });
    }

    public static void MainBackActivity(final Activity activity, String title) {
        RelativeLayout title_back_ll = (RelativeLayout) activity.findViewById(R.id.title_back_ll);
        TextView title_bar = (TextView) activity.findViewById(R.id.title_barr);
        title_bar.setText(title);
        ImageView title_back = (ImageView) activity.findViewById(R.id.title_back);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    public static void MainBack_Show(final Context context, View view, String title, final FragmentManager fm){
        TextView title_bar = (TextView)view.findViewById(R.id.title_barr);
        title_bar.setText(title);
        ImageView title_image = (ImageView)view.findViewById(R.id.title_image);
        title_image.setVisibility(View.VISIBLE);
        title_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ReleaseShowActivity.class));
            }
        });
        ImageView title_back = (ImageView)view.findViewById(R.id.title_back);
        title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.popBackStack();
            }
        });
    }

}
