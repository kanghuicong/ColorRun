package com.mengshitech.colorrun.customcontrols;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.VersionEntiy;

/**
 * 作者：wschenyongyin on 2016/8/6 16:11
 * 说明:检查更新的dialog
 */
public class UpdateDialog extends Dialog implements View.OnClickListener {
    Context context;// 上下文联系
    private LeaveMyDialogListener listener;
    int theme;// dialog主题
    View customview;
    int layout;// 自定义的布局
    VersionEntiy entiy;
    TextView tv_version_number;
    TextView tv_update_size;
    TextView tv_update_content;


    public UpdateDialog(Context context, int layout, VersionEntiy entiy, int theme,
                          LeaveMyDialogListener listener) {
        super(context, theme);
        this.context = context;
        this.theme = theme;
        this.layout = layout;
        this.listener = listener;
        this.entiy = entiy;
    }
    public UpdateDialog(Context context, int layout, VersionEntiy entiy,
                        LeaveMyDialogListener listener) {
        super(context);
        this.context = context;
        this.layout = layout;
        this.listener = listener;
        this.entiy = entiy;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout);


        Button btn_close = (Button) findViewById(R.id.btn_close);
        Button btn_update = (Button) findViewById(R.id.btn_update);

        tv_update_content = (TextView) findViewById(R.id.update_content);
        tv_update_size = (TextView) findViewById(R.id.update_size);
        tv_version_number = (TextView) findViewById(R.id.version_number);

        String content;
        content = entiy.getUpdate_content();
        content = content.replace("\\n", "\n");
        tv_update_content.setText(content);
        tv_version_number.setText(entiy.getVersionName());
        tv_update_size.setText(entiy.getUpdate_size());

        btn_close.setOnClickListener(this);
        btn_update.setOnClickListener(this);


    }


    public interface LeaveMyDialogListener {
        public void onClick(View view);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);

    }
}
