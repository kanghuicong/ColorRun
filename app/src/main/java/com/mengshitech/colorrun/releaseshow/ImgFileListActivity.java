package com.mengshitech.colorrun.releaseshow;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.GetSDKVersion;
import com.nostra13.universalimageloader.utils.L;

public class ImgFileListActivity extends Activity implements OnItemClickListener {

    ListView listView;
    Util util;
    ImgFileListAdapter listAdapter;
    List<FileTraversal> locallist;
    String evaluate_content;
    private int READ_EXTERNAL_STORAGE_REQUEST_CODE = 001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imgfilelist);
        listView = (ListView) findViewById(R.id.listView1);
        if (GetSDKVersion.getAndroidSDKVersion() >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(ImgFileListActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS, Manifest.permission.CAMERA},
                        001);
            } else {
                init();
                ContentCommon.PermissionsState = 1;
            }
        } else {
            ContentCommon.PermissionsState = 1;
            init();
        }


    }


    private void init() {
        util = new Util(this);
        locallist = util.LocalImgFileList();
        List<HashMap<String, String>> listdata = new ArrayList<HashMap<String, String>>();
        Bitmap bitmap[] = null;
        Intent intent = getIntent();
        evaluate_content = intent.getStringExtra("evaluate_content");


        if (locallist != null) {
            bitmap = new Bitmap[locallist.size()];
            for (int i = 0; i < locallist.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("filecount", locallist.get(i).filecontent.size() + "张");
                map.put("imgpath", locallist.get(i).filecontent.get(0) == null ? null : (locallist.get(i).filecontent.get(0)));
                map.put("filename", locallist.get(i).filename);
                listdata.add(map);
            }
        }
        listAdapter = new ImgFileListAdapter(this, listdata);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        Intent intent = new Intent(this, ImgsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", locallist.get(arg2));
        bundle.putString("evaluate_content", evaluate_content);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            Intent intent = new Intent(this, ReleaseShowActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("files", ContentCommon.ShowImageList);
            bundle.putString("evaluate_content", evaluate_content);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 001) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                init();
            } else {
                Toast.makeText(ImgFileListActivity.this, "获取权限失败", Toast.LENGTH_LONG).show();
                finish();

            }

        }

    }


}
