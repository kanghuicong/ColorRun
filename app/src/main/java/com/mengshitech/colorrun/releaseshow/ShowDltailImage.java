package com.mengshitech.colorrun.releaseshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;

/**
 * 作者：wschenyongyin on 2016/8/31 16:27
 * 说明:
 */
public class ShowDltailImage extends Activity implements View.OnClickListener{

    private LinearLayout btn_back;
    ImageView show_image,image_delete;
    String image_path;
    int postion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_detail_image);

        Intent intent=getIntent();
        postion=intent.getIntExtra("postion",1);

        Log.i("showdltaile_postion",postion+"");
        image_path=intent.getStringExtra("image_path");
        findView();
    }

    private void findView(){
        btn_back= (LinearLayout) findViewById(R.id.showdetail_btn_back);
        show_image= (ImageView) findViewById(R.id.show_detail_image);
        image_delete= (ImageView) findViewById(R.id.image_delete);

        Glide.with(ShowDltailImage.this).load(image_path).error(R.mipmap.defaut_error_long).into(show_image);
        btn_back.setOnClickListener(this);
        image_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showdetail_btn_back:
                finish();
                break;
            case R.id.image_delete:
                Intent intent = new Intent();
//                intent.putExtra("postion",postion+"");
                intent.putExtra("postion",postion);
                setResult(001,intent);
                finish();
                break;
        }
    }
}
