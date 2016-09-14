package com.mengshitech.colorrun.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.mengshitech.colorrun.bean.ShowEntity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kk on 2016/9/13.
 */
public class StorageState {
    static List<ShowEntity> mShowList;
    static List<ShowEntity> MyshowList;
    static ShowEntity mShow;
    static ShowEntity Myshow;

    public StorageState(){}

    public static void CheckConmentState(){
        new Thread(runnable).start();
    }

    public static void DeleteShowState(String show_id){
        MyshowList = ContentCommon.MyshowList;
        for (int i = 0; i != MyshowList.size(); i++){
            Myshow = MyshowList.get(i);
            if (Myshow.getShow_id().equals(show_id)){
                ContentCommon.MyshowList.remove(i);
            }
        }
    }

    public static void AddShowState(String show_id){
        ShowEntity showEntity = new ShowEntity();
        showEntity.setShow_id(show_id);
        showEntity.setComment_num("0");
        ContentCommon.MyshowList.add(showEntity);
    }


    static Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "show");
            map.put("index", "3");
            map.put("user_id", ContentCommon.user_id);

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
            MyshowList = ContentCommon.MyshowList;
            try {
                mShowList = JsonTools.getShowInfo("result", result);
                if (mShowList != null) {
                    if (MyshowList == null) {
                        ContentCommon.MyshowList = new ArrayList<ShowEntity>();
                        ContentCommon.MyshowList.addAll(mShowList);
                    } else {
                        if (ContentCommon.MyshowState.equals("0")) {
                            ContentCommon.MyshowStateList = new ArrayList<String>();
                        }
                            for (int i = 0; i < MyshowList.size(); i++) {
                                Myshow = MyshowList.get(i);
                                for (int j = 0; j < mShowList.size(); j++) {
                                    mShow = mShowList.get(j);
                                    if (Myshow.getShow_id().equals(mShow.getShow_id()) && Integer.valueOf(Myshow.getComment_num()) < Integer.valueOf(mShow.getComment_num())) {
                                        if (!mShow.getUser_id().equals(ContentCommon.user_id)) {
                                            ContentCommon.MyshowState = "1";
                                            ContentCommon.myshowstate = "1";
                                            ContentCommon.MyshowStateList.add(Myshow.getShow_id());
                                        }
                                    }
                                }
                            }
                            ContentCommon.MyshowList.clear();
                            ContentCommon.MyshowList.addAll(mShowList);
//                        }else {
//                            ContentCommon.MyshowList.clear();
//                            ContentCommon.MyshowList.addAll(mShowList);
//                        }
                    }
                }else {
                    ContentCommon.MyshowList = null;
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };
}
