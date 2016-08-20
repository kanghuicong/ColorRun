package com.mengshitech.colorrun.fragment.show;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by atenklsy on 2016/7/14 22:03.
 * E-address:atenk@qq.com.
 */
public class showDetail extends Activity implements View.OnClickListener{
    ImageView showdetail_hear,showdetail_comment,showdetail_share;
    TextView showdetail_username,showdetail_content,showdetail_time,showdetail_comment_num,comment_text;
    GridView showdetail_image,gv_like;
    EditText et_show_comment;
    Button bt_show_comment;
    ListView lv_comment;
    ShowDetailCommentAdpter lv_adapter;
    ShowDetailGridViewAdapter gv_adapter;
    String show_id,comment_userid;
    int state;
    View show_view;
    LinearLayout ll_like,ll_show_like;
    List<String> ImageList;
    List<String> imagepath = new ArrayList<String>();
    List<CommentEntity> commentlist;
    ImageView show_back;

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
        comment_userid = bundle.getString("comment_userid");
        showdetail_username.setText(bundle.getString("user_name"));
        showdetail_content.setText(bundle.getString("show_content"));
        showdetail_time.setText(bundle.getString("show_time"));
        showdetail_comment_num.setText(bundle.getString("show_comment_num"));
        String show_hear = bundle.getString("user_header");
        String show_Image = bundle.getString("show_image");
        String header_path = ContentCommon.path+show_hear;
        Glide.with(showDetail.this).load(header_path).transform(new GlideCircleTransform(this)).into(showdetail_hear);
        try {
            ImageList = JsonTools.getImageInfo(show_Image);
            imagepath = new ArrayList<String>();
            for (int i = 0; i < ImageList.size(); i++) {
                String paths = ImageList.get(i);
                imagepath.add(paths);
                ShowGridViewAdapter adapter = new ShowGridViewAdapter(showDetail.this,showDetail.this,imagepath,imagepath.size());
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
        showdetail_comment = (ImageView)findViewById(R.id.im_show_comment);
        showdetail_comment_num = (TextView)findViewById(R.id.tv_show_comment_num);
        showdetail_share = (ImageView)findViewById(R.id.im_show_share);
        showdetail_time = (TextView)findViewById(R.id.tv_show_time);
        show_back= (ImageView) findViewById(R.id.show_back);
        ll_like = (LinearLayout)findViewById(R.id.ll_showlistview_like);
        ll_show_like = (LinearLayout)findViewById(R.id.ll_show_like);
        show_view = (View)findViewById(R.id.show_view);
        gv_like = (GridView)findViewById(R.id.gv_showdetail_image);
        lv_comment = (ListView)findViewById(R.id.lv_showdetail_comment);
        comment_text = (TextView)findViewById(R.id.tv_show_comment_text);
        et_show_comment = (EditText)findViewById(R.id.et_show_comment);
        bt_show_comment = (Button)findViewById(R.id.bt_show_comment);
        bt_show_comment.setOnClickListener(this);
        show_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_show_comment:
                if (ContentCommon.login_state.equals("1")){
                    if ("".equals(et_show_comment.getText().toString())) {
                        Toast.makeText(showDetail.this, "请输入评论内容...", Toast.LENGTH_SHORT).show();
                    }else {
                        new Thread(comment_show_runnable).start();}
                } else{
                    Toast.makeText(showDetail.this,"没有登录，不能评论哦~",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.show_back:
                finish();

                break;
        }
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
            Message msg = new Message();
            msg.obj = result;
            like_handler.sendMessage(msg);
        }
    };

    Handler like_handler= new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(showDetail.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    List<LikeEntity> likelist = JsonTools.getLikeInfo("result",result);
                    gv_adapter = new ShowDetailGridViewAdapter(showDetail.this,likelist,likelist.size());
                    gv_like.setAdapter(gv_adapter);
                    int state = JsonTools.getState("state",result);
                    Log.i("点赞state",state+"");
                    if (state == 1){
                        gv_adapter = new ShowDetailGridViewAdapter(showDetail.this,likelist,likelist.size());
                        gv_like.setAdapter(gv_adapter);
                    } else {
                        show_view.setVisibility(View.GONE);
                        ll_show_like.setVisibility(View.GONE);
                    }
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
                Toast.makeText(showDetail.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
//                progressDialog.dismiss();
                try {
                    commentlist = JsonTools.getCommentInfo("result",result);

                    lv_adapter = new ShowDetailCommentAdpter(showDetail.this,commentlist,lv_comment);
                    lv_comment.setAdapter(lv_adapter);
                    int state = JsonTools.getState("state",result);
                    Log.i("commentlist",commentlist+"");
                    if (state == 1){
                        lv_adapter = new ShowDetailCommentAdpter(showDetail.this,commentlist,lv_comment);
                        lv_comment.setAdapter(lv_adapter);
                    } else {
                        comment_text.setVisibility(View.VISIBLE);
                    }
                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    Runnable comment_show_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "7");
            map.put("show_id",show_id);
            map.put("user_id",ContentCommon.user_id);
            map.put("comment_content",et_show_comment.getText().toString());
            map.put("comment_userid",comment_userid);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            comment_show_handler.sendMessage(msg);
        }
    };

    Handler comment_show_handler= new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(showDetail.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    state = JsonTools.getState("state",result);
                    if (state ==1){
                        Toast.makeText(showDetail.this,"评论成功！",Toast.LENGTH_SHORT).show();

                        UserDao dao = new UserDao(showDetail.this);
                        UserEntiy modler =  dao.find(ContentCommon.user_id);
                        long time_now = new Date().getTime();

                        CommentEntity info = new CommentEntity();
                        info.setUser_name(modler.getUser_name());
                        info.setUser_header(modler.getUser_header());
                        info.setComment_time(time_now+"");
                        info.setComment_content(et_show_comment.getText().toString());
                        commentlist.add(info);
                        lv_adapter.notifyDataSetChanged();
                        et_show_comment.setText("");
                    } else {
                        Toast.makeText(showDetail.this,"评论失败！",Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };
}
