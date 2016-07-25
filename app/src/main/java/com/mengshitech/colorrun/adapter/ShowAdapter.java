package com.mengshitech.colorrun.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.ImageEntity;
import com.mengshitech.colorrun.bean.ShowEntity;
import com.mengshitech.colorrun.fragment.show.showDetailFragment;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * atenklsy
 */
public class ShowAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, View.OnClickListener {
    FragmentManager fm;
    ViewHolder holder;
    View view;
    List<String> ImageList;
    List<String> imagepath = new ArrayList<String>();
    ListView mListView;
    ShowEntity mShowEntity;
    List<ShowEntity> mShowList;

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

    public ShowAdapter(int count,Context context,FragmentManager fm, List<ShowEntity> showList,
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
    public int getChildCount(int childCount){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        mShowEntity = mShowList.get(position);
        holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.show_listview,null);

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
            holder.show_comment = (ImageView)view.findViewById(R.id.im_show_comment);
            holder.show_like = (ImageView)view.findViewById(R.id.im_show_like);
            holder.show_share = (ImageView)view.findViewById(R.id.im_show_share) ;
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        String header_path = IPAddress.path+mShowEntity.getUser_header();
        Log.i("header_path:",header_path);
        Glide.with(context).load(header_path).into(holder.user_header);

        holder.user_name.setText(mShowEntity.getUser_name());
        holder.show_content.setText(mShowEntity.getShow_content());
        holder.show_time.setText(mShowEntity.getShow_time());
        holder.show_comment_num.setText(mShowEntity.getComment_num());
        holder.show_like_num.setText(mShowEntity.getLike_num());

        String result = mShowEntity.getShow_image();

        try {
            ImageList = JsonTools.getImageInfo(result);
            for (int i = 0; i < ImageList.size(); i++) {
                String Imagepath = ImageList.get(i);
                System.out.println("list d  chang du1111111111111111111  " + ImageList.size());
                String path = Imagepath;
                imagepath.add(path);
                ShowGridViewAdapter adapter = new ShowGridViewAdapter(context,imagepath,imagepath.size());
                System.out.println("list d  chang du2222222222222222222  " + imagepath.size());
                holder.show_image.setAdapter(adapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * 改变drawable的大小
         */
        mListView.setOnItemClickListener(this);
        holder.show_like.setOnClickListener(this);
        holder.show_comment.setOnClickListener(this);
        holder.show_share.setOnClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showDetailFragment mShowDetailFragment = new showDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("mShowEntity", mShowEntity);
        mShowDetailFragment.setArguments(args);
        Utility.replace2DetailFragment(fm, mShowDetailFragment);
    }
    public void addItem(int item) {
        count = item;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im_show_like:
                Toast.makeText(context, "like", Toast.LENGTH_SHORT).show();
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

}
