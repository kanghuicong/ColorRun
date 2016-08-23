package com.mengshitech.colorrun.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.customcontrols.ChoseImageDiaLog;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.utils.CompressImage;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.upload;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：wschenyongyin on 2016/8/3 14:57
 * 说明:
 */
public class UserLogActivity extends Activity implements View.OnClickListener {
    private ImageView user_image;
    private ImageView image_chose;
    private LinearLayout ll_back;
    private ChoseImageDiaLog diaLog;
    private String imageFilePath;
    private File temp;
    String ScuessImagePath;
    private static final String IMAGE_FILE_LOCATION = "file:///sdcard/temp.jpg";//temp file
    Uri imageUri = Uri.parse(IMAGE_FILE_LOCATION);//The Uri to store the big bitmap

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me_detail_userlog);
        init();

    }

    private void init() {
        image_chose = (ImageView) findViewById(R.id.image_chose);
        user_image = (ImageView) findViewById(R.id.user_image);
        ll_back = (LinearLayout) findViewById(R.id.userlog_btn_back);
        Glide.with(UserLogActivity.this).load(ContentCommon.user_log).into(user_image);
        ll_back.setOnClickListener(this);
        image_chose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //后退
            case R.id.image_chose:
                diaLog = new ChoseImageDiaLog(UserLogActivity.this, R.layout.dialog_choseimage, R.style.dialog,
                        new ChoseImageDiaLog.LeaveMyDialogListener() {
                            @Override
                            public void onClick(View view) {
                                switch (view.getId()) {
                                    case R.id.btn_takephoto:
                                        imageFilePath = Environment
                                                .getExternalStorageDirectory()
                                                .getAbsolutePath()
                                                + "/filename.jpg";
                                        temp = new File(imageFilePath);
                                        Uri imageFileUri = Uri.fromFile(temp);// 获取文件的Uri
                                        Intent it = new Intent(
                                                MediaStore.ACTION_IMAGE_CAPTURE);// 跳转到相机Activity
                                        it.putExtra(
                                                android.provider.MediaStore.EXTRA_OUTPUT,
                                                imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
                                        startActivityForResult(it, 102);

                                        diaLog.dismiss();
                                        break;
                                    case R.id.btn_picture:
                                        Intent intent = new Intent(Intent.ACTION_PICK
                                        );
                                        intent.setDataAndType(
                                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

                                        startActivityForResult(intent, 103);
                                        diaLog.dismiss();
                                        break;
                                    case R.id.btn_cancel:
                                        diaLog.dismiss();
                                        break;

                                    default:
                                        break;
                                }
                            }
                        });

                Window window = diaLog.getWindow();
                diaLog.show();
                window.setGravity(Gravity.BOTTOM);
                window.getDecorView().setPadding(0, 0, 0, 0);
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = WindowManager.LayoutParams.FILL_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);
                break;
            //选择图片
            case R.id.userlog_btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    //执行上传证件照的线程
    Runnable uploadRunnable = new Runnable() {
        @Override
        public void run() {
            String servletPath = ContentCommon.ImagePath;
            ScuessImagePath = upload.uploadImage(temp, servletPath);
            Message msg = new Message();
            msg.obj = ScuessImagePath;
            uploadHandle.sendMessage(msg);

        }
    };
    Handler uploadHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.equals("failure")) {
                Toast.makeText(UserLogActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int state = JsonTools.getState("state", result);
                    if (state == 1) {
                        String datas = JsonTools.getDatas(result);
                        ScuessImagePath = JsonTools.getUserLog(datas);
//                        ScuessImagePath=list.get(0);

                        Log.i("ScuessImagePath", ScuessImagePath + "ssss");
                        new Thread(updateRunable).start();
                    } else {
                        Toast.makeText(UserLogActivity.this, "图片上传失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    Runnable updateRunable = new Runnable() {
        @Override
        public void run() {
            String servlet = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "user");
            map.put("user_id", ContentCommon.user_id);
            map.put("index", "3");
            map.put("update_type", "user_header");
            map.put("update_values", ScuessImagePath);
            String result = HttpUtils.sendHttpClientPost(servlet, map, "utf-8");
            Message msg = new Message();
            msg.obj = result;

            handler.sendMessage(msg);

        }
    };
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            Log.i("result", result + ":shsiss");
            if (result.equals("1")) {
                Toast.makeText(UserLogActivity.this, "更改头像成功", Toast.LENGTH_SHORT).show();
                UserDao dao = new UserDao(UserLogActivity.this);
                dao.update_data("user_header", ScuessImagePath, ContentCommon.user_id);

            } else if (result.equals("0")) {
                Toast.makeText(UserLogActivity.this, "更改头像失败", Toast.LENGTH_SHORT).show();
            }
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 102:

                if (resultCode == AppCompatActivity.RESULT_OK) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;
                    Bitmap bmp = BitmapFactory.decodeFile(imageFilePath, options);

//                    Bitmap bmp = BitmapFactory.decodeFile(imageFilePath);
                    user_image.setImageBitmap(bmp);

                    new Thread(uploadRunnable).start();

                }
                break;
            case 103:


                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                ContentResolver resolver = getContentResolver();


                if (data != null) {
                    Uri originalUri = data.getData(); // 获得图片的uri
                    if (originalUri != null) {
                        try {
                            Bitmap bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // 这里开始的第二部分，获取图片的路径：

                        String[] proj = {MediaStore.Images.Media.DATA};

                        // 好像是android多媒体数据库的封装接口，具体的看Android文档
                        @SuppressWarnings("deprecation")
                        Cursor cursor = managedQuery(originalUri, proj, null, null,
                                null);
                        // 按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor
                                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        // 最后根据索引值获取图片路径
                        imageFilePath = cursor.getString(column_index);
                        Bitmap bmp = BitmapFactory.decodeFile(imageFilePath, options);
                        user_image.setImageBitmap(bmp);
                        //压缩图片
                        String image = CompressImage.compressBitmap(UserLogActivity.this, imageFilePath, 200, 200, false);


                        temp = new File(image);
                        new Thread(uploadRunnable).start();
                        System.out.println(imageFilePath);
                    } else {

                    }

                }

                break;

        }

    }


}
