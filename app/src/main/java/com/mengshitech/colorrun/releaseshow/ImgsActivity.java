package com.mengshitech.colorrun.releaseshow;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.SpaceImageDetailActivity;
import com.mengshitech.colorrun.fragment.lerun.ShowMap;
import com.mengshitech.colorrun.fragment.me.myLeRunFragment;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.Utility;

public class ImgsActivity extends Activity implements OnClickListener {

    Bundle bundle;
    FileTraversal fileTraversal;
    GridView imgGridView;
    ImgsAdapter imgsAdapter;
    LinearLayout select_layout;
    Util util;
    RelativeLayout relativeLayout2;
    HashMap<Integer, ImageView> hashImage;
    LinearLayout btn_ok, btn_back;
    ArrayList<String> filelist;

    RelativeLayout back;
    Button count;
    String evaluate_content;
    public static LinearLayout ll_imageactivity;
    ArrayList<String> cancelList;
    public static FrameLayout frameLayout;
    int num = 0;
    private TextView btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photogrally);
        cancelList=new ArrayList<String>();
        imgGridView = (GridView) findViewById(R.id.gridView1);
        bundle = getIntent().getExtras();
        fileTraversal = bundle.getParcelable("data");
        evaluate_content = bundle.getString("evaluate_content");
        btn_cancel= (TextView) findViewById(R.id.btn_cancel);
        select_layout = (LinearLayout) findViewById(R.id.selected_image_layout);
        relativeLayout2 = (RelativeLayout) findViewById(R.id.relativeLayout2);
        btn_ok = (LinearLayout) findViewById(R.id.btn_ok);
        back = (RelativeLayout) findViewById(R.id.back);
        count = (Button) findViewById(R.id.count);
        btn_back = (LinearLayout) findViewById(R.id.btn_back);
        ll_imageactivity = (LinearLayout) findViewById(R.id.ll_imageactivity);
        frameLayout = (FrameLayout) findViewById(R.id.fm_image);


        hashImage = new HashMap<Integer, ImageView>();
        if (ContentCommon.ShowImageList == null) {
            ContentCommon.ShowImageList = new ArrayList<String>();

        } else {
            num = ContentCommon.ShowImageList.size();
        }


        filelist = ContentCommon.ShowImageList;
        // imgGridView.setOnItemClickListener(this);
        util = new Util(this);

        imgsAdapter = new ImgsAdapter(this, fileTraversal.filecontent,
                onItemClickClass);
        imgGridView.setAdapter(imgsAdapter);

        btn_ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                sendfiles();

            }
        });
        btn_back.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

    }


    class BottomImgIcon implements OnItemClickListener {

        int index;

        public BottomImgIcon(int index) {
            this.index = index;
        }

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {

        }
    }

    @SuppressLint("NewApi")
    public ImageView iconImage(final String filepath, int index, CheckBox checkBox)
            throws FileNotFoundException {
        LayoutParams params = new LayoutParams(
                relativeLayout2.getMeasuredHeight() - 10,
                relativeLayout2.getMeasuredHeight() - 10);
        params.gravity = Gravity.CENTER;
        params.leftMargin = 2;
        final ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//		imageView.setBackgroundResource(R.mipmap.imgbg);
//		float alpha = 100;
        imageView.setAlpha(255);
        util.imgExcute(imageView, imgCallBack, filepath);
//		imageView.setOnClickListener(new ImgOnclick(filepath, checkBox));
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMap show = new ShowMap(ImgsActivity.this, filepath, frameLayout, ll_imageactivity);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fm_image, show).addToBackStack(null).commit();
                frameLayout.setVisibility(View.VISIBLE);
//				ll_imageactivity.setVisibility(View.GONE);

            }
        });
        return imageView;
    }

    ImgCallBack imgCallBack = new ImgCallBack() {
        @Override
        public void resultImgCall(ImageView imageView, Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    };

    class ImgOnclick implements OnClickListener {
        String filepath;
        CheckBox checkBox;

        public ImgOnclick(String filepath, CheckBox checkBox) {
            this.filepath = filepath;
            this.checkBox = checkBox;
        }

        @Override
        public void onClick(View arg0) {
            checkBox.setChecked(false);
            select_layout.removeView(arg0);
            count.setText(select_layout.getChildCount() + "");
            filelist.remove(filepath);
        }
    }

    ImgsAdapter.OnItemClickClass onItemClickClass = new ImgsAdapter.OnItemClickClass() {
        @Override
        public void OnItemClick(View v, int Position, CheckBox checkBox) {
            String filapath = fileTraversal.filecontent.get(Position);
            String imagepath = imgGridView.getItemAtPosition(Position).toString();
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                select_layout.removeView(hashImage.get(Position));
                filelist.remove(filapath);
                count.setText(select_layout.getChildCount() + "");
            } else {
                try {
                    checkBox.setChecked(true);
                    ImageView imageView = iconImage(filapath, Position,
                            checkBox);

                    if (imageView != null) {
                        int Num = num + select_layout.getChildCount();
                        ;
                        if (Num < 6) {
                            hashImage.put(Position, imageView);
                            filelist.add(filapath);
                            cancelList.add(filapath);
                            select_layout.addView(imageView);
                            count.setText(select_layout.getChildCount() + "");
                        } else {
                            checkBox.setChecked(false);
                            Toast.makeText(ImgsActivity.this, "最多只能选6张图片",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    };

//    public void tobreak(View view) {
//        finish();
////    }

    public void sendfiles() {


        Intent intent = new Intent(this, ReleaseShowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("files", filelist);
        bundle.putString("evaluate_content", evaluate_content);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ImgsActivity.this, ImgFileListActivity.class));
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                ContentCommon.ShowImageList.removeAll(cancelList);
                startActivity(new Intent(ImgsActivity.this, ImgFileListActivity.class));
                finish();
                break;
            case R.id.btn_cancel:
                ContentCommon.ShowImageList=filelist;
                ContentCommon.ShowImageList.removeAll(cancelList);
                Intent intent = new Intent(this, ReleaseShowActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("files", filelist);
                bundle.putString("evaluate_content", evaluate_content);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }

    }


}
