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
				// TODO �Զ���ɵķ������
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
							initData();
							// 判断是否有网络
							judgeHandler.sendEmptyMessageDelayed(0,0);
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
			/**
			 * 2、判断是否在sp中是否有账号密码
			 */

			SharedPreferences spAccount = getSharedPreferences("sp_account",
					MODE_PRIVATE);
			String userId = spAccount.getString("userId", null);
			String userPwd = spAccount.getString("userPwd", null);
			// 从sp中取账号密码，没取到则为null
			if (!TextUtils.isEmpty(userId) || !TextUtils.isEmpty(userPwd)) {
				// 如果账号和密码都不为空，则跳转到主页面
				startActivity(new Intent(SplashActivity.this,
						MainActivity.class));
				finish();
			} else {
				// 否则跳转到登录界面
				startActivity(new Intent(SplashActivity.this,
						LoginActivity.class));
				finish();
			}
		}
	};

	private void initData() {
		/**
		 * 1、判断是否有网络
		 */

	}

}
