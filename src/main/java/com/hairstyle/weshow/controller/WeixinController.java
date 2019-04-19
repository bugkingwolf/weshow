package com.hairstyle.weshow.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.weixin4j.WeixinException;
import org.weixin4j.WeixinSupport;
import org.weixin4j.http.HttpsClient;
import org.weixin4j.http.Response;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hairstyle.weshow.body.GlobalErrorMessage;
import com.hairstyle.weshow.body.HttpRequestBody;
import com.hairstyle.weshow.body.HttpResponseBody;
import com.hairstyle.weshow.common.OrderStatusEnum;
import com.hairstyle.weshow.common.WxPayConfig;
import com.hairstyle.weshow.dao.OrderInfoMapper;
import com.hairstyle.weshow.dao.WxpayFlowInfoMapper;
import com.hairstyle.weshow.domain.DecryptDataInfo;
import com.hairstyle.weshow.domain.OrderInfo;
import com.hairstyle.weshow.domain.WxpayFlowInfo;
import com.hairstyle.weshow.service.CustomerService;
import com.hairstyle.weshow.utils.ConvertUtils;
import com.hairstyle.weshow.utils.IpUtils;
import com.hairstyle.weshow.utils.JsonUtils;
import com.hairstyle.weshow.utils.StringUtils;
import com.hairstyle.weshow.utils.weixin.PayUtil;
import com.hairstyle.weshow.utils.weixin.WxUtils;
import com.hairstyle.weshow.utils.weixin.vo.OAuthJsToken;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/weixin")
@RestController
public class WeixinController extends WeixinSupport {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String appid = "wxe17d544601c53b2a";        //微信小程序appid
    private static final String secret = "58c992f95b3d767e6d288cfa1eab57e0";    //微信小程序密钥
    private static final String grant_type = "authorization_code";

    @Autowired
    CustomerService customerServiceImpl;
    @Autowired
    WxpayFlowInfoMapper wxpayFlowInfoMapper;
    @Autowired
    OrderInfoMapper orderInfoMapper;

    @PostMapping("/login")
    public HttpResponseBody login(HttpServletRequest request, @RequestBody(required = true) String body) {
        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Integer> result = new HashMap<>();
        try {

        	int status = 0;//0失败 1成功
        	
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用小程序登录注册接口【login】开始，请求参数：" + bizContent);
            JSONObject jsonObject = JSON.parseObject(bizContent);

            String encryptedData = jsonObject.get("encryptedData") + "";
            String iv = jsonObject.get("iv") + "";
            String code = jsonObject.get("code") + "";
            OAuthJsToken oAuthJsToken = getOAuthJsToken(code);
            String sessionKey = oAuthJsToken.getSession_key();
            String decryptData = WxUtils.decryptData(encryptedData, sessionKey, iv);
            log.info("调用小程序登录注册接口【login】decryptData：" + decryptData);

            DecryptDataInfo decryptDataInfo = JsonUtils.fromJSON(decryptData, DecryptDataInfo.class);
            int customerId = customerServiceImpl.login(decryptDataInfo);
            if(customerId != 0){
            	//成功
            	status = 1;
            	result.put("customerId", customerId);
            }
            result.put("status", status);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            log.info("调用小程序登录注册接口【login】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用小程序登录注册接口【login】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @PostMapping("/getopenid")
    public HttpResponseBody getOpenId(HttpServletRequest request, @RequestBody(required = true) String body) {
        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Object> result = new HashMap<>();
        try {

            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取openid接口【getOpenId】开始，请求参数：" + bizContent);
            JSONObject jsonObject = JSON.parseObject(bizContent);

            String code = jsonObject.get("code") + "";
            OAuthJsToken oAuthJsToken = getOAuthJsToken(code);

            String openid = oAuthJsToken.getOpenid();

            logger.info("openid=" + openid);
            logger.info("sessionKey=" + oAuthJsToken.getSession_key());
            result.put("openid", openid);
            result.put("sessionKey", oAuthJsToken.getSession_key());

            DecryptDataInfo decryptDataInfo = new DecryptDataInfo();
            decryptDataInfo.setOpenId(openid);
//            int status  = customerServiceImpl.login(decryptDataInfo);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            log.info("调用获取openid接口【getOpenId】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用获取openid接口【getOpenId】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }


    /**
     * @param body
     * @param request
     * @Description: 发起微信支付
     */
    @RequestMapping("wxpay")
    public HttpResponseBody wxPay(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Object> result = new HashMap<>();
        try {


            log.info("调用微信统一下单接口【wxPay】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用微信统一下单接口【wxPay】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("amount") == null || StringUtils.isEmpty(paramMap.get("amount").toString().trim())
                    || paramMap.get("orderId") == null || StringUtils.isEmpty(paramMap.get("orderId").toString().trim())
                    || paramMap.get("openId") == null || StringUtils.isEmpty(paramMap.get("openId").toString().trim())
                    || paramMap.get("payType") == null || StringUtils.isEmpty(paramMap.get("payType").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }


            //生成的随机字符串
            String nonce_str = StringUtils.getRandomStringByLength(32);
            //商品名称
            String body2 = "测试商品名称";
            //获取本机的ip地址
            String spbill_create_ip = IpUtils.getIpAddr(request);

            //===================================================================================
//            String orderNo = "10";
//            String money = "1";//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败

            String orderNo = paramMap.get("orderId") + "";
            String money = paramMap.get("amount") + "";//支付金额，单位：分，这边需要转成字符串类型，否则后面的签名会失败
            Integer payType = Integer.parseInt(paramMap.get("payType") + "");//支付方式 1小程序支付 2扫码支付
            String openId = "";//支付方式 1小程序支付 2扫码支付
            if (payType == 1) {
                openId = paramMap.get("openId") + "";
            }
            //tradeType 1小程序支付 2扫码支付
            String tradeType = WxPayConfig.TRADETYPE_JSAPI;
            if (payType == 1) {
                tradeType = WxPayConfig.TRADETYPE_JSAPI;
            } else if (payType == 2) {
                tradeType = WxPayConfig.TRADETYPE_NATIVE;
            }
            //===================================================================================

            Map<String, String> packageParams = new HashMap<String, String>();
            packageParams.put("appid", WxPayConfig.appid);
            packageParams.put("mch_id", WxPayConfig.mch_id);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("body", body2);
            packageParams.put("out_trade_no", orderNo);//商户订单号
            packageParams.put("total_fee", money);//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            packageParams.put("spbill_create_ip", spbill_create_ip);
            packageParams.put("notify_url", WxPayConfig.notify_url);
            packageParams.put("trade_type", tradeType);
            if (payType == 1) {
                packageParams.put("openid", openId);
            }

            // 除去数组中的空值和签名参数
            packageParams = PayUtil.paraFilter(packageParams);
            String prestr = PayUtil.createLinkString(packageParams); // 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串

            //MD5运算生成签名，这里是第一次签名，用于调用统一下单接口
            String mysign = PayUtil.sign(prestr, WxPayConfig.key, "utf-8").toUpperCase();
            log.info("=======================第一次签名：" + mysign + "=====================");

            //拼接统一下单接口使用的xml数据，要将上一步生成的签名一起拼接进去
            String xml = "";
            if (payType == 1) {

                xml = "<xml>" + "<appid>" + WxPayConfig.appid + "</appid>"
                        + "<body><![CDATA[" + body2 + "]]></body>"
                        + "<mch_id>" + WxPayConfig.mch_id + "</mch_id>"
                        + "<nonce_str>" + nonce_str + "</nonce_str>"
                        + "<notify_url>" + WxPayConfig.notify_url + "</notify_url>"
                        + "<openid>" + openId + "</openid>"
                        + "<out_trade_no>" + orderNo + "</out_trade_no>"
                        + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                        + "<total_fee>" + money + "</total_fee>"
                        + "<trade_type>" + tradeType + "</trade_type>"
                        + "<sign>" + mysign + "</sign>"
                        + "</xml>";
            } else if (payType == 2) {

                xml = "<xml>" + "<appid>" + WxPayConfig.appid + "</appid>"
                        + "<body><![CDATA[" + body2 + "]]></body>"
                        + "<mch_id>" + WxPayConfig.mch_id + "</mch_id>"
                        + "<nonce_str>" + nonce_str + "</nonce_str>"
                        + "<notify_url>" + WxPayConfig.notify_url + "</notify_url>"
                        + "<out_trade_no>" + orderNo + "</out_trade_no>"
                        + "<spbill_create_ip>" + spbill_create_ip + "</spbill_create_ip>"
                        + "<total_fee>" + money + "</total_fee>"
                        + "<trade_type>" + tradeType + "</trade_type>"
                        + "<sign>" + mysign + "</sign>"
                        + "</xml>";
            }

            log.info("调试模式_统一下单接口 请求XML数据：" + xml);

            //调用统一下单接口，并接受返回的结果
            String res = PayUtil.httpRequest(WxPayConfig.pay_url, "POST", xml);

            log.info("调试模式_统一下单接口 返回XML数据：" + res);

            // 将解析结果存储在HashMap中
            Map map = PayUtil.doXMLParse(res);

            String return_code = (String) map.get("return_code");//返回状态码

            //返回给移动端需要的参数
//            Map<String, Object> response = new HashMap<String, Object>();
            if (return_code == "SUCCESS" || return_code.equals(return_code)) {
                // 业务结果
                String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
                String code_url = (String) map.get("code_url");//返回的预付单信息
                Long timeStamp = System.currentTimeMillis() / 1000;
                String stringSignTemp = "appId=" + WxPayConfig.appid + "&nonceStr=" + nonce_str + "&package=prepay_id=" + prepay_id + "&signType=" + WxPayConfig.SIGNTYPE + "&timeStamp=" + timeStamp;
                //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
                String paySign = PayUtil.sign(stringSignTemp, WxPayConfig.key, "utf-8").toUpperCase();
                log.info("=======================第二次签名：" + paySign + "=====================");

                result.put("nonceStr", nonce_str);//随机字符串
                result.put("package", "prepay_id=" + prepay_id);//返回的预付单信息
                result.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
                result.put("paySign", paySign);//第二次签名
                result.put("appid", WxPayConfig.appid);
                result.put("codeurl", code_url);//二维码Url

                //更新订单信息
                //业务逻辑代码
            }

            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("调用微信统一下单接口【wxPay】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用微信统一下单接口【wxPay】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    /**
     * @return
     * @throws Exception
     * @throws WeixinException
     * @Description:微信支付
     */
    @RequestMapping(value = "/wxnotify")
    public void wxNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        //sb为微信返回的xml
        String notityXml = sb.toString();
        String resXml = "";
        log.info("======接收到微信支付回调的报文：" + notityXml);

        Map map = PayUtil.doXMLParse(notityXml);

        String returnCode = (String) map.get("return_code");
        if ("SUCCESS".equals(returnCode)) {
            //验证签名是否正确
            if (PayUtil.verify(PayUtil.createLinkString(map), (String) map.get("sign"), WxPayConfig.key, "utf-8")) {

                /**此处添加自己的业务逻辑代码start**/

                Integer orderId = Integer.parseInt(map.get("out_trade_no") + "");//商户订单号
                String transactionId = map.get("transaction_id") + "";//微信支付订单号
                String timeEnd = map.get("time_end") + "";//支付完成时间
                String cashFee = map.get("cash_fee") + "";//现金支付金额
                String totalFee = map.get("total_fee") + "";//订单金额
                String openId = map.get("openid") + "";//openid
                String tradeType = map.get("trade_type") + "";//trade_type

                if (WxPayConfig.TRADETYPE_JSAPI.equals(tradeType)) {
                    //更新订单状态
                    OrderInfo orderInfo = new OrderInfo();
                    orderInfo.setOrderId(orderId);
                    orderInfo.setStatus(OrderStatusEnum.WATI_CONFIRM.getCode());
                    orderInfoMapper.updateByPrimaryKey(orderInfo);
                }

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                Date timeEndDate = sdf.parse(timeEnd);

                WxpayFlowInfo wxpayFlowInfo = new WxpayFlowInfo();
                wxpayFlowInfo.setAmount(Integer.parseInt(totalFee));
                wxpayFlowInfo.setOpenId(openId);
                wxpayFlowInfo.setOrderId(orderId);
                wxpayFlowInfo.setTransactionId(transactionId);
                wxpayFlowInfo.setPayTime(timeEndDate);
                if (WxPayConfig.TRADETYPE_JSAPI.equals(tradeType)) {
                    wxpayFlowInfo.setTradeType(1);
                } else if (WxPayConfig.TRADETYPE_NATIVE.equals(tradeType)) {
                    wxpayFlowInfo.setTradeType(2);
                }
                int insert = wxpayFlowInfoMapper.insertSelective(wxpayFlowInfo);

                /**此处添加自己的业务逻辑代码end**/

                //通知微信服务器已经支付成功
                resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                        + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
            }
        } else {
            resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                    + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        log.info("===微信支付回调返回数据" + resXml);
        log.info("===微信支付回调数据结束");

        BufferedOutputStream out = new BufferedOutputStream(
                response.getOutputStream());
        out.write(resXml.getBytes());
        out.flush();
        out.close();
    }

    public OAuthJsToken getOAuthJsToken(String code) throws WeixinException, IOException {
        if (code == null || code.equals("")) {
            throw new WeixinException("invalid null, code is null.");
        }

        OAuthJsToken oauthJsToken = new OAuthJsToken();
        Map<String, Object> ret = new HashMap<String, Object>();
        //拼接参数
        String param = "?grant_type=" + grant_type + "&appid=" + appid + "&secret=" + secret + "&js_code=" + code;

        //创建请求对象
        HttpsClient http = new HttpsClient();
        //调用获取access_token接口
        Response res = http.get("https://api.weixin.qq.com/sns/jscode2session" + param);
        //根据请求结果判定，是否验证成功
        JSONObject jsonObj = res.asJSONObject();
        if (jsonObj != null) {
            Object errcode = jsonObj.get("errcode");
            if (errcode != null) {
                //返回异常信息
                throw new WeixinException(getCause(Integer.parseInt(errcode.toString())));
            }

            ObjectMapper mapper = new ObjectMapper();
            oauthJsToken = mapper.readValue(jsonObj.toJSONString(), OAuthJsToken.class);

            logger.info("openid=" + oauthJsToken.getOpenid());
            logger.info("sessionKey=" + oauthJsToken.getSession_key());
            ret.put("openid", oauthJsToken.getOpenid());
            ret.put("sessionKey", oauthJsToken.getSession_key());
        }
        return oauthJsToken;
    }

    public static void main(String[] args) {

    }
}
