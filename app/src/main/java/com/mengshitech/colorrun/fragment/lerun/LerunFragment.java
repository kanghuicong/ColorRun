package com.mengshitech.colorrun.fragment.lerun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.mengshitech.colorrun.utils.GsonTools;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.IPAddress;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;
import com.mengshitech.colorrun.view.MyListView;

import org.json.JSONException;


public class LerunFragment extends Fragment implements OnClickListener{

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

    List<String> gideviewlist;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = getActivity();
        lerunView = View.inflate(mActivity, R.layout.fragment_lerun, null);
        findById();
        return lerunView;

    }

    private void findById() {

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
        // 初始化ViewPager的图片
        initLeRunList();
        // 初始化listView数据
        fm = getFragmentManager();
        //初始化fm给ListView、GridView用
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


        // 为广告位ViewPager加入数据源、viewpager、是否自动滚动
        lvLerun.setAdapter(new LeRunListViewAdapter(mActivity, mLeRunList, fm,
                lvLerun));
        // 为活动ListView加入数据源、ListView
        gvHotActivity.setAdapter(new LeRunGridViewAdapter(mActivity, gideviewlist, fm, gvHotActivity));
        // 为活动GridView加入数据源、GridView


    }

    private void initLeRunList() {
        // 模拟初始化活动ListView的数据源
        mLeRunList = new ArrayList<LeRunEntity>();
        LeRunEntity mLeRunEntity1 = new LeRunEntity();
        mLeRunEntity1.setLeRunBackgroundId(R.mipmap.poprun);
        mLeRunEntity1.setLeRunLocation("上海浦东外高桥森兰绿地");
        mLeRunEntity1.setLeRunName("泡泡跑");
        mLeRunEntity1.setLeRunTime("08月19日");
        mLeRunList.add(mLeRunEntity1);
        LeRunEntity mLeRunEntity2 = new LeRunEntity();
        mLeRunEntity2.setLeRunBackgroundId(R.mipmap.jxnu);
        mLeRunEntity2.setLeRunLocation("北京朝阳芳草地");
        mLeRunEntity2.setLeRunName("荧光跑");
        mLeRunEntity2.setLeRunTime("09月25日");
        mLeRunList.add(mLeRunEntity2);
        LeRunEntity mLeRunEntity3 = new LeRunEntity();
        mLeRunEntity3.setLeRunBackgroundId(R.mipmap.colorrun);
        mLeRunEntity3.setLeRunLocation("广州海珠广州塔");
        mLeRunEntity3.setLeRunName("卡乐跑");
        mLeRunEntity3.setLeRunTime("11月12日");
        mLeRunList.add(mLeRunEntity3);

        //测试热门活动
        gideviewlist = new ArrayList<String>();
        gideviewlist.add("http://news.qingdaonews.com/images/attachement/jpg/site1/20150606/70f1a1e0059416dd218f0a.jpg");
        gideviewlist.add("http://www.k618.cn/wlsj/299/201506/W020150614338908200476.jpg");

    }

    private void initImgList() {
        // 模拟初始化广告栏的数据源

        new Thread(getLunBOimageRunnable).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvLeRunActivity:
                // 活动按钮
                Toast.makeText(mActivity, "活动", Toast.LENGTH_SHORT).show();
                Utility.replace2DetailFragment(fm, new LerunEventListView());
                break;
            case R.id.tvLeRunTheme:
                //主题按钮
                Toast.makeText(mActivity, "主题", Toast.LENGTH_SHORT).show();
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
                    .setAdapter(new LeRunVpAdapter(mActivity,imgList, vpLeRunAd, AutoRunning));

        }
    };

    //获取lerun主题信息
    Runnable getLeRunRunnable = new Runnable() {
        @Override
        public void run() {
            String Path=IPAddress.PATH;
            Map<String,String> map=new HashMap<String,String>();
            map.put("flag","lerun");
            map.put("index","0");
            String result=HttpUtils.sendHttpClientPost(Path,map,"utf-8");


        }
    };



}
