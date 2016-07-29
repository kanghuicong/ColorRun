package com.mengshitech.colorrun.utils;
//有问题发邮箱:wschenyongyin@qq.com
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;



import android.util.Log;

public class upload {
	private static String requestURL = "http://xxxx这里为你的ip地址xxxx:8080/ServletForUpload/servlet/ImageUploadServlet"; // 服务器的URL
	private static final String CHARSET = "UTF-8"; // 设置编码
	private int readTimeOut = 10 * 1000; // 读取超时
	private int connectTimeout = 10 * 1000; // 超时时间
	private int mStatus; // 返回状态码


	public synchronized void postMethod(List<String> imageUrlList) {
		try {
			String[] filePath = new String[imageUrlList.size()];

			int size = imageUrlList.size();
			for (int i = 0; i < size; i++) {
				filePath[i] = imageUrlList.get(i);
			}

			// 链接超时，请求超时设置
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					connectTimeout);
			HttpConnectionParams.setSoTimeout(httpParams, readTimeOut);

			// 请求参数设置
			HttpClient client = new DefaultHttpClient(httpParams);
			HttpPost post = new HttpPost(requestURL);
			MultipartEntity entity = new MultipartEntity();

			

			// 
			for (String p : filePath) {
				entity.addPart("fileimg", new FileBody(new File(p), "image/*"));
			}

			post.setEntity(entity);
			HttpResponse resp = client.execute(post);
			// 获取回调值
			Log.e("", EntityUtils.toString(resp.getEntity()));
			mStatus = resp.getStatusLine().getStatusCode();
			Log.e("返回的值为", mStatus + "");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
