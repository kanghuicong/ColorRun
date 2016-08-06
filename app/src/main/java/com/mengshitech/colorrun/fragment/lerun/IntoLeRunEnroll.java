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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.LerunEnrollListViewAdapter;
import com.mengshitech.colorrun.alipay.AlipayFragment;
import com.mengshitech.colorrun.bean.EnrollEntity;
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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kanghuicong on 2016/7/27  17:58.
 * 515849594@qq.com
 */
public class IntoLeRunEnroll extends Fragment implements View.OnClickListener {
    TextView enroll_name, enroll_time, enroll_address, enroll_explain;
    EditText user_name, card_number, enroll_unit, enroll_number;
    RadioGroup rg_enroll_sex, rg_enroll_size;
    Spinner enroll_spinner_card, enroll_spinner_id;
    ImageView enroll_authentication;
    ListView enroll_listview;
    RadioButton enroll_agree, rb_sex_man, rb_sex_woman, rb_size_s, rb_size_m, rb_size_l, rb_size_xl, rb_size_2xl, rb_size_3xl;
    Button bt_enroll_agree;
    String sex = "男";
    String size = "S";
    ImageView enroll_image;
    ChoseImageDiaLog dialog;
    String imageFilePath;
    File temp;
    //图片上传成功后返回的图片路径
    String ScuessImagePath;
    String user_id;
    View enroll_view;
    int lerun_id, charge_mode, free_price, common_price, vip_price, choose_price=-2,type;
    String free_equipment, common_equipment, vip_equipment;
    LerunEnrollListViewAdapter adapter;
    List<EnrollEntity> list = new ArrayList<EnrollEntity>();
    protected WeakReference<View> mRootView;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null || mRootView.get() == null) {

            enroll_view = inflater.inflate(R.layout.lerun_event_enroll, null);
            MainBackUtility.MainBack(enroll_view, "报名", getFragmentManager());
            FindId();
            GetData();
            EnrollClick();

            mRootView = new WeakReference<View>(enroll_view);
        } else {
            ViewGroup parent = (ViewGroup) mRootView.get().getParent();
            if (parent != null) {
                parent.removeView(mRootView.get());
            }
        }
        return mRootView.get();
    }

    private void GetData() {
        lerun_id = getArguments().getInt("lerun_id");
        enroll_name.setText(getArguments().getString("title"));
        enroll_time.setText(getArguments().getString("time"));
        enroll_address.setText(getArguments().getString("address"));
        charge_mode = getArguments().getInt("charge_mode");
        free_price = getArguments().getInt("free_price");
        common_price = getArguments().getInt("common_price");
        vip_price = getArguments().getInt("vip_price");
        free_equipment = getArguments().getString("free_equipment");
        common_equipment = getArguments().getString("common_equipment");
        vip_equipment = getArguments().getString("vip_equipment");

        if (charge_mode == 1) {
            enroll_explain.setText("所有人免费");
        }
        if (charge_mode == 2) {
            enroll_explain.setText("只有本校学生且上传学生证照片方可免费");
        }
        if (charge_mode == 3) {
            enroll_explain.setText("请选择价格");
        }


        if (free_price >= 0) {
            EnrollEntity entity = new EnrollEntity();
            entity.setPrice(free_price);
            entity.setEnroll_equipment(free_equipment);
            Log.i("free_price", free_price + "");
            list.add(entity);
        }
        if (common_price >= 0) {
            EnrollEntity entity = new EnrollEntity();
            entity.setPrice(common_price);
            entity.setEnroll_equipment(common_equipment);
            list.add(entity);
        }
        if (vip_price >= 0) {
            EnrollEntity entity = new EnrollEntity();
            entity.setPrice(vip_price);
            entity.setEnroll_equipment(vip_equipment);
            list.add(entity);
        }
        adapter = new LerunEnrollListViewAdapter(getActivity(), list, enroll_listview, new LerunEnrollListViewAdapter.CallBack() {
            @Override
            public void returnInfo(int price) {
                choose_price = price;
                Log.i("choose_price", choose_price + "");
            }
        });
        enroll_listview.setAdapter(adapter);
    }

    private void FindId() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_type", Activity.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");

        enroll_name = (TextView) enroll_view.findViewById(R.id.tv_enroll_university);
        enroll_time = (TextView) enroll_view.findViewById(R.id.tv_enroll_time);
        enroll_address = (TextView) enroll_view.findViewById(R.id.tv_enroll_add);
        user_name = (EditText) enroll_view.findViewById(R.id.et_enroll_name);
        rg_enroll_sex = (RadioGroup) enroll_view.findViewById(R.id.rg_enroll_sex);
        rb_sex_man = (RadioButton) enroll_view.findViewById(R.id.rb_sex_man);
        rb_sex_woman = (RadioButton) enroll_view.findViewById(R.id.rb_sex_woman);
        enroll_spinner_card = (Spinner) enroll_view.findViewById(R.id.Spinner_enroll_card);
        card_number = (EditText) enroll_view.findViewById(R.id.et_card_number);
        rg_enroll_size = (RadioGroup) enroll_view.findViewById(R.id.rg_enroll_size);
        rb_size_s = (RadioButton) enroll_view.findViewById(R.id.rb_size_s);
        rb_size_m = (RadioButton) enroll_view.findViewById(R.id.rb_size_m);
        rb_size_l = (RadioButton) enroll_view.findViewById(R.id.rb_size_l);
        rb_size_xl = (RadioButton) enroll_view.findViewById(R.id.rb_size_xl);
        rb_size_2xl = (RadioButton) enroll_view.findViewById(R.id.rb_size_2xl);
        rb_size_3xl = (RadioButton) enroll_view.findViewById(R.id.rb_size_3xl);
        enroll_spinner_id = (Spinner) enroll_view.findViewById(R.id.Spinner_enroll_id);
        enroll_unit = (EditText) enroll_view.findViewById(R.id.et_enroll_unit);
        enroll_number = (EditText) enroll_view.findViewById(R.id.et_contact_number);
        enroll_authentication = (ImageView) enroll_view.findViewById(R.id.iv_enroll_authentication);
        enroll_agree = (RadioButton) enroll_view.findViewById(R.id.rb_enroll_agree);
        bt_enroll_agree = (Button) enroll_view.findViewById(R.id.bt_enroll_agree);
        enroll_listview = (ListView) enroll_view.findViewById(R.id.lv_enroll_listview);
        enroll_image = (ImageView) enroll_view.findViewById(R.id.iv_enroll_image);
        enroll_explain = (TextView) enroll_view.findViewById(R.id.tv_price_explain);
        enroll_authentication.setOnClickListener(this);
    }

    private void EnrollClick() {
        bt_enroll_agree.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   Log.i("1choose_price",choose_price+"");
                                                   if (!"".equals(user_name.getText().toString()) &&
                                                           !"".equals(card_number.getText().toString()) &&
                                                           !"".equals(enroll_unit.getText().toString()) &&
                                                           !"".equals(enroll_number.getText().toString()) &&
                                                           !"请选择".equals(enroll_spinner_card.getSelectedItem().toString()) &&
                                                           !"请选择".equals(enroll_spinner_id.getSelectedItem().toString()) &&
                                                           choose_price>=0) {
                                                       if (user_name.getText().toString().length() <= 10) {
                                                           if (enroll_number.getText().toString().length() == 11) {
                                                               if (charge_mode == 2) {
                                                                   if ("本校学生".equals(enroll_spinner_id.getSelectedItem().toString()) && choose_price==0) {
                                                                       if (enroll_authentication.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.mipmap.enroll_add_image).getConstantState())) {
                                                                           Toast.makeText(getActivity(), "请将学生证图片上传！", Toast.LENGTH_SHORT).show();
                                                                       } else {
                                                                           if (rb_sex_man.isChecked()) {
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
                                                                           } new Thread(runnable).start();
                                                                       }
                                                                   } else {
                                                                       if (rb_sex_man.isChecked()) {
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
                                                                       } new Thread(runnable).start();
                                                                   }
                                                               } else {
                                                                   if (rb_sex_man.isChecked()) {
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
                                                                   new Thread(runnable).start();
                                                               }
                                                           } else {
                                                               Toast.makeText(getActivity(), "请输入正确的电话号码!", Toast.LENGTH_SHORT).show();
                                                               enroll_number.setText("");
                                                           }
                                                       } else {
                                                           Toast.makeText(getActivity(), "请输入正确的姓名!", Toast.LENGTH_SHORT).show();
                                                           user_name.setText("");
                                                       }
                                                   } else {
                                                       Toast.makeText(getActivity(), "请将信息填写完整！", Toast.LENGTH_SHORT).show();
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
            map.put("lerun_id", lerun_id + "");
            map.put("user_telphone", enroll_number.getText().toString());
            map.put("lerun_title", enroll_name.getText().toString());
            map.put("signin_type", signin_type);
            map.put("personal_name", user_name.getText().toString());
            map.put("company_name", enroll_unit.getText().toString());
            map.put("identity_type", enroll_spinner_card.getSelectedItem().toString());
            map.put("identity_card", card_number.getText().toString());
            map.put("dress_size", size);
            map.put("payment", choose_price + "");
            map.put("certificate_image", ScuessImagePath);
            map.put("user_sex", sex);

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
                Toast.makeText(getActivity(), "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else if (result.equals("0")) {
                Toast.makeText(getActivity(), "报名失败...", Toast.LENGTH_SHORT).show();
            } else if (result.equals("1")) {
                Toast.makeText(getActivity(), "您已经报过名啦", Toast.LENGTH_SHORT).show();
            } else if (result.equals("2")) {
                Bundle bundle = new Bundle();
                bundle.putString("user_name",user_name.getText().toString());
                bundle.putString("lerun_title",enroll_name.getText().toString());
                bundle.putInt("lerun_price",choose_price);
                Log.i("1Payment",user_name.getText().toString()+enroll_name.getText().toString()+choose_price);
//                LeRunPayment leRunPayment = new LeRunPayment();
//                leRunPayment.setArguments(bundle);
                AlipayFragment alipayFragment=new AlipayFragment();
                alipayFragment.setArguments(bundle);
                Utility.replace2DetailFragment(getFragmentManager(), alipayFragment);
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
                Toast.makeText(getActivity(), "图片上传失败", Toast.LENGTH_SHORT).show();
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
        dialog = new ChoseImageDiaLog(getActivity(), R.layout.dialog_choseimage,
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
                ContentResolver resolver = getActivity().getContentResolver();


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
                    Cursor cursor = getActivity().managedQuery(originalUri, proj, null, null,
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