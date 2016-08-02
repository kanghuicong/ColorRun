package com.mengshitech.colorrun.fragment.lerun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.adapter.LeRunGridViewAdapter;
import com.mengshitech.colorrun.adapter.LeRunListViewAdapter;
import com.mengshitech.colorrun.adapter.LeRunVpAdapter;
import com.mengshitech.colorrun.bean.LeRunEntity;
import com.mengshitech.colorrun.bean.LunBoEntity;
import com.mengshitech.colorrun.bean.VideoEntity;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;
import com.mengshitech.colorrun.view.MyListView;

import org.json.JSONException;


public class LerunFragment extends Fragment implements OnClickListener {

    //热播视频的图片
    ImageView hotImage;
    //热播视频的url
    String video_url;

    ImageView img_hotfire;
    View lerunView;
    ViewPager vpLeRunAd;
    // 广告首页ViewPager
    List<ImageView> imgList;
    // 广告图片
    TextView tvleRunCity, tvLeRunActivity, tvLeRunTheme, tvLeRunSignUp, tvLeRunFootPrint, tvHotActivity, tvHotVideo;
    // 城市选择按钮
    MyListView lvLerun;
    // 活动的ListView，为了避免冲突，屏蔽了ListView的滑动事件
    GridView gvHotActivity;
    List<LeRunEntity> mLeRunList;
    // 活动的数据源
    Boolean AutoRunning = true;
    FragmentManager fm;
    // 页面布局
    Activity mActivity;
    // 广告栏是否自动滑动

    List<LeRunEntity> gideviewlist;

    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //使用缓存 使fragment保持原有状态
        mActivity = getActivity();
        if (lerunView == null) {
            lerunView = View.inflate(mActivity, R.layout.fragment_lerun, null);
            findById();
        }
        ViewGroup parent = (ViewGroup) lerunView.getParent();
        if (parent != null) {
            parent.removeView(lerunView);
        }
        return lerunView;

    }

    private void findById() {

        //热门视频图片
        hotImage = (ImageView) lerunView.findViewById(R.id.ivHotView);

        vpLeRunAd = (ViewPager) lerunView.findViewById(R.id.vpLeRunAd);
        // 顶部ViewPager滚动栏
        tvLeRunActivity = (TextView) lerunView.findViewById(R.id.tvLeRunActivity);
        // 活动按钮
        tvLeRunTheme = (TextView) lerunView.findViewById(R.id.tvLeRunTheme);
        // 主题按钮
        tvLeRunFootPrint = (TextView) lerunView.findViewById(R.id.tvLeRunFootPrint);
        // 足迹按钮
        tvLeRunSignUp = (TextView) lerunView.findViewById(R.id.tvLeRunSignUp);

        //热门活动标题
        tvHotActivity = (TextView) lerunView.findViewById(R.id.tvHotActivity);
        //热门视频标题
        tvHotVideo = (TextView) lerunView.findViewById(R.id.tvHotVideo);
        // 签到按钮
        tvleRunCity = (TextView) lerunView.findViewById(R.id.tvleRunCity);
        // 城市选择按钮
        lvLerun = (MyListView) lerunView.findViewById(R.id.lvLerun);
        gvHotActivity = (GridView) lerunView.findViewById(R.id.gvHotActivity);
        // 活动的listView
        initView();
    }

    private void initView() {
        initImgList();

        fm = getFragmentManager();
        //初始化fm给ListView、GridView用
//热播图片的点击事件
        hotImage.setOnClickListener(this);

        tvLeRunActivity.setOnClickListener(this);
        Utility.changeTopDrawableSize(tvLeRunActivity, R.mipmap.icon_activity, 80, 80);
        tvLeRunTheme.setOnClickListener(this);
        Utility.changeTopDrawableSize(tvLeRunTheme, R.mipmap.icon_theme, 80, 80);
        tvLeRunFootPrint.setOnClickListener(this);
        Utility.changeTopDrawableSize(tvLeRunFootPrint, R.mipmap.icon_footer, 80, 80);
        tvLeRunSignUp.setOnClickListener(this);
        Utility.changeTopDrawableSize(tvLeRunSignUp, R.mipmap.icon_sign, 80, 80);
//        Utility.changeDrawableSize(img_hotfire,R.mipmap.hot_fire,40,40);
        Utility.changeRightDrawableSize(tvHotActivity, R.mipmap.hot_fire, 30, 30);
        Utility.changeRightDrawableSize(tvHotVideo, R.mipmap.hot_vido, 30, 30);


    }

    //
    private void initImgList() {
        new Thread(getLunBOimageRunnable).start();
        new Thread(getLeRunRunnable).start();
        new Thread(videoRunnable).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLeRunActivity:
                // 活动按钮
                Utility.replace2DetailFragment(fm, new LerunEventListView());
                break;
            case R.id.tvLeRunTheme:
                //主题按钮
                Utility.replace2DetailFragment(fm, new LeRunThemePager());
                break;
            case R.id.tvLeRunFootPrint:
                // 足迹按钮
                Toast.makeText(mActivity, "足迹", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvLeRunSignUp:
                // 签到按钮
                Toast.makeText(mActivity, "签到", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tvleRunCity:
                // 城市选择按钮
                break;
            case R.id.ivHotView:
                Uri uri = Uri.parse(video_url);
                Intent it = new Intent(Intent.ACTION_VIEW, uri);
                mActivity.startActivity(it);
                break;

            default:
                break;
        }
    }


    //获取轮播照片
    Runnable getLunBOimageRunnable = new Runnable() {
        @Override
        public void run() {
            String path = IPAddress.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "lunbo");
            map.put("index", "0");

            String jsonString = HttpUtils.sendHttpClientPost(path, map, "utf-8");
            Log.i("jsonString:", jsonString);

            try {
                List<LunBoEntity> result = JsonTools.getLunboImageInfo("datas", jsonString);
                Log.i("list的数据22:", result.toString() + "");
                Message msg = new Message();
                msg.obj = result;

                LunBOhandler.sendMessage(msg);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }
    };
    //
    Handler LunBOhandler = new Handler() {

        public void handleMessage(Message msg) {
            List<LunBoEntity> list = (List<LunBoEntity>) msg.obj;
            Log.i("list的数据:", list.toString() + "");
            imgList = new ArrayList<ImageView>();
            for (int i = 0; i < list.size(); i++) {
                LunBoEntity entity = list.get(i);
                ImageView img = new ImageView(mActivity);
                img.setScaleType(ScaleType.FIT_XY);
                Glide.with(mActivity).load(entity.getLunbo_image()).into(img);
                imgList.add(img);

            }

            vpLeRunAd
                    .setAdapter(new LeRunVpAdapter(mActivity, imgList, vpLeRunAd, AutoRunning));

        }
    };

    //获取lerun主题信息
    Runnable getLeRunRunnable = new Runnable() {
        @Override
        public void run() {
            String Path = IPAddress.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "lerun");
            map.put("index", "0");
            String result = HttpUtils.sendHttpClientPost(Path, map, "utf-8");
            Log.i("获取主题信息:", result);
            try {
                List<LeRunEntity> lerunlist = JsonTools.getLerunInfo("result", result);
                Log.i("解析后的主题信息:", lerunlist.size() + "");
                Message msg = new Message();
                msg.obj = lerunlist;
                LerunInfohandler.sendMessage(msg);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };

    Handler LerunInfohandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<LeRunEntity> lerunlist = (List<LeRunEntity>) msg.obj;
            Log.i("lerunlist的东西:", lerunlist.size() + "");

            lvLerun.setAdapter(new LeRunListViewAdapter(mActivity, lerunlist, fm,
                    lvLerun));
//热门活动gridview
            gideviewlist = new ArrayList<LeRunEntity>();
            for (int i = 0; i < 2; i++) {
                LeRunEntity entity = lerunlist.get(i);

                gideviewlist.add(entity);
            }
            Log.i("gridlist的大小:", gideviewlist.size() + "");
            gvHotActivity.setAdapter(new LeRunGridViewAdapter(mActivity, gideviewlist, fm, gvHotActivity));


        }
    };


    //获取热门视频信息的线程

    Runnable videoRunnable = new Runnable() {
        @Override
        public void run() {
            String Path = IPAddress.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "lunbo");
            map.put("index", "1");
            String result = HttpUtils.sendHttpClientPost(Path, map, "utf-8");
            Log.i("result信息", result);
            Message msg = new Message();
            msg.obj = result;
            videoHandler.sendMessage(msg);

        }
    };

    Handler videoHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                Toast.makeText(mActivity, "连接服务器超时", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    Log.i("videoImagezhixingle", "内容");
                    List<VideoEntity> list = JsonTools.getVideoInfo(result);

                    VideoEntity entity = list.get(0);
                    Log.i("图片地址", entity.getVideo_url());
                    Glide.with(mActivity).load(entity.getVideo_image()).into(hotImage);
                    video_url = entity.getVideo_url();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    };

}
