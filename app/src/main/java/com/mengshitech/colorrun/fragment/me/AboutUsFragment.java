package com.mengshitech.colorrun.fragment.me;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mengshitech.colorrun.MainActivity;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.VersionEntiy;
import com.mengshitech.colorrun.customcontrols.ProgressDialog;
import com.mengshitech.colorrun.customcontrols.UpdateDialog;
import com.mengshitech.colorrun.fragment.lerun.LerunVideo;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.GsonTools;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * atenklsy
 */
public class AboutUsFragment extends Fragment implements View.OnClickListener {
    View me_version_view;
    TextView aboutus_version, aboutus_agreement;
    LinearLayout aboutus_update, aboutus_feedback, aboutus_connection;
    Timer timer;
    ProgressDialog progressDialog;
    UpdateDialog updateDialog;
    Context context;
    String update_url;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        MainActivity.rgMainBottom.setVisibility(View.GONE);
        context = getActivity();
        me_version_view = View.inflate(getActivity(), R.layout.me_aboutus, null);
        MainBackUtility.MainBack(me_version_view, "关于我们", getFragmentManager());
        FindId();
        return me_version_view;
    }

    private void FindId() {
        aboutus_version = (TextView) me_version_view.findViewById(R.id.me_aboutus_version);
        aboutus_update = (LinearLayout) me_version_view.findViewById(R.id.me_aboutus_update);
        aboutus_update.setOnClickListener(this);
        aboutus_feedback = (LinearLayout) me_version_view.findViewById(R.id.me_aboutus_feedback);
        aboutus_feedback.setOnClickListener(this);
        aboutus_connection = (LinearLayout) me_version_view.findViewById(R.id.me_aboutus_connection);
        aboutus_connection.setOnClickListener(this);
        aboutus_agreement = (TextView) me_version_view.findViewById(R.id.me_aboutus_agreement);

        progressDialog = ProgressDialog.show(context, "正在检查更新,请稍后");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_aboutus_update:
                progressDialog.show();

                new Thread(updateRunnable).start();
//                timer.schedule(timerTask, 1000);
                break;
            case R.id.me_aboutus_feedback:
                Utility.replace2DetailFragment(getFragmentManager(), new AboutUsFeedBack());
                break;
            case R.id.me_aboutus_connection:
                Utility.replace2DetailFragment(getFragmentManager(), new AboutUsConnection());
                break;
            case R.id.me_aboutus_agreement:
                break;
        }
    }

    //版本更新
    Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            String servlet = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "aboutus");
            map.put("index", "0");
            map.put("version_number", "v1.0");

            String result = HttpUtils.sendHttpClientPost(servlet, map, "utf-8");
            Message msg = new Message();
            msg.obj = result;
            updateHnaler.sendMessageDelayed(msg, 2000);

        }
    };
    Handler updateHnaler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            Log.i("返回的值", result);
            if (result.equals("timeout")) {
                Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {

                try {
                    String gsonString = JsonTools.getDatas(result);
                    final VersionEntiy entiy = GsonTools.getEntity(gsonString, VersionEntiy.class);

                    Log.i("返回的值", entiy.toString());
                    progressDialog.dismiss();

                    updateDialog = new UpdateDialog(context, R.layout.dialog_updateversion, entiy, R.style.dialog, new UpdateDialog.LeaveMyDialogListener() {
                        @Override
                        public void onClick(View view) {
                            switch (view.getId()) {
                                case R.id.btn_close:
                                    updateDialog.dismiss();
                                    break;
                                case R.id.btn_update:
                                    Uri uri = Uri.parse(entiy.getUpdate_url());
                                    Intent it = new Intent(Intent.ACTION_VIEW, uri);
                                    getActivity().startActivity(it);
                                    updateDialog.dismiss();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });

                    Window window = updateDialog.getWindow();
                    updateDialog.show();
                    window.setGravity(Gravity.CENTER);
                    window.getDecorView().setPadding(0, 0, 0, 0);
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.width = WindowManager.LayoutParams.FILL_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


                    window.setAttributes(lp);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    };


    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            new Thread(updateRunnable).start();
        }
    };
}
