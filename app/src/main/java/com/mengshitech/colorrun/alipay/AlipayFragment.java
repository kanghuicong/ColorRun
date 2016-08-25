package com.mengshitech.colorrun.alipay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.mengshitech.colorrun.R;
import com.mengshitech.colorrun.fragment.BaseFragment;
import com.mengshitech.colorrun.fragment.lerun.DisplayQRcodeFragment;
import com.mengshitech.colorrun.utils.CompressImage;
import com.mengshitech.colorrun.utils.ContentCommon;
import com.mengshitech.colorrun.utils.HttpUtils;
import com.mengshitech.colorrun.utils.JsonTools;
import com.mengshitech.colorrun.utils.Utility;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * 作者：wschenyongyin on 2016/8/4 10:53
 * 说明:
 */
public class AlipayFragment extends Fragment implements View.OnClickListener {
    private TextView payment_name, payment_title, payment_price;
    private View payment_view;
    private RelativeLayout rl_zfb, rl_wx;
    private ImageView iv_zfb, iv_wx;
    private String pay_way = "null";
    private Context context;
    private Button btn_pay;
    private String user_name;
    private String lerun_title;
    private String lerun_price;
    private String qrcode_image;
    private String user_id;
    private String user_telphone;
    private String payresult;

    public static final String PARTNER = "2088421712740222";
    // 商户收款账号
    public static final String SELLER = "752664184@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAN+YiddUabYk2z92rn9ed4/zZN8Fz0rB+KMweWnWjlVHdpnLfqNEO7jf37eMAd+3PaVeMXZ1egpnHXyooLTtpf1g+3YHTFmaOGOLpHy2Tj5eU0y6ljjnweQZ7rM0qe/D3ool84UIZzqUbgPnt1rkEZkjwIGx8A8kNeWZcaj77xshAgMBAAECgYB/bcmxBJSyj9K8GoFcaZQuYAJu8DqxWla/elLXtMWtaGr5P3ZOygZXWI+BZbNzslTZuBLsdgs1forZjqj4NDBSZCMzMCYgbrtYSVoNIWr92VHLX4onGaj8LEACZoBnF1zsgO1ErzRkzE7UQYNKHrbUiPN13x0npGJwNhzs6WKMmQJBAPzGZVlUMWese8Y9Rqzdi44YCSz0ze3xYBBRqBo5W0jmPAeRAHbruOTLt4bLMdiHpTh3K5AY3L4t84hbDaWp3/cCQQDicta51OVIMXcx4ySvnBqDd9zpO1QqrvybKl6/O9x2qlyG6UIRFZmdn76pRv4/hwY/l3snbS+6hxIuY3mj48enAkAEjxd42vnhIs1AsA48Q+qmb2yK8QddyUKwSKi9gFdTI0Pl5wmZG3tENSBkP/nwK9IhCJUyjiA9FdsUlH/UgxHVAkEA2SS9+xzHcF7eqZvihfLvCbpav9wAbZ225SPQDxjb436hk00B6VgJIjkYn0JQc6KKv1gG5FuzNO5o5MrGzf2SaQJBANqYIsLqTBpTpMX1uuh1RxyNDy3d/f00acSpHMsPaMITz52wdcHLwyZuW46Dwv/3v2OoraoYDA2mSPzy2u1coFI=";    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDfmInXVGm2JNs/dq5/XneP82Tf Bc9KwfijMHlp1o5VR3aZy36jRDu439+3jAHftz2lXjF2dXoKZx18qKC07aX9YPt2 B0xZmjhji6R8tk4+XlNMupY458HkGe6zNKnvw96KJfOFCGc6lG4D57da5BGZI8CB sfAPJDXlmXGo++8bIQIDAQAB";
    public static final int SDK_PAY_FLAG = 1;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        payment_view = inflater.inflate(R.layout.lerun_enroll_payment, null);
        context = getActivity();
        FindId();
        GetData();
        return payment_view;
    }

    private void GetData() {
        user_name = getArguments().getString("user_name");
        lerun_title = getArguments().getString("lerun_title");
        qrcode_image = getArguments().getString("qrcode_image");
        lerun_price = getArguments().getInt("lerun_price") + "";
        user_id = ContentCommon.user_id;
        user_telphone = getArguments().getString("user_telphone");

        payment_name.setText("姓名：" + getArguments().getString("user_name"));
        payment_title.setText(getArguments().getString("lerun_title"));
        payment_price.setText(getArguments().getInt("lerun_price") + "");
    }

    private void FindId() {
        payment_name = (TextView) payment_view.findViewById(R.id.tv_payment_name);
        payment_title = (TextView) payment_view.findViewById(R.id.tv_payment_title);
        payment_price = (TextView) payment_view.findViewById(R.id.tv_payment_price);
        btn_pay = (Button) payment_view.findViewById(R.id.btn_play);
        rl_zfb = (RelativeLayout) payment_view.findViewById(R.id.rl_zfb);
        rl_zfb.setOnClickListener(this);
        iv_zfb = (ImageView) payment_view.findViewById(R.id.iv_payment_zfb);
        rl_wx = (RelativeLayout) payment_view.findViewById(R.id.rl_wxzf);
        rl_wx.setOnClickListener(this);
        iv_wx = (ImageView) payment_view.findViewById(R.id.iv_payment_wx);

        btn_pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_zfb:
                iv_zfb.setImageResource(R.mipmap.checkbox_pressed);
                iv_wx.setImageResource(R.mipmap.checkbox_normal);
                pay_way = "zfb";
                break;
            case R.id.rl_wxzf:
                iv_zfb.setImageResource(R.mipmap.checkbox_normal);
                iv_wx.setImageResource(R.mipmap.checkbox_pressed);
                pay_way = "wx";
                break;
            case R.id.btn_play:

                if (pay_way.equals("zfb")) {
                    //支付宝支付
                    pay();
                } else if (pay_way.equals("wx")) {
                    //微信支付
                    Toast.makeText(getActivity(), "微信支付还未完善", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "请选择付款方式", Toast.LENGTH_SHORT).show();
                }

                break;

            default:
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档

                    Log.i("支付宝状态:", resultStatus + "");
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_LONG).show();
                        payresult=payResult.getResult();
                        new Thread(runnable).start();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    public void pay() {

        //订单信息
        String orderInfo = getOrderInfo(lerun_title+"(卡乐体育)", user_id+"", lerun_price);

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(getActivity());
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(getActivity());
        String version = payTask.getVersion();
        Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(context, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url是测试的网站，在app内部打开页面是基于webview打开的，demo中的webview是H5PayDemoActivity，
         * demo中拦截url进行支付的逻辑是在H5PayDemoActivity中shouldOverrideUrlLoading方法实现，
         * 商户可以根据自己的需求来实现
         */
        String url = "http://m.taobao.com";
        // url可以是一号店或者淘宝等第三方的购物wap站点，在该网站的支付过程中，支付宝sdk完成拦截支付
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * create the order info. 创建订单信息
     */
    private String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType() {
        return "sign_type=\"RSA\"";
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String path = ContentCommon.PATH;
            Map<String, String> map = new HashMap<String, String>();
            map.put("flag", "lerun");
            map.put("index", "5");
            map.put("user_id", user_id);
            map.put("payment", lerun_price);
            map.put("user_phone", user_telphone);
            map.put("payResult",payresult+"");

            String result = HttpUtils.sendHttpClientPost(path, map, "utf-8");
            Message msg = new Message();
            msg.obj = result;
            handle.sendMessage(msg);
        }
    };

    Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.equals("timeout")) {
                //连接服务器超时

            } else {
                try {
                    int state = JsonTools.getState("state", result);
                    if (state == 1) {
                        Bundle bundle = new Bundle();
                        bundle.putString("qrcode_image", qrcode_image + "");
                        bundle.putInt("type", 5);
                        DisplayQRcodeFragment displayQRcodeFragment = new DisplayQRcodeFragment();

                        displayQRcodeFragment.setArguments(bundle);
                        Utility.replace2DetailFragment(getFragmentManager(), displayQRcodeFragment);
                    } else {
                        //更新付款状态失败
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

}
