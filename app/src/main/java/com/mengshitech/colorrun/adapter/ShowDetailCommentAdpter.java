package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.CommentEntity;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.GlideCircleTransform;

import java.util.List;


/**
 * Created by atenklsy on 2016/7/16 16:56.
 * E-address:atenk@qq.com.
 */
public class ShowDetailCommentAdpter extends BaseAdapter{
    List<CommentEntity> mCommentList;
    ListView mCommentListView;
    Activity mActivity;

    public ShowDetailCommentAdpter(Activity activity, List<CommentEntity> commentList, ListView listView) {
        mCommentList = commentList;
        mCommentListView = listView;
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return mCommentList.size();
    }

    @Override
    public CommentEntity getItem(int position) {
        return mCommentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentEntity mCommentEntity = getItem(position);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.show_detail_comment_listview, null);
            holder = new ViewHolder();
            holder.ivUserHead = (ImageView) convertView.findViewById(R.id.ivComment_UserHead);
            holder.tvUserName = (TextView) convertView.findViewById(R.id.tvComment_UserName);
            holder.tvSendTime = (TextView) convertView.findViewById(R.id.tvComment_SendTime);
            holder.tvCommentContent = (TextView) convertView.findViewById(R.id.tvComment_Content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvUserName.setText(mCommentEntity.getUser_name());
        holder.tvSendTime.setText(mCommentEntity.getComment_time());
        holder.tvCommentContent.setText(mCommentEntity.getComment_content());

        String header_path = ContentCommon.path + mCommentEntity.getUser_header();
        Log.i("header_path:", header_path);
        Glide.with(mActivity).load(header_path).transform(new GlideCircleTransform(mActivity)).into(holder.ivUserHead);


        return convertView;
    }

    class ViewHolder {
        ImageView ivUserHead;
        TextView tvUserName;
        TextView tvSendTime;
        TextView tvCommentContent;
    }
}
