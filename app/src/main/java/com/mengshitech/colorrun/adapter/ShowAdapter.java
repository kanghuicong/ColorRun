package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
    String like_state,index;
    private Context context;
    int count;
    ImageView image;
    TextView text;
    String like_num;
    int yes_state=5,no_state=5;
    Map<Integer, Boolean> isCheckMap =  new HashMap<Integer, Boolean>();

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
//        Log.i("123", "getCount: " + count);
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

        like_state = mShowEntity.getLike_state();
        Log.i("77777777777like_state",like_state);

        if (like_state.equals("0")){
            holder.show_like.setBackgroundResource(R.mipmap.show_heart_no);
        }else if (like_state.equals("1")){
            holder.show_like.setBackgroundResource(R.mipmap.show_heart);
        }

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

        final TextView number = holder.show_like_num;
        holder.show_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    image = (ImageView) v.findViewById(R.id.im_show_like);
                    String state = like_state;
                    if (ContentCommon.user_id == null) {
                        Toast.makeText(context, "请先登录...", Toast.LENGTH_SHORT).show();
                    } else {
                        if (state.equals("0")) {
                            index = "8";
                            new Thread(runnable).start();
                            if (yes_state == 1 || yes_state==5) {
                                Log.i("lalala","lalalalalla");
                                number.setText(Integer.valueOf(number.getText().toString()) + 1 + "");
                                mShowEntity.setLike_num(number.getText().toString());
                            }else {
                                Log.i("qqqqqq",yes_state+"");
                                Toast.makeText(context,"11111111111",Toast.LENGTH_SHORT).show();
                            }
                        } else if (state.equals("1")) {
                            index = "6";
                            new Thread(runnable).start();
                            if (no_state == 1 || no_state == 5) {
                                Log.i("hahaha","hahahahaha");
                                number.setText(Integer.valueOf(number.getText().toString()) - 1 + "");
                                mShowEntity.setLike_num(number.getText().toString());
                            }else {
                                Log.i("ppppppp",no_state+"");
                                Toast.makeText(context,"222222222222",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            }
        });

        holder.show_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContentCommon.user_id == null) {
                    Toast.makeText(context, "请先登录...", Toast.LENGTH_SHORT).show();
                } else {
                    OnekeyShare onkeyShare =new OnekeyShare();
                    onkeyShare.disableSSOWhenAuthorize();
                    onkeyShare.setTitle(mShowEntity.getUser_name()+"的卡乐彩色跑");
                    onkeyShare.setText(mShowEntity.getShow_content());

                    onkeyShare.setImageUrl(ImageList.get(1));
                    onkeyShare.setUrl("http://www.roay.cn/");
                    //显示分享列表

                    onkeyShare.show(context);
                }
            }
        });
//        holder.show_like.setOnClickListener(this);
        holder.show_comment.setOnClickListener(this);
//        holder.show_share.setOnClickListener(this);
        return view;
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
//            case R.id.im_show_share:
//                if (ContentCommon.user_id == null) {
//                    Toast.makeText(context, "请先登录...", Toast.LENGTH_SHORT).show();
//                } else {
//                    OnekeyShare onkeyShare =new OnekeyShare();
//                    onkeyShare.disableSSOWhenAuthorize();
//                    onkeyShare.setTitle("卡乐 越运动越青春");
//                    onkeyShare.setText("On The Way");
//
//                    onkeyShare.setImageUrl("http://www.roay.cn/uploads/160804/1-160P4114125948.png");
//                    onkeyShare.setUrl("http://www.roay.cn/");
//                    //显示分享列表
//
//                    onkeyShare.show(context);
//                }
//                break;
            default:
                break;
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", index);
            map.put("user_id", ContentCommon.user_id);
            map.put("like_userid", mShowEntity.getUser_id());
            map.put("show_id", mShowEntity.getShow_id());

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Log.i("result", result);
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }

    };


        Handler handler = new Handler() {

            public void handleMessage(Message msg) {
                String result = (String) msg.obj;

                Log.i("result111", result);
                if (result.equals("timeout")) {
//                progressDialog.dismiss();
                    Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
                } else {
//                progressDialog.dismiss();
                    if (index.equals("8")) {
                        try {
                            Log.i("点赞时状态", like_state);
                            yes_state = JsonTools.getState("state", result);
                            Log.i("点赞返回状态", yes_state + "");
                            if (yes_state == 1) {
                                like_state = "1";
                                mShowEntity.setLike_state(like_state);
                                image.setBackgroundResource(R.mipmap.show_heart);
//                                number.setText(Integer.valueOf(number) + 1 + "");
                            } else {
                                Toast.makeText(context, "点赞失败...", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }else if (index.equals("6")){
                        try {
                            Log.i("取消赞时状态",like_state);
                            no_state = JsonTools.getState("state",result);
                            Log.i("取消赞返回状态",no_state+"");

                            if (no_state==1){
                                like_state = "0";
                                mShowEntity.setLike_state(like_state);
                                image.setBackgroundResource(R.mipmap.show_heart_no);
//                                number.setText(Integer.valueOf(number)-1+"");
                            }else {
                                Toast.makeText(context,"取消赞失败...",Toast.LENGTH_SHORT).show();
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

