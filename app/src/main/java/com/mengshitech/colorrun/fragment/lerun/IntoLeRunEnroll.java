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
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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

import com.google.gson.Gson;
import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.activity.RegisterSuccess;
import com.mengshitech.colorrun.adapter.LerunEnrollListViewAdapter;
import com.mengshitech.colorrun.alipay.AlipayFragment;
import com.mengshitech.colorrun.bean.CreateQrBean;
import com.mengshitech.colorrun.bean.EnrollEntity;
import com.mengshitech.colorrun.customcontrols.ChoseImageDiaLog;
import com.mengshitech.colorrun.fragment.PaySuccessFragment;
import com.mengshitech.colorrun.fragment.me.myLeRunFragment;
import com.mengshitech.colorrun.utils.CompressImage;
import com.mengshitech.colorrun.utils.CreateQrCode;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.RandomUtils;
import com.mengshitech.colorrun.utils.Utility;
import com.mengshitech.colorrun.utils.upload;

import org.json.JSONException;

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

    File qrcodeFile;
    //图片上传成功后返回的图片路径
    String ScuessImagePath;
    String user_id;
    View enroll_view;
    int lerun_id, charge_mode, free_price, common_price, vip_price, choose_price = -2, type;
    String free_equipment, common_equipment, vip_equipment;
    LerunEnrollListViewAdapter adapter;
    List<EnrollEntity> list = new ArrayList<EnrollEntity>();
    protected WeakReference<View> mRootView;
    LinearLayout ll_enroll_content;
    int index;
    int flag = 100;
    private Context context;
    String signin_type = "";//报名类型
    private String QRcodeImage;
    private int choseimage_state = 0;
    FragmentManager fragmentManager;
    Activity activity;
    private String user_telphone;
    private String order_id;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        fragmentManager = getFragmentManager();

        activity = getActivity();
        if (mRootView == null || mRootView.get() == null) {

            enroll_view = inflater.inflate(R.layout.lerun_event_enroll, null);
            MainBackUtility.MainBack(enroll_view, "报名", getFragmentManager());
            MainActivity.rgMainBottom.setVisibility(View.GONE);
            FindId();
            GetData();
            EnrollClick();


            ll_enroll_content.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    // TODO Auto-generated method stub
                    ll_enroll_content.setFocusable(true);
                    ll_enroll_content.setFocusableInTouchMode(true);
                    ll_enroll_content.requestFocus();

                    return false;
                }
            });
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
            enroll_explain.setText("只有承办方人员且上传学生证、工作证等有效材料证明方可免费");
        }
        if (charge_mode == 3) {
            enroll_explain.setText("请选择价格");
        }


        if (free_price >= 0) {
            EnrollEntity entity = new EnrollEntity();
            entity.setPrice(free_price);
            entity.setEnroll_equipment(free_equipment);
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
            }
        });

        enroll_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flag = position;
                adapter.setFlag(flag);
                adapter.notifyDataSetChanged();
            }
        });
        enroll_listview.setAdapter(adapter);
    }


    private void FindId() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_type", Activity.MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");

        ll_enroll_content = (LinearLayout) enroll_view.findViewById(R.id.ll_enroll_content);
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


        bt_enroll_agree.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

                                                   user_telphone = enroll_number.getText().toString();

                                                   if (!"".equals(user_name.getText().toString()) &&
                                                           !"".equals(card_number.getText().toString()) &&
                                                           !"".equals(enroll_unit.getText().toString()) &&
                                                           !"".equals(enroll_number.getText().toString()) &&
                                                           !"请选择".equals(enroll_spinner_card.getSelectedItem().toString()) &&
                                                           !"请选择".equals(enroll_spinner_id.getSelectedItem().toString()) &&
                                                           choose_price >= 0) {

                                                       Pattern p_phone = Pattern.compile("^1[34578]\\d{9}$");
                                                       Matcher m_phone = p_phone.matcher(enroll_number.getText().toString());

                                                       Pattern p_name = Pattern.compile("^[\u4e00-\u9fa5]+$");
                                                       Matcher m_name = p_name.matcher(user_name.getText().toString());

                                                       if (m_name.matches()) {
                                                           if (m_phone.matches()) {
                                                               if (charge_mode == 2) {
                                                                   Log.i("类型", enroll_spinner_id.getSelectedItem().toString() + "");
                                                                   if ("承办方".equals(enroll_spinner_id.getSelectedItem().toString()) && choose_price == 0) {
                                                                       if (choseimage_state == 0) {
                                                                           Toast.makeText(context, "请上传材料证明承办方身份！", Toast.LENGTH_SHORT).show();
                                                                       } else {
                                                                           Log.i("charge_mode==2", "type==1");
                                                                           signin_type = "1";
                                                                           creatQRcode();
                                                                           new Thread(uploadRunnable).start();
                                                                       }
                                                                   } else if ("非承办方".equals(enroll_spinner_id.getSelectedItem().toString()) && choose_price == 0) {
                                                                       Toast.makeText(context, "只有承办方人员才可免费！", Toast.LENGTH_SHORT).show();
                                                                   } else {
                                                                       creatQRcode();
                                                                       Log.i("charge_mode==2", "type==2");
                                                                       signin_type = "2";
                                                                       new Thread(QrcodeRunnable).start();
                                                                   }
                                                               } else {
                                                                   creatQRcode();
                                                                   switch (charge_mode) {
                                                                       //全部免费
                                                                       case 1:
                                                                           signin_type = "";
                                                                           new Thread(QrcodeRunnable).start();
                                                                           Log.i("charge_mode==1", "type==1");
                                                                           break;
                                                                       //全部收费
                                                                       case 3:
                                                                           signin_type = "";
                                                                           new Thread(QrcodeRunnable).start();
                                                                           Log.i("charge_mode==3", "type==1");
                                                                           break;

                                                                       default:
                                                                           break;
                                                                   }
                                                               }
                                                           } else {
                                                               Toast.makeText(context, "请输入正确的电话号码!", Toast.LENGTH_SHORT).show();
                                                           }
                                                       } else {
                                                           Toast.makeText(context, "请输入正确的姓名!", Toast.LENGTH_SHORT).show();
                                                           user_name.setText("");
                                                       }
                                                   } else {
                                                       Toast.makeText(context, "请将信息填写完整！", Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           }

        );
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
            String imagepath = (String) msg.obj;
            if (imagepath.equals("failure")) {
                Toast.makeText(context, "图片上传失败", Toast.LENGTH_SHORT).show();
            } else {

                try {
                    ScuessImagePath = JsonTools.getUserLog(imagepath);

                    Log.i("上传证件照imagepath", ScuessImagePath);
                    new Thread(QrcodeRunnable).start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    };


    //上传二维码
    Runnable QrcodeRunnable = new Runnable() {
        @Override
        public void run() {
            String servletPath = ContentCommon.ImagePath;
            ScuessImagePath = upload.uploadImage(qrcodeFile, servletPath);
            Message msg = new Message();
            msg.obj = ScuessImagePath;
            QrcodeHandle.sendMessage(msg);

        }
    };
    Handler QrcodeHandle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String imagepath = (String) msg.obj;
            if (imagepath.equals("failure")) {
                Toast.makeText(context, "图片上传失败", Toast.LENGTH_SHORT).show();
            } else {
                try {

                    QRcodeImage = JsonTools.getUserLog(imagepath);

                    Log.i("上传二维码imagepath", QRcodeImage);

                    new Thread(Mode1runnable).start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };


    //执行免费报名的请求的线程
    Runnable Mode1runnable = new Runnable() {
        @Override
        public void run() {
            order_id = RandomUtils.LerunOrderId();
            Log.i("order_id2",order_id+"aa");
            String path = ContentCommon.PATH;
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
            map.put("QRcode_Path", QRcodeImage);
            map.put("charge_mode", charge_mode + "");
            map.put("order_id", order_id);

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");
            Message msg = new Message();
            msg.obj = result;
            mode1handler.sendMessage(msg);
        }
    };
    //执行免费报名的请求的handler
    Handler mode1handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Log.i("报名", result + "sssssss");
                    int state = JsonTools.getState("state", result);
                    if (state == 1) {
                        //报名成功的操作
                        switch (charge_mode) {
                            case 1:
                                Bundle bundle = new Bundle();
                                bundle.putString("qrcode_image", QRcodeImage + "");
                                bundle.putInt("type", 2);
                                DisplayQRcodeFragment displayQRcodeFragment = new DisplayQRcodeFragment();

                                displayQRcodeFragment.setArguments(bundle);
                                Utility.replace2DetailFragment(getFragmentManager(), displayQRcodeFragment);
                                break;
                            case 2:
                                if (signin_type.equals("1")) {
                                    Bundle bundle2 = new Bundle();
                                    bundle2.putString("qrcode_image", QRcodeImage + "");
                                    bundle2.putInt("type", 2);
                                    DisplayQRcodeFragment displayQRcodeFragment2 = new DisplayQRcodeFragment();

                                    displayQRcodeFragment2.setArguments(bundle2);
                                    Utility.replace2DetailFragment(getFragmentManager(), displayQRcodeFragment2);

                                } else {
                                    Bundle bundle3 = new Bundle();
                                    bundle3.putString("user_name", user_name.getText().toString());
                                    bundle3.putString("lerun_title", enroll_name.getText().toString());
                                    bundle3.putString("qrcode_image", QRcodeImage + "");
                                    bundle3.putInt("lerun_price", choose_price);
                                    bundle3.putString("user_telphone", user_telphone);
                                    bundle3.putInt("lerun_id", lerun_id);
                                    bundle3.putString("order_id", order_id);

                                    Log.i("1Payment", user_name.getText().toString() + enroll_name.getText().toString() + choose_price);
                                    AlipayFragment alipayFragment = new AlipayFragment();
                                    alipayFragment.setArguments(bundle3);
                                    Utility.replace2DetailFragment(getFragmentManager(), alipayFragment);
                                }

                                break;
                            case 3:
                                Bundle bundle4 = new Bundle();
                                bundle4.putString("user_name", user_name.getText().toString());
                                bundle4.putString("lerun_title", enroll_name.getText().toString());
                                bundle4.putInt("lerun_price", choose_price);
                                bundle4.putString("qrcode_image", QRcodeImage + "");
                                bundle4.putString("user_telphone", user_telphone);
                                bundle4.putInt("lerun_id", lerun_id);
                                bundle4.putString("order_id", order_id);
                                Log.i("1Payment", user_name.getText().toString() + enroll_name.getText().toString() + choose_price);
                                AlipayFragment alipayFragment = new AlipayFragment();
                                alipayFragment.setArguments(bundle4);
                                Utility.replace2DetailFragment(getFragmentManager(), alipayFragment);
                                break;
                        }

                    } else {
                        //报名失败
                        String datas = JsonTools.getDatas(result);
                        Toast.makeText(context, datas, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


    /***************************************************************************
     * 选择证件照
     *********************************************************************************/
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
                                + "/" + RandomUtils.getRandomInt() + ".jpg";
                        File file = new File(imageFilePath);
                        Uri imageFileUri = Uri.fromFile(file);// 获取文件的Uri
                        Intent it = new Intent(
                                MediaStore.ACTION_IMAGE_CAPTURE);// 跳转到相机Activity
                        it.putExtra(
                                android.provider.MediaStore.EXTRA_OUTPUT,
                                imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
                        String compressImage = CompressImage.compressBitmap(context, imageFilePath, 300, 300, false);
                        temp = new File(compressImage);
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
                    choseimage_state = 1;

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
                    //压缩图片
                    String compressImage = CompressImage.compressBitmap(context, imageFilePath, 300, 300, false);
                    temp = new File(compressImage);
                    choseimage_state = 1;

//                    System.out.println(imageFilePath);
                }

                break;
        }

    }


    //创建二维码
    private void creatQRcode() {
        CreateQrBean bean = new CreateQrBean();
        bean.setLerun_id(lerun_id);
        bean.setUser_id(user_id);
        bean.setUser_telphone(enroll_number.getText().toString());

        Gson gson = new Gson();
        String jsonString = gson.toJson(bean);
        Log.i("jsonString", "" + jsonString);
        String qrcodeImage = CreateQrCode.createImage(jsonString, 400, 400);
        Log.i("qrcodeImage", qrcodeImage);
        qrcodeFile = new File(qrcodeImage);
    }


}