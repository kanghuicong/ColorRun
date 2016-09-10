package com.mengshitech.colorrun.fragment.show;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import com.mengshitech.colorrun.activity.LoginActivity;
import com.mengshitech.colorrun.adapter.ShowDetailCommentAdpter;
import com.mengshitech.colorrun.adapter.ShowDetailGridViewAdapter;
import com.mengshitech.colorrun.adapter.ShowGridViewAdapter;
import com.mengshitech.colorrun.bean.CommentEntity;
import com.mengshitech.colorrun.bean.LikeEntity;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.DateUtils;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.nostra13.universalimageloader.utils.L;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by atenklsy on 2016/7/14 22:03.
 * E-address:atenk@qq.com.
 */
public class showDetail extends Activity implements View.OnClickListener {
    ImageView showdetail_hear, show_delete;
    TextView showdetail_username, showdetail_content, showdetail_time, comment_text,show_report;
    GridView showdetail_image, gv_like;
    EditText et_show_comment;
    Button bt_show_comment;
    ListView lv_comment;
    ShowDetailGridViewAdapter gv_adapter;
    String show_id, comment_userid,comment_id;
    int comment_position;
    View show_view;
    LinearLayout ll_like, ll_show_like;
    List<String> ImageList;
    List<String> imagepath = new ArrayList<String>();
    List<CommentEntity> commentlist = new ArrayList<CommentEntity>();
    ShowDetailCommentAdpter lv_adapter;
    List<CommentEntity> list = new ArrayList<CommentEntity>();

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail);
        MainBackUtility.MainBackActivity(showDetail.this, "秀帖");
        FindId();
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
        String show_hear = bundle.getString("user_header");
        String show_Image = bundle.getString("show_image");
        String header_path = ContentCommon.path + show_hear;
        Glide.with(showDetail.this).load(header_path).transform(new GlideCircleTransform(this)).error(R.mipmap.default_avtar).into(showdetail_hear);
        try {
            ImageList = JsonTools.getImageInfo(show_Image);
            imagepath = new ArrayList<String>();
            for (int i = 0; i < ImageList.size(); i++) {
                String paths = ImageList.get(i);
                imagepath.add(paths);
                ShowGridViewAdapter adapter = new ShowGridViewAdapter(showDetail.this, showDetail.this, imagepath, imagepath.size());
                showdetail_image.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (ContentCommon.user_id.equals(comment_userid)) {
            show_delete.setVisibility(View.VISIBLE);
        }else {
            show_report.setVisibility(View.VISIBLE);
        }
    }

    private void FindId() {
        showdetail_hear = (ImageView) findViewById(R.id.im_show_userhear);
        showdetail_username = (TextView) findViewById(R.id.tv_show_username);
        showdetail_content = (TextView) findViewById(R.id.tv_show_content);
        showdetail_image = (GridView) findViewById(R.id.gv_show_image);
        showdetail_time = (TextView) findViewById(R.id.tv_show_time);
        ll_like = (LinearLayout) findViewById(R.id.ll_showlistview_like);
        ll_show_like = (LinearLayout) findViewById(R.id.ll_show_like);
        show_delete = (ImageView) findViewById(R.id.show_delete);
        show_delete.setOnClickListener(this);
        show_report = (TextView)findViewById(R.id.show_report);
        show_report.setOnClickListener(this);
        show_view = (View) findViewById(R.id.show_view);
        gv_like = (GridView) findViewById(R.id.gv_showdetail_image);
        lv_comment = (ListView) findViewById(R.id.lv_showdetail_comment);
        lv_comment.setOnItemLongClickListener(new MyClickLong());
        comment_text = (TextView) findViewById(R.id.tv_show_comment_text);
        et_show_comment = (EditText) findViewById(R.id.et_show_comment);
        bt_show_comment = (Button) findViewById(R.id.bt_show_comment);
        bt_show_comment.setOnClickListener(this);
        ll_like.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //评论按钮
            case R.id.bt_show_comment:
                if (ContentCommon.login_state.equals("1")) {
                    if ("".equals(et_show_comment.getText().toString())) {
                        Toast.makeText(showDetail.this, "请输入评论内容...", Toast.LENGTH_SHORT).show();
                    } else if (et_show_comment.getText().toString().length() >= 400) {
                        Toast.makeText(showDetail.this, "亲，您的评论太长啦...", Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(comment_show_runnable).start();
                    }
                } else {
                    startActivity(new Intent(showDetail.this, LoginActivity.class));
                    Toast.makeText(showDetail.this, "请先登录...", Toast.LENGTH_SHORT).show();
                }
                break;
            //删除show
            case R.id.show_delete:
                AlertDialog.Builder builder_show = new AlertDialog.Builder(showDetail.this);
                builder_show.setMessage("确定删除show?");
                builder_show.setTitle("提示");
                builder_show.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(delete_show_runnable).start();

                    }
                });
                builder_show.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder_show.create().show();
                break;
            case R.id.show_report:
                AlertDialog.Builder builder_report = new AlertDialog.Builder(showDetail.this);
                builder_report.setMessage("确定举报该show?");
                builder_report.setTitle("提示");
                builder_report.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder_report.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder_report.create().show();
                break;
            default:
                break;
        }
    }

    class MyClickLong implements AdapterView.OnItemLongClickListener {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            //定义AlertDialog.Builder对象，当长按列表项的时候弹出确认删除对话框
            comment_position = position;
            CommentEntity commentEntity = commentlist.get(position);
            Log.d("MyClickLong", ContentCommon.user_id+"1111");
            Log.d("MyClickLong", commentEntity.getUser_id()+"2222");
            if (ContentCommon.user_id.equals(comment_userid) || ContentCommon.user_id.equals(commentEntity.getUser_id())) {
                comment_id = commentEntity.getComment_id();
                AlertDialog.Builder builder = new AlertDialog.Builder(showDetail.this);
                builder.setMessage("确定删除评论?");
                builder.setTitle("提示");

                //添加AlertDialog.Builder对象的setPositiveButton()方法
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(delete_comment_runnable).start();
                    }
                });

                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.create().show();
            }
            return false;
        }
    }

    //点赞信息
    Runnable like_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "5");
            map.put("show_id", show_id);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            like_handler.sendMessage(msg);
        }
    };

    Handler like_handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(showDetail.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    List<LikeEntity> likelist = JsonTools.getLikeInfo("result", result);
                    if (likelist != null && !likelist.equals("")) {
                        show_view.setVisibility(View.VISIBLE);
                        ll_show_like.setVisibility(View.VISIBLE);
                    }
                    gv_adapter = new ShowDetailGridViewAdapter(showDetail.this, likelist, likelist.size());
                    gv_like.setAdapter(gv_adapter);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    //评论信息
    Runnable comment_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "4");
            map.put("show_id", show_id);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            comment_handler.sendMessage(msg);
        }
    };

    Handler comment_handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(showDetail.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    commentlist = JsonTools.getCommentInfo("result", result);
                    list.addAll(commentlist);
                    lv_adapter = new ShowDetailCommentAdpter(showDetail.this, commentlist, lv_comment);
                    if (commentlist != null && commentlist.size() != 0) {
                        comment_text.setVisibility(View.GONE);
                    }
                    lv_comment.setAdapter(lv_adapter);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };


    //发布评论
    Runnable comment_show_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "7");
            map.put("show_id", show_id);
            map.put("user_id", ContentCommon.user_id);
            map.put("comment_content", et_show_comment.getText().toString());
            map.put("comment_userid", comment_userid);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            comment_show_handler.sendMessage(msg);
        }
    };

    Handler comment_show_handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(showDetail.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int state = JsonTools.getState("state", result);
                    String datas = JsonTools.getDatas(result);
                    if (state == 1) {
                        Toast.makeText(showDetail.this, "评论成功！", Toast.LENGTH_SHORT).show();

                        UserDao dao = new UserDao(showDetail.this);
                        UserEntiy modler = dao.find(ContentCommon.user_id);
                        String time_now = DateUtils.getCurrentDate();

                        CommentEntity info = new CommentEntity();
                        info.setComment_id(datas);
                        info.setUser_name(modler.getUser_name());
                        info.setUser_header(modler.getUser_header());
                        info.setComment_time(time_now);
                        info.setComment_content(et_show_comment.getText().toString());
                        commentlist.add(0, info);
                        list.add(0, info);
                        lv_adapter.changeCount(commentlist.size());
                        lv_adapter.notifyDataSetChanged();
                        et_show_comment.setText("");
                        comment_text.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(showDetail.this, "评论失败！", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };

    //删除show
    Runnable delete_show_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "1");
            map.put("show_id", show_id);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            delete_show_handler.sendMessage(msg);
        }
    };

    Handler delete_show_handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("1")) {
                Toast.makeText(showDetail.this, "删除成功！", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent("refreshShow");
                LocalBroadcastManager broadcastManager=LocalBroadcastManager.getInstance(showDetail.this);
                broadcastManager.sendBroadcast(intent);
                finish();
            } else {
                Toast.makeText(showDetail.this, "删除失败！", Toast.LENGTH_SHORT).show();
            }

        }
    };

    //删除评论
    Runnable delete_comment_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "9");
            map.put("comment_id", comment_id);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            delete_comment_handler.sendMessage(msg);
        }
    };

    Handler delete_comment_handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            try {
                int state = JsonTools.getState("state",result);
                if (state == 1) {
                    list.remove(comment_position);
                    commentlist.clear();
                    commentlist.addAll(list);
                    lv_adapter.changeCount(commentlist.size());
                    lv_adapter.notifyDataSetChanged();
                    Toast.makeText(showDetail.this, "删除成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(showDetail.this, "删除失败！", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };


    //Edittext 点击空白位置隐藏软键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    // 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    //多种隐藏软件盘方法的其中一种
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
