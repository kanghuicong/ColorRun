package com.mengshitech.colorrun.fragment.lerun;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.UserEntiy;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;

/**
 * Created by kanghuicong on 2016/7/27  17:58.
 * 515849594@qq.com
 */
public class IntoLeRunEnroll extends AppCompatActivity {
    TextView enroll_name,enroll_time,enroll_address;
    EditText user_name,card_number,enroll_unit,enroll_number;
    RadioGroup rg_enroll_sex,rg_enroll_size;
    Spinner enroll_spinner_card,enroll_spinner_id;
    ImageView enroll_authentication;
    ListView enroll_price;
    RadioButton enroll_agree,rb_sex_man,rb_sex_woman,rb_size_s,rb_size_m,rb_size_l,rb_size_xl,rb_size_2xl,rb_size_3xl;
    Button bt_enroll_agree;
    String sex,size;
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.lerun_event_enroll);
        MainBackUtility.MainBackActivity(IntoLeRunEnroll.this,"报名");
        FindId();
        EnrollClick();

    }

    private void FindId() {
        enroll_name = (TextView)findViewById(R.id.tv_enroll_university);
        enroll_time = (TextView)findViewById(R.id.tv_enroll_time);
        enroll_address = (TextView)findViewById(R.id.tv_enroll_add);
        user_name = (EditText)findViewById(R.id.et_enroll_name);
        rg_enroll_sex = (RadioGroup)findViewById(R.id.rg_enroll_sex);
        rb_sex_man = (RadioButton)findViewById(R.id.rb_sex_man);
        rb_sex_woman = (RadioButton)findViewById(R.id.rb_sex_woman);
        enroll_spinner_card = (Spinner)findViewById(R.id.Spinner_enroll_card);
        card_number = (EditText)findViewById(R.id.et_card_number);
        rg_enroll_size = (RadioGroup)findViewById(R.id.rg_enroll_size);
        rb_size_s = (RadioButton)findViewById(R.id.rb_size_s);
        rb_size_m = (RadioButton)findViewById(R.id.rb_size_m);
        rb_size_l = (RadioButton)findViewById(R.id.rb_size_l);
        rb_size_xl = (RadioButton)findViewById(R.id.rb_size_xl);
        rb_size_2xl = (RadioButton)findViewById(R.id.rb_size_2xl);
        rb_size_3xl = (RadioButton)findViewById(R.id.rb_size_3xl);
        enroll_spinner_id = (Spinner)findViewById(R.id.Spinner_enroll_id);
        enroll_unit = (EditText)findViewById(R.id.et_enroll_unit);
        enroll_number = (EditText)findViewById(R.id.et_contact_number);
        enroll_authentication = (ImageView)findViewById(R.id.iv_enroll_authentication);
        enroll_agree = (RadioButton)findViewById(R.id.rb_enroll_agree);
        bt_enroll_agree = (Button)findViewById(R.id.bt_enroll_agree);
    }

    private void EnrollClick() {
        bt_enroll_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(user_name.getText().toString())||
                    "".equals(card_number.getText().toString())||
                    "".equals(enroll_unit.getText().toString())||
                    "".equals(enroll_number.getText().toString())||
                    "请选择".equals(enroll_spinner_card.getSelectedItem().toString()) ||
                    "请选择".equals(enroll_spinner_id.getSelectedItem().toString())){
                    Toast.makeText(IntoLeRunEnroll.this,"请将信息填写完整！",Toast.LENGTH_SHORT).show();
                }else {
                    if ("本校学生".equals(enroll_spinner_id.getSelectedItem().toString())){
                        if (enroll_authentication.getDrawable().getCurrent().getConstantState().equals(getResources().getDrawable(R.mipmap.enroll_add_image).getConstantState())) {
                            Toast.makeText(IntoLeRunEnroll.this, "请将学生证图片上传！", Toast.LENGTH_SHORT).show();
                        }else {

                        }
                    }else{}

                    if (rb_sex_man.isChecked()){
                        sex = "男";
                    }else if(rb_sex_woman.isChecked()) {
                        sex = "女";
                    }
                    if (rb_size_s.isChecked()){
                        size = "S";
                    }else if (rb_size_m.isChecked()) {
                        size = "M";
                    }else if (rb_size_l.isChecked()) {
                        size = "L";
                    }else if (rb_size_xl.isChecked()) {
                        size = "XL";
                    }else if (rb_size_2xl.isChecked()) {
                        size = "2XL";
                    }else if (rb_size_3xl.isChecked()) {
                        size = "3XL";
                    }
                }
            }
        });
    }
}
