package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.ArrayMap;
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
import com.mengshitech.colorrun.utils.GlideCircleTransform;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * atenklsy
 */
public class ShowAdapter extends BaseAdapter implements View.OnClickListener {
    FragmentManager fm;
    ViewHolder holder;
    View view;
    List<String> ImageList;
    List<String> imagepath = new ArrayList<String>();
    ListView mListView;
    ShowEntity mShowEntity;
    List<ShowEntity> mShowList;
    String index;
    private Context context;
    int count;
    TextView show_like;
    int yes_state, no_state;
    int like_pos,share_pos;
    String sta;
    List<String> list = new ArrayList<String>();


    private static class ViewHolder {
        private ImageView user_header;
        private TextView user_name;
        private GridView show_image;
        private TextView show_content;
        private TextView show_time;
        private TextView show_comment_num;
        private TextView show_like;
        private ImageView show_comment;
        private ImageView show_share;
    }

    public ShowAdapter(int count, Context context, FragmentManager fm, List<ShowEntity> showList,
                       ListView mListView) {
        this.count = count;
        this.context = context;
        this.mShowList = showList;
        this.fm = fm;
        this.mListView = mListView;
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
    public View getView(final int position, final View convertView, ViewGroup parent) {
        ShareSDK.initSDK(parent.getContext());
        Log.i("456", "getView: " + imagepath.size());
        mShowEntity = mShowList.get(position);
        holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.show_listview, null);

            holder = new ViewHolder();
            holder.user_header = (ImageView) view
                    .findViewById(R.id.im_show_userhear);
            holder.user_name = (TextView) view
                    .findViewById(R.id.tv_show_username);
            holder.show_image = (GridView) view
                    .findViewById(R.id.gv_show_image);
            holder.show_content = (TextView) view
                    .findViewById(R.id.tv_show_content);
            holder.show_time = (TextView) view
                    .findViewById(R.id.tv_show_time);
            holder.show_comment_num = (TextView) view
                    .findViewById(R.id.tv_show_comment_num);
            holder.show_comment = (ImageView) view.findViewById(R.id.im_show_comment);
            holder.show_like = (TextView) view.findViewById(R.id.tv_show_like);
            holder.show_share = (ImageView) view.findViewById(R.id.im_show_share);

            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        String header_path = ContentCommon.path + mShowEntity.getUser_header();
        Glide.with(context).load(header_path).transform(new GlideCircleTransform(context)).into(holder.user_header);
        //读取基本数据
        holder.user_name.setText(mShowEntity.getUser_name());
        holder.show_content.setText(mShowEntity.getShow_content());
        holder.show_time.setText(mShowEntity.getShow_time());
        holder.show_comment_num.setText(mShowEntity.getComment_num());
        holder.show_like.setText(mShowEntity.getLike_num());

        String like_state = mShowEntity.getLike_state();
        list.add(like_state);
        //根据like_state判断爱心初始状态
        if (like_state.equals("0")) {
            Log.i("state初始状态0====", like_state + "");
            Drawable drawable = context.getResources().getDrawable(R.mipmap.show_heart_no);
            drawable.setBounds(0, 0, 40, 40);//必须设置图片大小，否则不显示
            holder.show_like.setCompoundDrawables(drawable, null, null, null);
        } else if (like_state.equals("1")) {
            Log.i("state初始状态1====", like_state + "");
            Drawable drawable = context.getResources().getDrawable(R.mipmap.show_heart);
            drawable.setBounds(0, 0, 40, 40);//必须设置图片大小，否则不显示
            holder.show_like.setCompoundDrawables(drawable, null, null, null);
        }

        //gridview图片
        String result = mShowEntity.getShow_image();
        try {
            ImageList = JsonTools.getImageInfo(result);
            imagepath = new ArrayList<String>();
            for (int i = 0; i < ImageList.size(); i++) {
                String paths = ImageList.get(i);
                System.out.println("list d  chang du1111111111111111111  " + ImageList.size());
                imagepath.add(paths);
                ShowGridViewAdapter adapter = new ShowGridViewAdapter(context, imagepath, imagepath.size());
                System.out.println("list d  chang du2222222222222222222  " + imagepath.size());
                holder.show_image.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        holder.show_like.setOnClickListener(new LikeListener(position, like_state));

        holder.show_share.setOnClickListener(new ShareListener(position) );
        holder.show_comment.setOnClickListener(this);
        return view;
    }

    class LikeListener implements View.OnClickListener {

        private int position;
        private String like_state;

        public LikeListener(int pos, String state) {
            position = pos;
            like_state = state;
        }

        @Override
        public void onClick(View v) {
            like_pos = position;
            if (ContentCommon.user_id == null) {
                Toast.makeText(context, "请先登录...", Toast.LENGTH_SHORT).show();
            } else {
                show_like = (TextView) v.findViewById(R.id.tv_show_like);
                Log.i("state点击时状态", like_state + "");
                if (list.get(like_pos).equals("0")) {
                    index = "8";
                    new Thread(runnable).start();
                } else if (list.get(like_pos).equals("1")) {
                    index = "6";
                    new Thread(runnable).start();
                }
            }
        }
    }

    class ShareListener implements View.OnClickListener{
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
                OnekeyShare onkeyShare =new OnekeyShare();
                onkeyShare.disableSSOWhenAuthorize();
                ShowEntity showEntity = mShowList.get(share_pos);
                onkeyShare.setTitle(showEntity.getUser_name()+"的卡乐彩色跑");
                onkeyShare.setText(showEntity.getShow_content());

                onkeyShare.setImageUrl(ImageList.get(1));
                onkeyShare.setUrl("http://www.roay.cn/");
                //显示分享列表

                onkeyShare.show(context);
            }
        }
    }

    public void addItem(int item) {
        count = item;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_show_comment:
                Toast.makeText(context, "comment", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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
                        Log.i("state点赞返回状态", yes_state + "");
                        if (yes_state == 1) {
                            list.set(like_pos,"1");
                            mShowEntity = mShowList.get(like_pos);
                            show_like.setText(Integer.valueOf(show_like.getText().toString()) + 1 + "");
                            mShowEntity.setLike_state("1");
                            mShowEntity.setLike_num(show_like.getText().toString());
                            Drawable drawable = context.getResources().getDrawable(R.mipmap.show_heart);
                            drawable.setBounds(0, 0, 40, 40);//必须设置图片大小，否则不显示
                            show_like.setCompoundDrawables(drawable, null, null, null);
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
                        Log.i("state取消赞返回状态", no_state + "");
                        if (no_state == 1) {
                            list.set(like_pos,"0");
                            mShowEntity = mShowList.get(like_pos);
                            show_like.setText(Integer.valueOf(show_like.getText().toString()) - 1 + "");
                            mShowEntity.setLike_state("0");
                            mShowEntity.setLike_num(show_like.getText().toString());
                            Drawable drawable = context.getResources().getDrawable(R.mipmap.show_heart_no);
                            drawable.setBounds(0, 0, 40, 40);//必须设置图片大小，否则不显示
                            show_like.setCompoundDrawables(drawable, null, null, null);
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

