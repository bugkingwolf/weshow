package com.hairstyle.weshow.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hairstyle.weshow.body.GlobalErrorMessage;
import com.hairstyle.weshow.body.HttpRequestBody;
import com.hairstyle.weshow.body.HttpResponseBody;
import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.CustomerAccountInfo;
import com.hairstyle.weshow.domain.CustomerFaceInfo;
import com.hairstyle.weshow.domain.CustomerIncomeInfo;
import com.hairstyle.weshow.domain.CustomerInfo;
import com.hairstyle.weshow.service.CustomerService;
import com.hairstyle.weshow.service.FaceService;
import com.hairstyle.weshow.utils.AliyunOSSClientUtil;
import com.hairstyle.weshow.utils.ConvertUtils;
import com.hairstyle.weshow.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("customer")
public class CustomerController {

    @Autowired
    CustomerService customerService;
    @Autowired
    FaceService faceService;

    /**
     * 获取百度云 用户人脸信息接口
     *
     * @param request
     * @param body
     * @param faceImgFile
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/getfaceinfo")
    public HttpResponseBody getFaceInfo(HttpServletRequest request, @RequestPart(required = true) String body,
                                        @RequestBody(required = true) MultipartFile faceImgFile) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, CustomerFaceInfo> result = new HashMap<>();

        try {
            log.info("调用获取用户人脸信息接口【getFaceInfo】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取用户人脸信息接口【getFaceInfo】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("customerId") == null || StringUtils.isEmpty(paramMap.get("customerId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }
            //上传阿里云
            AliyunOSSClientUtil ossClient = new AliyunOSSClientUtil();
            String imgUrl = ossClient.uploadImg2Oss(faceImgFile);
            //转换数据
            CustomerInfo customerInfo = JsonUtils.fromJSON(bizContent, CustomerInfo.class);
            //顾客id
            Integer customerId = customerInfo.getCustomerId();
            //获取人脸数据
            CustomerFaceInfo faceInfo = faceService.getFaceInfo(customerId, imgUrl);

            result.put("faceinfo", faceInfo);
            httpResponseBody.setBizContent(result);

        } catch (Exception e) {
            log.info("调用获取用户人脸信息接口【getFaceInfo】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
            e.printStackTrace();
        } finally {
            log.info("调用获取用户人脸信息接口【getFaceInfo】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    /**
     * 获取顾客个人信息
     *
     * @param request
     * @param body
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/getcustomerinfo")
    public HttpResponseBody getCustomerInfo(HttpServletRequest request, @RequestBody(required = true) String body) {
        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, CustomerInfo> result = new HashMap<>();
        try {

            log.info("调用获取顾客个人信息接口【getCustomerInfo】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取顾客个人信息接口【getCustomerInfo】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("customerId") == null || StringUtils.isEmpty(paramMap.get("customerId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            CustomerInfo customerInfo = JsonUtils.fromJSON(bizContent, CustomerInfo.class);
            Integer customerId = customerInfo.getCustomerId();
            CustomerInfo customer = customerService.getByCustomerId(customerId);

            result.put("customerinfo", customer);
            httpResponseBody.setBizContent(result);

        } catch (Exception e) {
            log.info("调用获取顾客个人信息接口【getCustomerInfo】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
            e.printStackTrace();
        } finally {
            log.info("调用获取顾客个人信息接口【getCustomerInfo】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    /**
     * 获取理发师顾客列表
     *
     * @param request
     * @param body
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/getmycustomerlist")
    public HttpResponseBody getMyCustomerList(HttpServletRequest request, @RequestBody(required = true) String body) {
        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, List<CustomerInfo>> result = new HashMap<>();
        try {

            log.info("调用获取理发师的顾客列表接口【getMyCustomerList】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取理发师的顾客列表接口【getMyCustomerList】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            BarberInfo barberInfo = JsonUtils.fromJSON(bizContent, BarberInfo.class);
            Integer barberId = barberInfo.getBarberId();
            List<CustomerInfo> list = customerService.getMyCustomerList(barberId);
            Date todayData = new Date();

            List<CustomerInfo> nearList = new ArrayList<CustomerInfo>();
            List<CustomerInfo> normalList = new ArrayList<CustomerInfo>();

            for (CustomerInfo customerInfo : list) {
                Date hairCutTime = customerInfo.getHairCutTime();
                Integer time = getTime(hairCutTime, todayData);
                if (time >= 20 && time <= 35) {
                    nearList.add(customerInfo);
                } else {
                    normalList.add(customerInfo);
                }
            }


            result.put("nearlist", nearList);
            result.put("normallist", normalList);
            httpResponseBody.setBizContent(result);

        } catch (Exception e) {
            log.info("调用获取理发师的顾客列表接口【getMyCustomerList】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
            e.printStackTrace();
        } finally {
            log.info("调用获取理发师的顾客列表接口【getMyCustomerList】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    /**
     * 获取顾客端我的发型师列表
     *
     * @param request
     * @param body
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/getmybarberlist")
    public HttpResponseBody getMyBarberList(HttpServletRequest request, @RequestBody(required = true) String body) {
        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, List<BarberInfo>> result = new HashMap<>();
        try {

            log.info("调用获取顾客端我的发型师列表接口【getMyBarberList】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取顾客端我的发型师列表接口【getMyBarberList】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("customerId") == null || StringUtils.isEmpty(paramMap.get("customerId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            CustomerInfo customerInfo = JsonUtils.fromJSON(bizContent, CustomerInfo.class);
            Integer customerId = customerInfo.getCustomerId();
            List<BarberInfo> list = customerService.getMyBarberList(customerId);

            result.put("list", list);
            httpResponseBody.setBizContent(result);

        } catch (Exception e) {
            log.info("调用获取顾客端我的发型师列表接口【getMyBarberList】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
            e.printStackTrace();
        } finally {
            log.info("调用获取顾客端我的发型师列表接口【getMyBarberList】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/income")
    public HttpResponseBody income(HttpServletRequest request, @RequestBody(required = true) String body) {
        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Integer> result = new HashMap<>();
        try {

            log.info("调用顾客增加收入接口【income】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用顾客增加收入接口【income】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("customerId") == null || StringUtils.isEmpty(paramMap.get("customerId").toString().trim())
                    || paramMap.get("title") == null || StringUtils.isEmpty(paramMap.get("title").toString().trim())
                    || paramMap.get("amount") == null || StringUtils.isEmpty(paramMap.get("amount").toString().trim())
                    || paramMap.get("type") == null || StringUtils.isEmpty(paramMap.get("type").toString().trim())
                    || paramMap.get("payWay") == null || StringUtils.isEmpty(paramMap.get("payWay").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            CustomerIncomeInfo customerIncomeInfo = JsonUtils.fromJSON(bizContent, CustomerIncomeInfo.class);
            convertDate(customerIncomeInfo);//根据type 转换正负值
            Integer payWay = customerIncomeInfo.getPayWay();
            Integer type = customerIncomeInfo.getType();
            int status;

            if (payWay != 1 && type == 2) {
                //支付,并且支付方式是微信
                status = customerService.incomeOtherWay(customerIncomeInfo);
            } else {
                status = customerService.income(customerIncomeInfo);
            }


            result.put("status", status);
            httpResponseBody.setBizContent(result);

        } catch (Exception e) {
            log.info("调用顾客增加收入接口【income】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
            e.printStackTrace();
        } finally {
            log.info("调用顾客增加收入接口【income】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/getincome")
    public HttpResponseBody getIncome(HttpServletRequest request, @RequestBody(required = true) String body) {
        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, CustomerAccountInfo> result = new HashMap<>();
        try {

            log.info("调用获取顾客收入信息接口【income】开始，body：" + body);
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取顾客收入信息接口【income】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("customerId") == null || StringUtils.isEmpty(paramMap.get("customerId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            CustomerAccountInfo customerAccountInfo = JsonUtils.fromJSON(bizContent, CustomerAccountInfo.class);
            CustomerAccountInfo customerAccount = customerService.getIncome(customerAccountInfo);

            result.put("customerAccount", customerAccount);
            httpResponseBody.setBizContent(result);

        } catch (Exception e) {
            log.info("调用获取顾客收入信息接口【income】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用获取顾客收入信息接口【income】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    protected Integer getTime(Date beginDate, Date endDate) {
        long daylong = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
        Integer day = new Long(daylong).intValue();
        return day;
    }

    protected void convertDate(CustomerIncomeInfo customerIncomeInfo) {
        Integer type = customerIncomeInfo.getType();
        if (type == 2) {
            customerIncomeInfo.setAmount(-1 * customerIncomeInfo.getAmount());
        }
    }

}
