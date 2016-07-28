package com.mengshitech.colorrun.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.HistoryEntity;
import com.mengshitech.colorrun.fragment.history.mHistoryDetailFragment;

import java.util.List;
/**
 * atenklsy
 */
public class HistoryAdapter extends BaseAdapter {
	private Activity mActivity;
	List<HistoryEntity> mHistotyList;
	ListView mListView;

	public HistoryAdapter(Activity activity, List<HistoryEntity> historyList,
			ListView listView) {
		mActivity = activity;
		mHistotyList = historyList;
		mListView = listView;
	}

	@Override
	public int getCount() {
		return 10;
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
	public View getView(int position, View convertView, ViewGroup parent) {
//		HistoryEntity mHistoryEntity = getItem(position);
		// 上面是该了getItem的方法，让她返回一个HistoryEntity类型的对象
		// HistoryEntity mHistoryEntity = mHistotyList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mActivity, R.layout.history_listview, null);
			holder = new ViewHolder();
			holder.ll_history = (LinearLayout) convertView
					.findViewById(R.id.ll_hisrory);
			holder.history_name = (TextView) convertView
					.findViewById(R.id.tv_history_name);
			holder.history_time = (TextView) convertView
					.findViewById(R.id.tv_history_time);
			holder.history_address = (TextView) convertView
					.findViewById(R.id.tv_history_address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mHistoryDetailFragment HistoryDetailFragment = new mHistoryDetailFragment();
			}
		});
		return convertView;
	}

	class ViewHolder {
		LinearLayout ll_history;
		TextView history_name;
		TextView history_time;
		TextView history_address;
	}

}
