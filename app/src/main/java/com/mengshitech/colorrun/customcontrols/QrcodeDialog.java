package com.mengshitech.colorrun.customcontrols;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.utils.IPAddress;

/**
 * 作者：wschenyongyin on 2016/8/3 09:36
 * 说明:
 */
public class QrcodeDialog extends Dialog implements
        View.OnClickListener {
    int layout;// 自定义的布局
    Context context;// 上下文联系
    int theme;// dialog主题
    View customview;
    private QrcodeDialogListener listener;
    String qrcode_image;

    // // 传入布局，activity
    public QrcodeDialog(Context context, int layout) {

        super(context, layout);
        this.context = context;
        this.layout = layout;
        customview = View.inflate(context, layout, null);
    }

    // 传入布局，activity，主题
    public QrcodeDialog(Context context, int layout, int theme, QrcodeDialogListener listener, String qrcode_image) {
        super(context, theme);
        this.context = context;
        this.theme = theme;
        this.layout = layout;
        this.listener = listener;
        this.qrcode_image = qrcode_image;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout);

        Log.i("oncreat", "success");


       LinearLayout btn_ok = (LinearLayout) findViewById(R.id.btn_qrcde_ok);
        ImageView image_qrcode = (ImageView) findViewById(R.id.image_qrcode);
        if (qrcode_image != null) {
            Glide.with(context).load(IPAddress.path + qrcode_image).into(image_qrcode);
        }
       btn_ok.setOnClickListener(this);

    }

    public interface QrcodeDialogListener {
        public void onClick(View view);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);

    }
}
