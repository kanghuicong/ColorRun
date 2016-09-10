package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.fragment.me.PersonDetail;
import com.mengshitech.colorrun.fragment.show.showDetail;
import com.mengshitech.colorrun.utils.DateUtils;
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.JustifyText;
import com.mengshitech.colorrun.utils.UtilsClick;
import com.mengshitech.colorrun.view.EmptyGridView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * atenklsy
 */
public class ShowAdapter extends BaseAdapter {
    FragmentManager fm;
    ViewHolder holder;
    //    View view;
    List<String> ImageList, share_list;
    List<String> imagepath = new ArrayList<String>();
    ListView mListView;
    ShowEntity mShowEntity;
    List<ShowEntity> mShowList;
    String index;
    Context context;
    int count;
    TextView show_like;
    int yes_state, no_state;
    int like_pos, share_pos;
    String sta;
    List<String> list = new ArrayList<String>();
    Activity activity;
    String glidviewImagelist;


    private static class ViewHolder {
        private ImageView user_header;
        private TextView user_name;
        private EmptyGridView show_image;
        private JustifyText show_content;
        private TextView show_time;
        private TextView show_like;
        private TextView show_comment;
        private TextView show_share;
    }

    public ShowAdapter(int count, Context context, FragmentManager fm, List<ShowEntity> showList,
                       ListView mListView) {
        this.count = count;
        this.context = context;
        this.mShowList = showList;
        this.fm = fm;
        this.mListView = mListView;
    }

    public ShowAdapter(int count, Activity activity, Context context, FragmentManager fm, List<ShowEntity> showList,
                       ListView mListView) {
        this.count = count;
        this.context = context;
        this.mShowList = showList;
        this.fm = fm;
        this.mListView = mListView;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public ShowEntity getItem(int position) {
        return mShowList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ShareSDK.initSDK(parent.getContext());
        Log.i("456", "getView: " + imagepath.size());
        mShowEntity = mShowList.get(position);
        holder = new ViewHolder();
        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = inflater.inflate(R.layout.show_listview, null);
            convertView = View.inflate(context, R.layout.show_listview, null);
            holder.user_header = (ImageView) convertView
                    .findViewById(R.id.im_show_userhear);
            holder.user_name = (TextView) convertView
                    .findViewById(R.id.tv_show_username);
            holder.show_image = (EmptyGridView) convertView
                    .findViewById(R.id.gv_show_image);
            holder.show_content = (JustifyText) convertView
                    .findViewById(R.id.tv_show_content);
            holder.show_time = (TextView) convertView
                    .findViewById(R.id.tv_show_time);
            holder.show_comment = (TextView) convertView.findViewById(R.id.im_show_comment);
            holder.show_like = (TextView) convertView.findViewById(R.id.tv_show_like);
            holder.show_share = (TextView) convertView.findViewById(R.id.im_show_share);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //头像
        String header_path = ContentCommon.path + mShowEntity.getUser_header();
        Glide.with(context).load(header_path).transform(new GlideCircleTransform(context)).error(R.mipmap.default_avtar).into(holder.user_header);

        //读取基本数据
        holder.user_name.setText(mShowEntity.getUser_name());
        holder.show_comment.setText(mShowEntity.getComment_num());
        holder.show_like.setText(mShowEntity.getLike_num());
        if (mShowEntity.getShow_content() != null && !mShowEntity.getShow_content().equals("")) {
            holder.show_content.setVisibility(View.VISIBLE);
            holder.show_content.setText(mShowEntity.getShow_content());
        } else {
            holder.show_content.setVisibility(View.GONE);
        }

        //发布show的时间
        String time_unit;
        time_unit = DateUtils.getDate(mShowEntity.getShow_time());
        holder.show_time.setText(time_unit);

        //根据like_state判断爱心初始状态
        String like_state = mShowEntity.getLike_state();
        //list保存点赞状态
        list.add(position, like_state);
        if (like_state.equals("0")) {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.show_heart_no);
            holder.show_like.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        } else if (like_state.equals("1")) {
            Drawable drawable = context.getResources().getDrawable(R.mipmap.show_heart);

            holder.show_like.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);

        }

        //设置图片大小
        Drawable drawable_share = context.getResources().getDrawable(R.mipmap.show_share);
//        drawable_share.setBounds(0, 0, 45, 45);//必须设置图片大小，否则不显示
        holder.show_share.setCompoundDrawablesWithIntrinsicBounds(drawable_share, null, null, null);
        Drawable drawable_comment = context.getResources().getDrawable(R.mipmap.show_comment);
//        drawable_comment.setBounds(0, 0, 45, 45);//必须设置图片大小，否则不显示
        holder.show_comment.setCompoundDrawablesWithIntrinsicBounds(drawable_comment, null, null, null);

        //gridview图片
        glidviewImagelist = mShowEntity.getShow_image();

        if (glidviewImagelist == null || glidviewImagelist.equals("")) {
            holder.show_image.setVisibility(View.GONE);
            Log.e("glidviewHandler", "2");
        } else {
            holder.show_image.setVisibility(View.VISIBLE);
            try {
//                    Log.i("Show_image", position + "");
                Log.e("glidviewHandler", "3");
                ImageList = JsonTools.getImageInfo(glidviewImagelist);
                imagepath = new ArrayList<String>();
                for (int i = 0; i < ImageList.size(); i++) {
                    String paths = ImageList.get(i);
                    imagepath.add(paths);
                    ShowGridViewAdapter adapter = new ShowGridViewAdapter(context, activity, imagepath, imagepath.size());
                    holder.show_image.setAdapter(adapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //gridview空白部分点击事件
        holder.show_image.setOnTouchInvalidPositionListener(new EmptyGridView.OnTouchInvalidPositionListener() {
            @Override
            public boolean onTouchInvalidPosition(int motionEvent) {
                return false; //不终止路由事件让父级控件处理事件
            }
        });
        holder.user_header.setOnClickListener(new PersonListener(position));
        holder.show_like.setOnClickListener(new LikeListener(position));
        holder.show_share.setOnClickListener(new ShareListener(position));
        holder.show_comment.setOnClickListener(new CommentListener(position));
        return convertView;
    }

    class PersonListener implements View.OnClickListener{
        private int position;
        public PersonListener(int pos){
            position = pos;
        }
        @Override
        public void onClick(View v) {
            ShowEntity showEntity = mShowList.get(position);
            if ( !ContentCommon.user_id .equals(showEntity.getUser_id())  && ContentCommon.user_id !=null) {
                Intent intent = new Intent(context, PersonDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("user_id", showEntity.getUser_id());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        }
    }

    class LikeListener implements View.OnClickListener {
        private int position;
        public LikeListener(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            like_pos = position;
            if (ContentCommon.login_state.equals("1")) {
                show_like = (TextView) v.findViewById(R.id.tv_show_like);
                if (UtilsClick.isFastClick(500)) {
                    return;
                } else {
                    if (list.get(like_pos).equals("0")) {
                        index = "8";
                        new Thread(runnable).start();
                    } else if (list.get(like_pos).equals("1")) {
                        index = "6";
                        new Thread(runnable).start();
                    }
                }
            } else {
                Toast.makeText(context, "请先登录...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class ShareListener implements View.OnClickListener {
        int position;

        public ShareListener(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            share_pos = position;
            if (ContentCommon.user_id == null) {
                Toast.makeText(context, "请先登录...", Toast.LENGTH_SHORT).show();
            } else {
                OnekeyShare onkeyShare = new OnekeyShare();
                onkeyShare.disableSSOWhenAuthorize();
                ShowEntity showEntity = mShowList.get(share_pos);
                onkeyShare.setTitle(showEntity.getUser_name() + "的卡乐彩色跑");
                onkeyShare.setText(showEntity.getShow_content());
//                DisplayMetrics dm = context.getResources().getDisplayMetrics();
//                onkeyShare.setHeight(dm.heightPixels/16);

                mShowEntity = mShowList.get(share_pos);
                String share_result = mShowEntity.getShow_image();
                try {
                    share_list = JsonTools.getImageInfo(share_result);
                    if (share_list.size() != 0) {
                        onkeyShare.setImageUrl(share_list.get(0));
                    }
                    onkeyShare.setUrl("http://www.roay.cn/");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                onkeyShare.show(context);

            }
        }
    }

    class CommentListener implements View.OnClickListener {
        int position;

        public CommentListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ShowEntity showEntity = mShowList.get(position);
            Intent intent = new Intent(context, showDetail.class);
            Bundle bundle = new Bundle();
            bundle.putString("show_id", showEntity.getShow_id());
            bundle.putString("comment_userid", showEntity.getUser_id());
            bundle.putString("user_name", showEntity.getUser_name());
            bundle.putString("show_content", showEntity.getShow_content());
            bundle.putString("show_time", showEntity.getShow_time());
            bundle.putString("show_comment_num", showEntity.getComment_num());
            bundle.putString("show_like_num", showEntity.getLike_num());
            bundle.putString("user_header", showEntity.getUser_header());
            bundle.putString("show_image", showEntity.getShow_image());
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    public void addItem(int item) {
        count = item;
    }


    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            ShowEntity showEntity = mShowList.get(like_pos);
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", index);
            map.put("user_id", ContentCommon.user_id);
            map.put("like_userid", showEntity.getUser_id());
            map.put("show_id", showEntity.getShow_id());
            Log.i("mShowEntitygetShow_id", showEntity.getShow_id());

            String result = HttpUtils.sendHttpClientPost(path, map, "utf-8");
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);

        }
    };

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            if (result.equals("timeout")) {
                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {

                if (index.equals("8")) {
                    try {
                        yes_state = JsonTools.getState("state", result);
                        if (yes_state == 1) {
                            list.set(like_pos, "1");
                            mShowEntity = mShowList.get(like_pos);
                            show_like.setText(Integer.valueOf(show_like.getText().toString()) + 1 + "");
                            mShowEntity.setLike_state("1");
                            mShowEntity.setLike_num(show_like.getText().toString());
                            Drawable drawable = context.getResources().getDrawable(R.mipmap.show_heart);
                            show_like.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                        } else {
                            Toast.makeText(context, "点赞失败...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                } else if (index.equals("6")) {
                    try {
                        no_state = JsonTools.getState("state", result);
                        if (no_state == 1) {
                            list.set(like_pos, "0");
                            mShowEntity = mShowList.get(like_pos);
                            show_like.setText(Integer.valueOf(show_like.getText().toString()) - 1 + "");
                            mShowEntity.setLike_state("0");
                            mShowEntity.setLike_num(show_like.getText().toString());
                            Drawable drawable = context.getResources().getDrawable(R.mipmap.show_heart_no);
                            show_like.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                        } else {
                            Toast.makeText(context, "取消赞失败...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }
    };





}

