package com.hairstyle.weshow.common;

/**
 * @Description:
 */
public class WxPayConfig {
    //小程序appid
    public static final String appid = "wxe17d544601c53b2a";
    //微信支付的商户id
    public static final String mch_id = "1528479421";
    //微信支付的商户密钥
    public static final String key = "1qazxsw23edc0okmnji98uhb5tgb7ygv";
//    public static final String key = "58c992f95b3d767e6d288cfa1eab57e0";
    //支付成功后的服务器回调url
    public static final String notify_url = "http://47.94.144.77:9090/weixin/wxnotify";
    //签名方式
    public static final String SIGNTYPE = "MD5";
    //交易类型
    public static final String TRADETYPE_JSAPI = "JSAPI";
    public static final String TRADETYPE_NATIVE = "NATIVE";
    //微信统一下单接口地址
    public static final String pay_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
}
