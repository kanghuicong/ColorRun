package com.mengshitech.colorrun.fragment.show;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.ShowGridViewAdapter;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by atenklsy on 2016/7/14 22:03.
 * E-address:atenk@qq.com.
 */
public class showDetailFragment extends Activity {
    ImageView showdetail_hear,showdetail_like,showdetail_comment,showdetail_share;
    TextView showdetail_username,showdetail_content,showdetail_time,showdetail_like_num,showdetail_comment_num;
    GridView showdetail_image;
    List<String> ImageList;
    List<String> imagepath = new ArrayList<String>();

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail);

        FindId();
        GetData();

    }

    private void GetData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        showdetail_username.setText(bundle.getString("user_name"));
        showdetail_content.setText(bundle.getString("show_content"));
        showdetail_time.setText(bundle.getString("show_time"));
        showdetail_like_num.setText(bundle.getString("show_like_num"));
        showdetail_comment_num.setText(bundle.getString("show_comment_num"));
        String show_hear = bundle.getString("user_header");
        String show_Image = bundle.getString("show_image");
        String header_path = ContentCommon.path+show_hear;
        Glide.with(showDetailFragment.this).load(header_path).into(showdetail_hear);
        try {
            ImageList = JsonTools.getImageInfo(show_Image);
            imagepath = new ArrayList<String>();
            for (int i = 0; i < ImageList.size(); i++) {
                String paths = ImageList.get(i);
                imagepath.add(paths);
                ShowGridViewAdapter adapter = new ShowGridViewAdapter(showDetailFragment.this,imagepath,imagepath.size());
                showdetail_image.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void FindId() {
        showdetail_hear = (ImageView)findViewById(R.id.im_show_userhear);
        showdetail_username = (TextView)findViewById(R.id.tv_show_username);
        showdetail_content = (TextView)findViewById(R.id.tv_show_content);
        showdetail_image = (GridView)findViewById(R.id.gv_show_image);
        showdetail_like = (ImageView)findViewById(R.id.im_show_like);
        showdetail_like_num = (TextView)findViewById(R.id.tv_show_like_num);
        showdetail_comment = (ImageView)findViewById(R.id.im_show_comment);
        showdetail_comment_num = (TextView)findViewById(R.id.tv_show_comment_num);
        showdetail_share = (ImageView)findViewById(R.id.im_show_share);
        showdetail_time = (TextView)findViewById(R.id.tv_show_time);
    }
}
