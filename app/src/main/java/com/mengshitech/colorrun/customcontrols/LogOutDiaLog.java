package com.mengshitech.colorrun.customcontrols;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mengshitech.colorrun.R;

public class LogOutDiaLog extends Dialog implements
		View.OnClickListener {
	int layout;// 自定义的布局
	Context context;// 上下文联系
	int theme;// dialog主题
	View customview;
	private LeaveMyDialogListener listener;

	public LogOutDiaLog(Context context) {
		super(context);
		this.context = context;
	}

	// // 传入布局，activity
	public LogOutDiaLog(Context context, int layout) {

		super(context, layout);
		this.context = context;
		this.layout = layout;
		customview = View.inflate(context, layout, null);
	}

	// 传入布局，activity，主题
	public LogOutDiaLog(Context context, int layout, int theme,
						LeaveMyDialogListener listener) {
		super(context, theme);
		this.context = context;
		this.theme = theme;
		this.layout = layout;
		this.listener = listener;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layout);


		Button btn_logout = (Button) findViewById(R.id.btn_logout);

		Button btn_cancel = (Button) findViewById(R.id.btn_cancel);

		btn_logout.setOnClickListener(this);

		btn_cancel.setOnClickListener(this);

	}

	public interface LeaveMyDialogListener {
		public void onClick(View view);
	}

	@Override
	public void onClick(View v) {
		listener.onClick(v);

	}

}
