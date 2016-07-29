package com.mengshitech.colorrun.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Window;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.utils.ToolKits;

/**
 * atenklsy
 */
public class SplashActivity extends Activity {
	/**
	 * 3秒闪屏页，用来判断网络、初始化数据、确认用户名是否登录等
	 */
	public static final String IS_FIRST = "is_first";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		initView();
	}

	private void initView() {


		new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message arg0) {

				if (!ToolKits
						.fetchBooble(SplashActivity.this, IS_FIRST, false)) {
					startActivity(new Intent(SplashActivity.this,
							GuideActivity.class));
					ToolKits.putBooble(SplashActivity.this, IS_FIRST, true);
					finish();
				} else {
					new Thread(new Runnable() {

						@Override
						public void run() {
							judgeHandler.sendEmptyMessageDelayed(0,2000);
						}
					}).start();
					finish();
				}
				ToolKits.putBooble(SplashActivity.this, IS_FIRST, true);
				return true;
			}
		}).sendEmptyMessageDelayed(0, 2000);
	}

	private Handler judgeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			// 从sp中取账号密码，没取到则为null
			if (1==1) {
				startActivity(new Intent(SplashActivity.this,
						MainActivity.class));
				finish();
			} else {}
		}
	};
}
