package com.mengshitech.colorrun.adapter;
//有问题发邮箱:wschenyongyin@qq.com
import java.util.ArrayList;
import java.util.List;



import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mengshitech.colorrun.R;

public class ReleaseShowGridViewAdapter extends BaseAdapter {
	//	private ArrayList<String> listfile = null;
	List<String> listfile=new ArrayList<String>();
	private Context context;
	private Bitmap bitmap;
	private int count;
	BitmapFactory.Options options;

	public ReleaseShowGridViewAdapter(Context context, List<String> listfile,
									  int count, Bitmap bitmap) {
		this.context = context;
		this.listfile = listfile;
		this.count = count;
		this.bitmap = bitmap;
		options = new BitmapFactory.Options();
	}

	public ReleaseShowGridViewAdapter(Context context, Bitmap bitmap, int count) {
		this.context = context;
		this.bitmap = bitmap;
		this.count = count;
		options = new BitmapFactory.Options();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		Bitmap bm = null;

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.gridview_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView
					.findViewById(R.id.gridvieww_image);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (listfile != null) {
			if (position == count - 1) {
				holder.image.setTag(position);
				holder.image.setImageBitmap(bitmap);
			} else {
				String path = listfile.get(position);

				options.inSampleSize = 2;

//				bm=BitmapFactory.decodeStream(path);
				if(bm!=null&&!bm.isRecycled()){
					bm.recycle() ;
					bm=null;
					System.gc();
				}
				bm = BitmapFactory.decodeFile(path,options);
				holder.image.setTag(position);
				holder.image.setImageBitmap(bm);
			}

		} else {
			holder.image.setTag(position);
			holder.image.setImageBitmap(bitmap);
		}

		return convertView;
	}

	private class ViewHolder {
		public ImageView image;

	}

}
