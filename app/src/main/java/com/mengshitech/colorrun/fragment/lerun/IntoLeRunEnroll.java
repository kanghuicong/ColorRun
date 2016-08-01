package com.mengshitech.colorrun.fragment.lerun;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.customcontrols.ChoseImageDiaLog;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;
import com.mengshitech.colorrun.utils.upload;

import android.view.View.OnClickListener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kanghuicong on 2016/7/27  17:58.
 * 515849594@qq.com
 */
public class IntoLeRunEnroll extends AppCompatActivity implements android.view.View.OnClickListener {
    TextView enroll_name, enroll_time, enroll_address;
    EditText user_name, card_number, enroll_unit, enroll_number;
    RadioGroup rg_enroll_sex, rg_enroll_size;
    Spinner enroll_spinner_card, enroll_spinner_id;
    ImageView enroll_authentication;
    ListView enroll_price;
    RadioButton enroll_agree, rb_sex_man, rb_sex_woman, rb_size_s, rb_size_m, rb_size_l, rb_size_xl, rb_size_2xl, rb_size_3xl;
    Button bt_enroll_agree;
    String sex="男";
    String size;
    ImageView image;
    ChoseImageDiaLog dialog;
    String imageFilePath;
    File temp;
    //图片上传成功后返回的图片路径
    String ScuessImagePath;
    String user_id;
    String lerun_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lerun_event_enroll);
        MainBackUtility.MainBackActivity(IntoLeRunEnroll.this, "报名");

        FindId();
        EnrollClick();

    }

    private void FindId() {

        SharedPreferences sharedPreferences = getSharedPreferences("user_type", Activity.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");


        enroll_name = (TextView) findViewById(R.id.tv_enroll_university);
        enroll_time = (TextView) findViewById(R.id.tv_enroll_time);
        enroll_address = (TextView) findViewById(R.id.tv_enroll_add);
        user_name = (EditText) findViewById(R.id.et_enroll_name);
        rg_enroll_sex = (RadioGroup) findViewById(R.id.rg_enroll_sex);
        rb_sex_man = (RadioButton) findViewById(R.id.rb_sex_man);
        rb_sex_woman = (RadioButton) findViewById(R.id.rb_sex_woman);
        enroll_spinner_card = (Spinner) findViewById(R.id.Spinner_enroll_card);
        card_number = (EditText) findViewById(R.id.et_card_number);
        rg_enroll_size = (RadioGroup) findViewById(R.id.rg_enroll_size);
        rb_size_s = (RadioButton) findViewById(R.id.rb_size_s);
        rb_size_m = (RadioButton) findViewById(R.id.rb_size_m);
        rb_size_l = (RadioButton) findViewById(R.id.rb_size_l);
        rb_size_xl = (RadioButton) findViewById(R.id.rb_size_xl);
        rb_size_2xl = (RadioButton) findViewById(R.id.rb_size_2xl);
        rb_size_3xl = (RadioButton) findViewById(R.id.rb_size_3xl);
        enroll_spinner_id = (Spinner) findViewById(R.id.Spinner_enroll_id);
        enroll_unit = (EditText) findViewById(R.id.et_enroll_unit);
        enroll_number = (EditText) findViewById(R.id.et_contact_number);
        enroll_authentication = (ImageView) findViewById(R.id.iv_enroll_authentication);
        enroll_agree = (RadioButton) findViewById(R.id.rb_enroll_agree);
        bt_enroll_agree = (Button) findViewById(R.id.bt_enroll_agree);

        enroll_authentication.setOnClickListener(this);
    }

    private void EnrollClick() {
        bt_enroll_agree.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("".equals(user_name.getText().toString()) ||
                                                           "".equals(card_number.getText().toString()) ||
                                                           "".equals(enroll_unit.getText().toString()) ||
                                                           "".equals(enroll_number.getText().toString()) ||
                                                           "请选择".equals(enroll_spinner_card.getSelectedItem().toString()) ||
                                                           "请选择".equals(enroll_spinner_id.getSelectedItem().toString())) {
                                                       Toast.makeText(IntoLeRunEnroll.this, "请将信息填写完整！", Toast.LENGTH_SHORT).show();
                                                   } else {
                                                       if ("本校学生".equals(enroll_spinner_id.getSelectedItem().toString())) {

                                                           if (enroll_authentication.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.mipmap.enroll_add_image).getConstantState())) {
                                                               if ("本校学生".equals(enroll_spinner_id.getSelectedItem().toString()))
                                                                   Toast.makeText(IntoLeRunEnroll.this, "请将学生证图片上传！", Toast.LENGTH_SHORT).show();
                                                           } else {

                                                           }
                                                       } else if (rb_sex_man.isChecked()) {
                                                           sex = "男";
                                                       } else if (rb_sex_woman.isChecked()) {
                                                           sex = "女";
                                                       } else if (rb_size_s.isChecked()) {
                                                           size = "S";
                                                       } else if (rb_size_m.isChecked()) {
                                                           size = "M";
                                                       } else if (rb_size_l.isChecked()) {
                                                           size = "L";
                                                       } else if (rb_size_xl.isChecked()) {
                                                           size = "XL";
                                                       } else if (rb_size_2xl.isChecked()) {
                                                           size = "2XL";
                                                       } else if (rb_size_3xl.isChecked()) {
                                                           size = "3XL";
                                                       }
                                                       new Thread(uploadRunnable).start();
                                                   }
                                               }
                                           }

        );
    }

    //执行报名的请求的线程
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String signin_type;
            if (enroll_spinner_card.getSelectedItem().toString().equals("本校学生")) {
                signin_type = "1";
            } else {
                signin_type = "2";
            }
            String path = IPAddress.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "lerun");
            map.put("index", "4");
            map.put("user_id", user_id);
            map.put("lerun_id", "999");
            map.put("user_telphone", enroll_number.getText().toString());
            map.put("lerun_title", enroll_name.getText().toString());
            map.put("signin_type", signin_type);

            map.put("personal_name", user_name.getText().toString());
            map.put("company_name", enroll_unit.getText().toString());
//            map.put("certificate_image",enroll_authentication);
            map.put("identity_type", enroll_spinner_card.getSelectedItem().toString());
            map.put("identity_card", card_number.getText().toString());
            map.put("dress_size", size);
            map.put("payment", "69");
            map.put("certificate_image", ScuessImagePath);
            map.put("user_sex",sex );

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };
    //执行报名的请求的handler
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            Log.i("result", result);
            if (result.equals("timeout")) {
//                progressDialog.dismiss();
                Toast.makeText(IntoLeRunEnroll.this, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else if (result.equals("0")) {
                Toast.makeText(IntoLeRunEnroll.this, "报名失败...", Toast.LENGTH_SHORT).show();
            } else if (result.equals("1")) {
                Toast.makeText(IntoLeRunEnroll.this, "您已经报过名啦", Toast.LENGTH_SHORT).show();
            } else if (result.equals("2")) {
                Toast.makeText(IntoLeRunEnroll.this, "报名成功，等待审核通过", Toast.LENGTH_SHORT).show();
                finish();
            }

        }
    };
    //执行上传证件照的线程
    Runnable uploadRunnable = new Runnable() {
        @Override
        public void run() {
            String servletPath = IPAddress.ImagePath;
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
            String imagepath = (String) msg.obj;
            if (imagepath.equals("failure")) {
                Toast.makeText(IntoLeRunEnroll.this, "图片上传失败", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(runnable).start();
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //上传证件照图片按钮
            case R.id.iv_enroll_authentication:
                showDailog();
                break;

            default:
                break;
        }
    }

    public void showDailog() {
        dialog = new ChoseImageDiaLog(IntoLeRunEnroll.this, R.layout.dialog_choseimage,
                R.style.dialog, new ChoseImageDiaLog.LeaveMyDialogListener() {

            @Override

            public void onClick(View view) {

                System.out.println("aaaaaaaaaaaaaa");
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
                        dialog.dismiss();
                        break;
                    case R.id.btn_picture:
                        Intent intent1 = new Intent(Intent.ACTION_PICK
                        );
                        intent1.setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent1, 103);
                        dialog.dismiss();

                        break;
                    case R.id.btn_cancel:

                        dialog.dismiss();
                        break;

                    default:
                        break;
                }

            }
        });
        // 设置dialog弹出框显示在底部，并且宽度和屏幕一样
        Window window = dialog.getWindow();
        dialog.show();
        window.setGravity(Gravity.BOTTOM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }




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
                    enroll_authentication.setImageBitmap(bmp);

                }
                break;
            case 103:
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;

                // 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                ContentResolver resolver = getContentResolver();


                if (data != null) {
                    Uri originalUri = data.getData(); // 获得图片的uri

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
                    enroll_authentication.setImageBitmap(bmp);

                    temp = new File(imageFilePath);

                    System.out.println(imageFilePath);
                }


                break;
        }

    }

}