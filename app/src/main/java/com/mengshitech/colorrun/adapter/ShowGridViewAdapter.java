package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.SpaceImageDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kanghuicong on 2016/7/25  13:41.
 * 515849594@qq.com
 */
public class ShowGridViewAdapter extends BaseAdapter {
    Context context;
    List<String> imagepath = new ArrayList<String>();
    int count;
    DisplayMetrics dm;
    Activity activity;
    int state;

    public ShowGridViewAdapter(Context context, Activity activity, List<String> imagepath, int count) {
        this.context = context;
        this.imagepath = imagepath;
        this.count = count;
        dm = context.getResources().getDisplayMetrics();
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.show_gridview, null);
            holder = new ViewHolder();
            holder.grid_image = (ImageView) convertView.findViewById(R.id.iv_show_gridview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewGroup.LayoutParams ps = holder.grid_image.getLayoutParams();
        ps.height = (dm.widthPixels / 7 * 2);
        holder.grid_image.setLayoutParams(ps);

        final String image_path = imagepath.get(position);


//        Glide.with(context).load(image_path).transform(new GlideRoundTransform(context)).into(holder.grid_image);
//        Glide.with(context).load(image_path).transform(new GlideRoundTransform(context)).error(R.mipmap.defaut_error_square).into(holder.grid_image);
        final ImageLoader imageLoader = ImageLoader.getInstance();
        if(!imageLoader.isInited()) imageLoader.init(ImageLoaderConfiguration.createDefault(context));


        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoader.displayImage(image_path, holder.grid_image, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                holder.grid_image.setImageResource(R.mipmap.defaut_error_square);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            }

            @Override
            public void onLoadingCancelled(String s, View view) {
            }
        });


        holder.grid_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SpaceImageDetailActivity.class);
                intent.putExtra("image_path", image_path);
                intent.putExtra("position", position);
                int[] location = new int[2];
                holder.grid_image.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);
                intent.putExtra("width", holder.grid_image.getWidth());
                intent.putExtra("height", holder.grid_image.getHeight());
                context.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView grid_image;
    }
}
