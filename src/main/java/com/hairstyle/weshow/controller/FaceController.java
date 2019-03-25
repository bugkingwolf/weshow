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
import org.springframework.web.multipart.MultipartFile;

import com.hairstyle.weshow.body.GlobalErrorMessage;
import com.hairstyle.weshow.body.HttpRequestBody;
import com.hairstyle.weshow.body.HttpResponseBody;
import com.hairstyle.weshow.domain.CameraDeviceInfo;
import com.hairstyle.weshow.domain.FaceInfo;
import com.hairstyle.weshow.service.FaceService;
import com.hairstyle.weshow.utils.ConvertUtils;
import com.hairstyle.weshow.utils.JsonUtils;
import com.hairstyle.weshow.utils.MultipartFileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("face")
public class FaceController {


    @Autowired
    FaceService faceServiceimpl;

    @SuppressWarnings("rawtypes")
    @PostMapping("/faceupload")
    public HttpResponseBody faceUpload(HttpServletRequest request, @RequestBody(required = true) String body,
                                       @RequestBody(required = true) MultipartFile faceImgFile) {
        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Integer> result = new HashMap<>();

        log.info("调用上传用户face接口【faceUpload】开始，faceImgFile：" + faceImgFile.getOriginalFilename() +
                ",size:" + faceImgFile.getSize() + ",name :" + faceImgFile.getName());
        log.info("调用上传用户face接口【faceUpload】开始，body：" + body);
        int status;//0失败 1成功
        try {

            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用上传用户face接口【faceUpload】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("faceId") == null || StringUtils.isEmpty(paramMap.get("faceId").toString().trim())
                    || paramMap.get("hairStatus") == null || StringUtils.isEmpty(paramMap.get("hairStatus").toString().trim())
                    || paramMap.get("deviceNo") == null || StringUtils.isEmpty(paramMap.get("deviceNo").toString().trim())
                    || paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())
                    || paramMap.get("faceToken") == null || StringUtils.isEmpty(paramMap.get("faceToken").toString().trim())
                    || paramMap.get("trackId") == null || StringUtils.isEmpty(paramMap.get("trackId").toString().trim())
                    || paramMap.get("groupId") == null || StringUtils.isEmpty(paramMap.get("groupId").toString().trim())
                    || paramMap.get("username") == null || StringUtils.isEmpty(paramMap.get("username").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            FaceInfo faceInfo = JsonUtils.fromJSON(bizContent, FaceInfo.class);

            status = faceServiceimpl.faceUpload(faceImgFile, faceInfo);

            result.put("status", status);
            httpResponseBody.setBizContent(result);

        } catch (Exception e) {
            status = 0;
            result.put("status", status);
            httpResponseBody.setBizContent(result);
            log.info("调用上传用户face接口【faceUpload】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用上传用户face接口【faceUpload】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/faceuploadbase64")
    public HttpResponseBody faceUploadBase64(HttpServletRequest request, @RequestBody(required = true) String body) {
        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, Integer> result = new HashMap<>();

        log.info("调用上传用户facebase64接口【faceUploadBase64】开始，body：" + body);
        int status;//0失败 1成功
        try {

            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();

            Map bizContentMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (bizContentMap.get("faceImgFile") != null) {
                bizContentMap.remove("faceImgFile");
            }
            log.info("调用上传用户facebase64接口【faceUploadBase64】开始，请求参数：" + JsonUtils.toJSON(bizContentMap));

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("faceId") == null || StringUtils.isEmpty(paramMap.get("faceId").toString().trim())
                    || paramMap.get("hairStatus") == null || StringUtils.isEmpty(paramMap.get("hairStatus").toString().trim())
                    || paramMap.get("deviceNo") == null || StringUtils.isEmpty(paramMap.get("deviceNo").toString().trim())
                    || paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())
                    || paramMap.get("faceToken") == null || StringUtils.isEmpty(paramMap.get("faceToken").toString().trim())
                    || paramMap.get("trackId") == null || StringUtils.isEmpty(paramMap.get("trackId").toString().trim())
                    || paramMap.get("groupId") == null || StringUtils.isEmpty(paramMap.get("groupId").toString().trim())
                    || paramMap.get("faceImgFile") == null || StringUtils.isEmpty(paramMap.get("faceImgFile").toString().trim())
                    || paramMap.get("username") == null || StringUtils.isEmpty(paramMap.get("username").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            //base64转换
            String faceBase64String = paramMap.get("faceImgFile") + "";
            MultipartFile faceMultipart = MultipartFileUtil.base64ToMultipart(faceBase64String);

            FaceInfo faceInfo = JsonUtils.fromJSON(bizContent, FaceInfo.class);
            status = faceServiceimpl.faceUpload(faceMultipart, faceInfo);

            result.put("status", status);
            httpResponseBody.setBizContent(result);

        } catch (Exception e) {
            status = 0;
            result.put("status", status);
            httpResponseBody.setBizContent(result);
            log.info("调用上传用户facebase64格式接口【faceUploadBase64】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用上传用户facebase64格式接口【faceUploadBase64】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/getfacebyseat")
    public HttpResponseBody getFaceInfoBySeatNo(HttpServletRequest request, @RequestBody(required = true) String body) {

        HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
        Map<String, List<FaceInfo>> result = new HashMap<>();
        try {
            HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
            String bizContent = httpRequestBody.getBizContent();
            log.info("调用获取此座位号当天人脸信息接口【getFaceInfoBySeatNo】开始，请求参数：" + bizContent);

            Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
            if (paramMap.get("seatNo") == null || StringUtils.isEmpty(paramMap.get("seatNo").toString().trim())
                    || paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())) {
                httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
                return httpResponseBody;
            }

            CameraDeviceInfo cameraDeviceInfo = JsonUtils.fromJSON(bizContent, CameraDeviceInfo.class);

            List<FaceInfo> faceInfo = faceServiceimpl.getFaceInfoBySeatNo(cameraDeviceInfo);

            result.put("face", faceInfo);
            httpResponseBody.setBizContent(result);
        } catch (Exception e) {
            log.info("调用获取此座位号当天人脸信息接口【getFaceInfoBySeatNo】异常 :异常[" + e.getMessage() + "]", e);
            httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
        } finally {
            log.info("调用获取此座位号当天人脸信息接口【getFaceInfoBySeatNo】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
        return httpResponseBody;
    }

}
