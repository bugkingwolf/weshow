package com.hairstyle.weshow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hairstyle.weshow.body.GlobalErrorMessage;
import com.hairstyle.weshow.body.HttpRequestBody;
import com.hairstyle.weshow.body.HttpResponseBody;
import com.hairstyle.weshow.domain.OrderInfo;
import com.hairstyle.weshow.service.OrderService;
import com.hairstyle.weshow.service.StoreService;
import com.hairstyle.weshow.utils.ConvertUtils;
import com.hairstyle.weshow.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    StoreService storeService;
    @Autowired
    OrderService ordreService;

    @PostMapping("/insert")
    public HttpResponseBody insert(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, OrderInfo> result = new HashMap<>();
        try {
            log.info("调用下订单接口【insert】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用下订单接口【insert】开始，请求参数：" + bizContent);

            OrderInfo orderInfo = JsonUtils.fromJSON(bizContent, OrderInfo.class);
            OrderInfo order = ordreService.insert(orderInfo);
            result.put("orderInfo", order);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            log.info("调用下订单接口【insert】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用下订单接口【insert】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @PostMapping("/validatecode")
    public HttpResponseBody validateCode(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Integer> result = new HashMap<>();
        try {
            log.info("调用验证预约码接口【validateCode】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用验证预约码接口【validateCode】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("code") == null || StringUtils.isEmpty(paramMap.get("code").toString().trim())
                    || paramMap.get("orderId") == null || StringUtils.isEmpty(paramMap.get("orderId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }
            int orderId = Integer.parseInt(paramMap.get("orderId") + "");
            int code = Integer.parseInt(paramMap.get("code") + "");
            int status = ordreService.validateCode(orderId, code);
            result.put("status", status);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            log.info("调用验证预约码接口【validateCode】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
            e.printStackTrace();
        } finally {
            log.info("调用验证预约码接口【validateCode】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/getbarberorderlist")
    public HttpResponseBody getBarberOrderList(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, List<OrderInfo>> result = new HashMap<>();
        try {
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取订单列表接口【getBarberOrderList】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            OrderInfo orderInfo = JsonUtils.fromJSON(bizContent, OrderInfo.class);
            List<OrderInfo> orders = ordreService.getBarberOrderlist(orderInfo);
            result.put("order", orders);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            log.info("调用获取订单列表接口【getBarberOrderList】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
            e.printStackTrace();
        } finally {
            log.info("调用获取订单列表接口【getBarberOrderList】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/getcustomerorderlist")
    public HttpResponseBody getCustomerOrderList(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, List<OrderInfo>> result = new HashMap<>();
        try {
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取顾客订单列表接口【getCustomerOrderList】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("customerId") == null || StringUtils.isEmpty(paramMap.get("customerId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            OrderInfo orderInfo = JsonUtils.fromJSON(bizContent, OrderInfo.class);
            List<OrderInfo> orders = ordreService.getCustomerOrderlist(orderInfo);
            result.put("order", orders);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            log.info("调用获取顾客订单列表接口【getCustomerOrderList】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
            e.printStackTrace();
        } finally {
            log.info("调用获取顾客订单列表接口【getCustomerOrderList】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/getorderdetail")
    public HttpResponseBody getOrderDetail(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, OrderInfo> result = new HashMap<>();
        try {
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取订单详情接口【getOrderDetail】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("orderId") == null || StringUtils.isEmpty(paramMap.get("orderId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            OrderInfo orderInfo = JsonUtils.fromJSON(bizContent, OrderInfo.class);
            OrderInfo orders = ordreService.getOrderDetail(orderInfo);
            result.put("order", orders);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            log.info("调用获取订单详情接口【getOrderDetail】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
            e.printStackTrace();
        } finally {
            log.info("调用获取订单详情接口【getOrderDetail】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/orderconfirm")
    public HttpResponseBody orderConfirm(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Integer> result = new HashMap<>();
        try {
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用确认订单接口【orderConfirm】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("orderId") == null || StringUtils.isEmpty(paramMap.get("orderId").toString().trim())
                    || paramMap.get("type") == null || StringUtils.isEmpty(paramMap.get("type").toString().trim())
                    || paramMap.get("remark") == null || StringUtils.isEmpty(paramMap.get("remark").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            OrderInfo orderInfo = JsonUtils.fromJSON(bizContent, OrderInfo.class);
            int status = ordreService.orderConfirm(orderInfo);
            result.put("status", status);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            log.info("调用确认订单接口【orderConfirm】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用确认订单接口【orderConfirm】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    /**
     * 订单绑定人脸照片(开始理发最后一步)
     *
     * @param request
     * @param body
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/bingfaceid")
    public HttpResponseBody bindFaceId(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Integer> result = new HashMap<>();
        int status;
        try {
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用订单绑定人脸照片接口【bindFaceId】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("orderId") == null || StringUtils.isEmpty(paramMap.get("orderId").toString().trim())
                    || paramMap.get("faceId") == null || StringUtils.isEmpty(paramMap.get("faceId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }
            Integer orderId = Integer.parseInt(String.valueOf(paramMap.get("orderId")));
            Integer haircutImgId = Integer.parseInt(String.valueOf(paramMap.get("haircutImgId")));

            status = ordreService.updateImage(orderId, haircutImgId);

            result.put("status", status);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            status = 0;
            result.put("status", status);
            httpResponseBody.setBizContent(result);
            log.info("调用订单绑定人脸照片接口【bindFaceId】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用订单绑定人脸照片接口【bindFaceId】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }


    @SuppressWarnings("rawtypes")
    @PostMapping("/finish")
    public HttpResponseBody finishHaircut(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Integer> result = new HashMap<>();
        int status;
        try {
            log.info("调用结束理发接口【finishHaircut】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用结束理发接口【finishHaircut】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("orderId") == null || StringUtils.isEmpty(paramMap.get("orderId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }
            Integer orderId = Integer.parseInt(String.valueOf(paramMap.get("orderId")));

            status = ordreService.finishHaircut(orderId);

            result.put("status", status);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            status = 0;
            result.put("status", status);
            httpResponseBody.setBizContent(result);
            log.info("调用结束理发接口【finishHaircut】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用结束理发接口【finishHaircut】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

}
