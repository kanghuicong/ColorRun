package com.mengshitech.colorrun.fragment.me;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.dao.UserDao;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.ContentCommon;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by kanghuicong on 2016/7/21  9:36.
 * 515849594@qq.com
 */
public class DialogUtility implements View.OnClickListener {
    static String userid;
    static String update_type;
    static String update_values;
    static TextView tv_title;
    static Context Context;
    static Pattern number = Pattern.compile("[0-9]*");

    //修改电话号码
    public static void DialogPhone(final Context context, final TextView tv_phone, final String user_id) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);


        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.me_detail_physique, null);
        TextView title = (TextView) layout.findViewById(R.id.tv_physique_title);
        final EditText et_physique = (EditText) layout.findViewById(R.id.et_physique);
//        TextView tv_physique_bar = (TextView) layout.findViewById(R.id.tv_physique_bar);
        Button bt_conservation = (Button) layout.findViewById(R.id.bt_physique_conservation);
        Button bt_canal = (Button) layout.findViewById(R.id.bt_physique_canal);

        addDialog(dialog, layout);
        title.setText("电话号码");
        et_physique.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        //取消退出对话框
        bt_canal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        //确认输入的号码
        bt_conservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_physique.getText().toString();
                userid = user_id;
                update_type = "user_phone";
                update_values = phone;
                Context = context;
                tv_title = tv_phone;
                Matcher m = number.matcher(phone);
                if (phone.trim().length() == 11 && m.matches()) {
                    new Thread(runnable).start();
                    dialog.dismiss();
                } else {
                    Toast.makeText(context, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
            }

        });
        //清除输入的号码
        et_physique.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_physique.setText("");
            }
        });
    }

    //选择性别
    public static void DialogSex(final Context context, final TextView tv_sex, final String user_id) {

        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.me_detail_sex, null);
        RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.chosesex_radiogroup);

        addDialog(dialog, layout);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {

                int radioButtonId = arg0.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) layout.findViewById(radioButtonId);
                String sex = rb.getText().toString();
                userid = user_id;
                update_type = "user_sex";
                update_values = sex;
                Context = context;
                tv_title = tv_sex;
                if (!"".equals(update_values)) {
                    new Thread(runnable).start();
                }
                dialog.dismiss();
            }
        });
    }

    //修改昵称
    public static void DialogNickname(final Context context, final TextView tv_nickname, final String user_id) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.me_detail_physique, null);
        TextView title = (TextView) layout.findViewById(R.id.tv_physique_title);
        final EditText et_physique = (EditText) layout.findViewById(R.id.et_physique);
//        TextView tv_physique_bar = (TextView) layout.findViewById(R.id.tv_physique_bar);
        Button bt_conservation = (Button) layout.findViewById(R.id.bt_physique_conservation);
        Button bt_canal = (Button) layout.findViewById(R.id.bt_physique_canal);

        addDialog(dialog, layout);
        et_physique.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        title.setText("昵称");
        bt_conservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = et_physique.getText().toString();
                userid = user_id;
                update_type = "user_name";
                update_values = nickname;
                Context = context;
                tv_title = tv_nickname;
                if (nickname.isEmpty()) {
                    Toast.makeText(context, "请输入昵称", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    new Thread(runnable).start();
                    dialog.dismiss();
                }
            }
        });
        bt_canal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    //修改地区、个性签名和邮箱
    public static void DialogAutograph(String type, final Context context, final TextView tv_conent, final String user_id) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.me_detail_physique, null);
        TextView title = (TextView) layout.findViewById(R.id.tv_physique_title);
        final EditText et_physique = (EditText) layout.findViewById(R.id.et_physique);
//        TextView tv_physique_bar = (TextView) layout.findViewById(R.id.tv_physique_bar);
        Button bt_conservation = (Button) layout.findViewById(R.id.bt_physique_conservation);
        Button bt_canal = (Button) layout.findViewById(R.id.bt_physique_canal);

        addDialog(dialog, layout);

        switch (type) {
            case ("sign"):
                title.setText("个性签名");
                et_physique.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                bt_conservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sign = et_physique.getText().toString();
                        userid = user_id;
                        update_type = "user_sign";
                        update_values = sign;
                        Context = context;
                        tv_title = tv_conent;
                        if (sign.isEmpty()) {
                            Toast.makeText(context, "请输入个性签名", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            new Thread(runnable).start();
                            dialog.dismiss();
                        }
                    }
                });
                bt_canal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case ("email"):
                title.setText("邮箱");
                bt_conservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String email = et_physique.getText().toString();
                        userid = user_id;
                        update_type = "user_email";
                        update_values = email;
                        Context = context;
                        tv_title = tv_conent;
                        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
                        Pattern p = Pattern.compile(str);
                        Matcher m = p.matcher(email);
                        if (m.matches()) {
                            new Thread(runnable).start();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "请输入正确的邮箱", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });
                bt_canal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case ("address"):
                et_physique.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                title.setText("地区");
                bt_conservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String address = et_physique.getText().toString();
                        userid = user_id;
                        update_type = "user_address";
                        update_values = address;
                        Context = context;
                        tv_title = tv_conent;
                        if (address.isEmpty()) {
                            Toast.makeText(context, "请输入昵称", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            new Thread(runnable).start();
                            dialog.dismiss();
                        }
                    }
                });
                bt_canal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
        }

    }

    //修改体重和身高
    public static void DialogPhysique(String type, final Context context, final TextView tv_physique, final String user_id) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.me_detail_physique, null);
        TextView title = (TextView) layout.findViewById(R.id.tv_physique_title);
        final EditText et_physique = (EditText) layout.findViewById(R.id.et_physique);
//        TextView tv_physique_bar = (TextView) layout.findViewById(R.id.tv_physique_bar);
        Button bt_conservation = (Button) layout.findViewById(R.id.bt_physique_conservation);
        Button bt_canal = (Button) layout.findViewById(R.id.bt_physique_canal);

        addDialog(dialog, layout);
        et_physique.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});

        switch (type) {
            case ("height"):
                title.setText("身高(cm)");
//                tv_physique_bar.setText("cm");
                bt_conservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String physique = et_physique.getText().toString();
                        userid = user_id;
                        update_type = "user_height";
                        update_values = physique;
                        Context = context;
                        tv_title = tv_physique;
                        Matcher m = number.matcher(physique);
                        if (m.matches()) {
                            new Thread(runnable).start();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "请输入身高", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                bt_canal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
            case ("weight"):
                title.setText("体重(kg)");
//                tv_physique_bar.setText("kg");
                bt_conservation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String physique = et_physique.getText().toString();
                        userid = user_id;
                        update_type = "user_weight";
                        update_values = physique;
                        Context = context;
                        tv_title = tv_physique;
                        Matcher m = number.matcher(physique);
                        if (m.matches()) {
                            new Thread(runnable).start();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(context, "请输入体重", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });


                bt_canal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    //注销账号
    public static void DialogCancel(final Context context, final ImageView header, final TextView name, final TextView id) {
        Log.i("DialogCancel", name.getText().toString() + "---" + id.getText().toString());
        LayoutInflater inflater = LayoutInflater.from(context);
        final Dialog dialog = new AlertDialog.Builder(context).create();


        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.me_cancel, null);
        Button bt_cancel_yes = (Button) layout.findViewById(R.id.bt_cancel_yes);
        Button bt_cancel_no = (Button) layout.findViewById(R.id.bt_cancel_no);
        addDialog(dialog, layout);

        bt_cancel_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        bt_cancel_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DialogCancel", "1111");
                header.setImageResource(R.mipmap.default_avtar);
                name.setText("未登录");
                id.setText("1");
                Log.i("DialogCancel", "2222");
                Log.i("DialogCancel", id.getText().toString() + "9");
                SharedPreferences mySharedPreferences = context.getSharedPreferences("user_type", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                editor.putString("user_type", "0");
                editor.putString("user_id", "");
                editor.commit();
                ContentCommon.login_state = "0";
                dialog.dismiss();
            }
        });
    }

    //添加窗口
    private static void addDialog(Dialog dialog, LinearLayout layout) {
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }

    static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "user");
            map.put("user_id", userid);
            map.put("update_type", update_type);
            map.put("update_values", update_values);
            map.put("index", "3");

            String result = HttpUtils.sendHttpClientPost(path, map,
                    "utf-8");

            Message msg = new Message();
            msg.obj = result;
            handler.sendMessage(msg);
        }
    };
    static Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String result = (String) msg.obj;

            if (result.equals("timeout")) {
                Toast.makeText(Context, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                if (result.equals("1")) {
                    tv_title.setText(update_values);
                    UserDao dao = new UserDao(Context);
                    Log.i("Dialog", update_type + "---" + update_values + "---" + userid);
                    dao.update_data(update_type, update_values, userid);
                    Toast.makeText(Context, "修改成功！", Toast.LENGTH_SHORT).show();
                } else if (result.equals("0")) {
                    Toast.makeText(Context, "修改失败！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
//        dialog.dismiss();
    }
}
