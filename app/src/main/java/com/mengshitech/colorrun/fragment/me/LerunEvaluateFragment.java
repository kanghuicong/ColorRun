package com.mengshitech.colorrun.fragment.me;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.bean.CommentEntity;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.MainBackUtility;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：wschenyongyin on 2016/8/13 09:52
 * 说明:
 */
public class LerunEvaluateFragment extends BaseFragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    private View lerun_evaluate_view;
    private Button btn_commit;
    private EditText et_evaluate;
    private RatingBar ratingBar;
    private int evaluate_score;
    private String evaluate_content;
    private int lerun_id;
    private String user_id;
    private int state;
    private String user_telphone;
    private Context context;


    @Override
    public View initView() {
        context=getActivity();

        lerun_evaluate_view = View.inflate(getActivity(), R.layout.fragment_lerunevaluate, null);
        MainBackUtility.MainBack(lerun_evaluate_view, "评价", getFragmentManager());
        init();
        return lerun_evaluate_view;
    }

    private void init() {
        lerun_id = getArguments().getInt("lerun_id");
        user_telphone=getArguments().getString("user_telphone");
        user_id = ContentCommon.user_id;

        ratingBar = (RatingBar) lerun_evaluate_view.findViewById(R.id.rb_evaluatelerun_ratingbar);
        et_evaluate = (EditText) lerun_evaluate_view.findViewById(R.id.et_lerunevaluate);
        btn_commit = (Button) lerun_evaluate_view.findViewById(R.id.btn_commit_evaluate);

        btn_commit.setOnClickListener(this);
        ratingBar.setOnRatingBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        evaluate_content = et_evaluate.getText().toString();
        if (evaluate_content.equals("") && evaluate_content == null) {
            Toast.makeText(context, "请输入您要评价的内容！", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(runnable).start();


        }

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        evaluate_score = (int) rating;

    }

    //提交评价的网络线程
    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "lerun");
            map.put("index", "12");
            map.put("user_id", user_id);
            map.put("lerun_id", lerun_id + "");
            map.put("evaluate_star", evaluate_score + "");
            map.put("evaluate_content", evaluate_content);
            map.put("user_telphone",user_telphone);

            String result = HttpUtils.sendHttpClientPost(ContentCommon.PATH, map, "utf-8");

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

            try {
                state = JsonTools.getState("state", result);
                if (state == 1) {
                    Toast.makeText(context, "评价成功" , Toast.LENGTH_SHORT).show();
                    et_evaluate.setText("");
                    getFragmentManager().popBackStack();
                } else {
                    Toast.makeText(context, "评价失败" , Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

}
