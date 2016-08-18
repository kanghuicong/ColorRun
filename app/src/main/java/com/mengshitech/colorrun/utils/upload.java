package com.mengshitech.colorrun.utils;
//有问题发邮箱:wschenyongyin@qq.com

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


import android.util.Log;
/**
 * 作者：wschenyongyin on 2016/6/16 15:06
 * 说明:上传图片文件
 */
public class upload {

    private static final String CHARSET = "UTF-8"; // 设置编码
    private static int readTimeOut = 60 * 1000; // 读取超时
    private static int connectTimeout = 60 * 1000; // 超时时间
    private static int mStatus; // 返回状态码
    private static final int TIME_OUT = 10 * 1000;
    private static HttpResponse resp;
    private static String result = "ceshi";


    public static synchronized String uploadListImage(List<String> imageUrlList, String ServletPath) throws IOException {
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
            HttpPost post = new HttpPost(ServletPath);
            MultipartEntity entity = new MultipartEntity();


            //
            for (String p : filePath) {
                entity.addPart("fileimg", new FileBody(new File(p), "image/*"));
            }

            post.setEntity(entity);
            resp = client.execute(post);
            // 获取回调值
//            Log.e("udasdasd", EntityUtils.toString(resp.getEntity()));
            mStatus = resp.getStatusLine().getStatusCode();
            if (mStatus == 200) {
                if(entity!=null){
                    result= EntityUtils.toString(resp.getEntity());
                }

            } else {
                result= "failure";
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //传file文件  和servlet地址
    public static String uploadImage(File file, String ServletPath) {

        String BOUNDARY = UUID.randomUUID().toString();  //边界标识   随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";   //内容类型

        try {
            URL url = new URL(ServletPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true);  //允许输入流
            conn.setDoOutput(true); //允许输出流
            conn.setUseCaches(false);  //不允许使用缓存
            conn.setRequestMethod("POST");  //请求方式
            conn.setRequestProperty("Charset", CHARSET);  //设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            conn.connect();

            if (file != null) {
                /**
                 * 当文件不为空，把文件包装并且上传
                 */
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                /**
                 * 这里重点注意：
                 * name里面的值为服务器端需要key   只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的   比如:abc.png
                 */

                sb.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());
                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();
                /**
                 * 获取响应码  200=成功
                 * 当响应成功，获取响应的流
                 */
                int res = conn.getResponseCode();
                if (res == 200) {
                    InputStream input = conn.getInputStream();
                    StringBuffer sb1 = new StringBuffer();
                    int ss;
                    while ((ss = input.read()) != -1) {
                        sb1.append((char) ss);
                    }
                    result = sb1.toString();
                    System.out.println(result);
                } else {
                    result = "failure";
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
