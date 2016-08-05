package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
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
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    String like_state;

    private Context context;
    int count;

    private static class ViewHolder {
        private ImageView user_header;
        private TextView user_name;
        private GridView show_image;
        private TextView show_content;
        private TextView show_time;
        private TextView show_like_num;
        private TextView show_comment_num;
        private ImageView show_like;
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
        Log.i("123", "getCount: " + count);
        return count;
    }

    public int getChildCount(int childCount) {
        return 1;
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
            holder.show_like_num = (TextView) view
                    .findViewById(R.id.tv_show_like_num);
            holder.show_comment_num = (TextView) view
                    .findViewById(R.id.tv_show_comment_num);
            holder.show_comment = (ImageView) view.findViewById(R.id.im_show_comment);
            holder.show_like = (ImageView) view.findViewById(R.id.im_show_like);
            holder.show_share = (ImageView) view.findViewById(R.id.im_show_share);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }


        String header_path = ContentCommon.path + mShowEntity.getUser_header();
        Log.i("header_path:", header_path);
        Glide.with(context).load(header_path).into(holder.user_header);

        holder.user_name.setText(mShowEntity.getUser_name());
        holder.show_content.setText(mShowEntity.getShow_content());
        holder.show_time.setText(mShowEntity.getShow_time());
        holder.show_comment_num.setText(mShowEntity.getComment_num());
        holder.show_like_num.setText(mShowEntity.getLike_num());

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

        /**
         * 改变drawable的大小
         */
        holder.show_like.setOnClickListener(this);
        holder.show_comment.setOnClickListener(this);
        holder.show_share.setOnClickListener(this);
        return view;
    }


    public void addItem(int item) {
        count = item;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_show_like:
                Toast.makeText(context, "like", Toast.LENGTH_SHORT).show();
                like_state = mShowEntity.getLike_state();
                if (like_state.equals("0")) {
                    new Thread(like_runnable).start();
                } else if (like_state.equals("1")) {
                    new Thread(cancellike_runnable).start();
                }
                break;
            case R.id.im_show_comment:
                Toast.makeText(context, "comment", Toast.LENGTH_SHORT).show();
                break;
            case R.id.im_show_share:
                Toast.makeText(context, "share", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    Runnable like_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            SharedPreferences sharedPreferences = context
                    .getSharedPreferences("user_type", Activity.MODE_PRIVATE);
            String id = sharedPreferences.getString("user_id", "");
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "8");
            map.put("user_id", id);
            map.put("like_userid", mShowEntity.getUser_id());
            map.put("show_id", mShowEntity.getShow_id());

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Log.i("result", result);
            Message msg = new Message();
            msg.obj = result;
            like_handler.sendMessage(msg);
        }

    };

        Handler like_handler = new Handler() {

            public void handleMessage(Message msg) {
                String result = (String) msg.obj;

                Log.i("result111", result);
                if (result.equals("timeout")) {
//                progressDialog.dismiss();
                    Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
                } else {
//                progressDialog.dismiss();
                    try {
                        int state = JsonTools.getState("state",result);
                        like_state = state+"";
                        Log.i("state", state+"");
                        holder.show_like.setImageResource(R.mipmap.show_heart);
                        holder.show_like_num.setText(Integer.valueOf(holder.show_like_num.getText().toString()) + 1 + "");
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };




    Runnable cancellike_runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            SharedPreferences sharedPreferences = context
                    .getSharedPreferences("user_type", Activity.MODE_PRIVATE);
            String id = sharedPreferences.getString("user_id", "");
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "6");
            map.put("user_id", id);
            map.put("show_id", mShowEntity.getShow_id());

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Log.i("result", result);
            Message msg = new Message();
            msg.obj = result;
            cancellike_handler.sendMessage(msg);
        }

    };

    Handler cancellike_handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            Log.i("result111", result);
            if (result.equals("timeout")) {
//                progressDialog.dismiss();
                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
//                progressDialog.dismiss();
                try {
                    int state = JsonTools.getState("state",result);
                    like_state = state+"";
                    Log.i("like_state", state+"");
                    holder.show_like.setImageResource(R.mipmap.show_heart_no);
                    holder.show_like_num.setText(Integer.valueOf(holder.show_like_num.getText().toString()) - 1 + "");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    };
}

