package com.mengshitech.colorrun.fragment.show;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.ShowDetailCommentAdpter;
import com.mengshitech.colorrun.adapter.ShowDetailGridViewAdapter;
import com.mengshitech.colorrun.adapter.ShowGridViewAdapter;
import com.mengshitech.colorrun.bean.CommentEntity;
import com.mengshitech.colorrun.bean.LikeEntity;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by atenklsy on 2016/7/14 22:03.
 * E-address:atenk@qq.com.
 */
public class showDetailFragment extends Activity {
    ImageView showdetail_hear,showdetail_like,showdetail_comment,showdetail_share;
    TextView showdetail_username,showdetail_content,showdetail_time,showdetail_like_num,showdetail_comment_num;
    GridView showdetail_image,gv_like;
    ListView lv_comment;
    String show_id;
    LinearLayout ll_like;
    List<String> ImageList;
    List<String> imagepath = new ArrayList<String>();
    String user_id,user_name,user_header,like_time;

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail);
        FindId();
        ll_like.setVisibility(View.GONE);
        GetData();
        new Thread(like_runnable).start();
        new Thread(comment_runnable).start();
    }


    private void GetData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        show_id = bundle.getString("show_id");
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
        ll_like = (LinearLayout)findViewById(R.id.ll_showlistview_like);
        gv_like = (GridView)findViewById(R.id.gv_showdetail_image);
        lv_comment = (ListView)findViewById(R.id.lv_showdetail_comment);
    }

    Runnable like_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "5");
            map.put("show_id",show_id);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Log.i("result", result);
            Message msg = new Message();
            msg.obj = result;
            like_handler.sendMessage(msg);
        }
    };

    Handler like_handler= new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
//                progressDialog.dismiss();
                Toast.makeText(showDetailFragment.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
//                progressDialog.dismiss();
                try {
                    List<LikeEntity> likelist = JsonTools.getLikeInfo("result",result);
                    Log.i("likelist",likelist+"");
                    ShowDetailGridViewAdapter gv_adapter = new ShowDetailGridViewAdapter(showDetailFragment.this,likelist,likelist.size());
                    gv_like.setAdapter(gv_adapter);
                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };


    Runnable comment_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "4");
            map.put("show_id",show_id);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Log.i("result", result);
            Message msg = new Message();
            msg.obj = result;
            comment_handler.sendMessage(msg);
        }
    };

    Handler comment_handler= new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
//                progressDialog.dismiss();
                Toast.makeText(showDetailFragment.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
//                progressDialog.dismiss();
                try {
                    List<CommentEntity> commentlist = JsonTools.getCommentInfo("result",result);
                    Log.i("commentlist",commentlist+"");
                    ShowDetailCommentAdpter lv_adapter = new ShowDetailCommentAdpter(showDetailFragment.this,commentlist,lv_comment);
                    gv_like.setAdapter(lv_adapter);
                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };
}
