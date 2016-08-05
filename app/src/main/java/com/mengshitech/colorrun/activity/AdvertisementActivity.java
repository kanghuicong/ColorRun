package com.mengshitech.colorrun.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.utils.ContentCommon;

/**
 * 作者：wschenyongyin on 2016/8/1 14:00
 * 说明:启动时广告页面的activity
 */
public class AdvertisementActivity  extends Activity{
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertisement_layout);
        init();
    }

    private  void init(){

        image= (ImageView) findViewById(R.id.imageadvertisement);

        Glide.with(AdvertisementActivity.this).load(ContentCommon.path+"lerunposter/advertisement_poster.jpg").into(image);


        new Handler(new Handler.Callback() {

            @Override
            public boolean handleMessage(Message msg) {
                if (1 == 1) {
                    startActivity(new Intent(AdvertisementActivity.this,
                            MainActivity.class));
                    finish();
                } else {

                }

                return true;
            }
        }).sendEmptyMessageDelayed(0, 2000);
    }



}
