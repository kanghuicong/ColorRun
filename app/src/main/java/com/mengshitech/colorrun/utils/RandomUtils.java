package com.mengshitech.colorrun.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 *@Project: LeRun
 *@Author: wschenyongyin
 *@Date: 2016年8月10日
 *@explain:
 *@TestState:
 */

public class RandomUtils {
	
	public RandomUtils(){}
	
	//产生特定范围内的随机整数
	public static int getRandomInt(){
		Random random=new Random();
		int randNum = random.nextInt(1000000000-1)+1;
		return randNum;
	}
	
	//产生特定长度的字符串
	public static String getRandomString(int length) { // length表示生成字符串的长度

		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	//产生随机的订单号
	public static String LerunOrderId(){

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(date);
		String ran=getRandomInt()+"";
		String LerunOrderId=time+ran;
		Log.i("order_id",LerunOrderId+"ss");
		return LerunOrderId;
	}
	public static String Randompictrue(){

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(date);
		Random random=new Random();
		int randNum = random.nextInt(100000-1)+1;
		String Randompictrue=time+randNum;
//		Log.i("order_id",LerunOrderId+"ss");
		return Randompictrue;
	}



}
