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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hairstyle.weshow.body.GlobalErrorMessage;
import com.hairstyle.weshow.body.HttpRequestBody;
import com.hairstyle.weshow.body.HttpResponseBody;
import com.hairstyle.weshow.domain.BarberAddressInfo;
import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.SubscribeManagementInfo;
import com.hairstyle.weshow.service.BarberService;
import com.hairstyle.weshow.service.CustomerService;
import com.hairstyle.weshow.service.FaceService;
import com.hairstyle.weshow.utils.ConvertUtils;
import com.hairstyle.weshow.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("barber")
public class BarberController {

	@Autowired
	CustomerService customerService;
	@Autowired
	FaceService faceService;
	@Autowired
	BarberService barberService;

	/**
	 * 获取理发师详情
	 * 
	 * @param request
	 * @param body
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/getbarberinfo")
	public HttpResponseBody getBarberInfo(HttpServletRequest request, @RequestBody(required = true) String body) {

		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, BarberInfo> result = new HashMap<>();

		try {
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用理发师详情接口【getBarberInfo】开始，请求参数：" + bizContent);

			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if (paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())) {
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}

			BarberInfo barberInfo = JsonUtils.fromJSON(bizContent, BarberInfo.class);

			Integer barberId = barberInfo.getBarberId();
			BarberInfo barber = barberService.getBarberInfo(barberId);
			result.put("barberinfo", barber);
			httpResponseBody.setBizContent(result);

		} catch (Exception e) {
			log.info("调用理发师详情接口【getBarberInfo】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		} finally {
			log.info("调用理发师详情接口【getBarberInfo】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}

	/**
	 * 理发师列表
	 * 
	 * @param request
	 * @param body
	 * @return
	 */
	@PostMapping("/list")
	public HttpResponseBody list(HttpServletRequest request, @RequestBody(required = true) String body) {
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, List<BarberInfo>> result = new HashMap<>();
		try {

			log.info("调用理发师列表接口【list】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用理发师列表接口【list】开始，请求参数：" + bizContent);

			BarberInfo barberInfo = JsonUtils.fromJSON(bizContent, BarberInfo.class);
			List<BarberInfo> list = barberService.list(barberInfo);

			result.put("list", list);

			httpResponseBody.setBizContent(result);

		} catch (Exception e) {
			log.info("调用理发师列表接口【list】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		} finally {
			log.info("调用理发师列表接口【list】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}

	/**
	 * 配置预约时间管理接口
	 * 
	 * @param request
	 * @param body
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/subscribemanagement")
	public HttpResponseBody subscribeManagement(HttpServletRequest request, @RequestBody(required = true) String body) {
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, Integer> result = new HashMap<>();
		try {

			log.info("调用配置预约时间管理接口【subscribeManagement】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用配置预约时间管理接口【subscribeManagement】开始，请求参数：" + bizContent);

			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if (paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())
					|| paramMap.get("position") == null || StringUtils.isEmpty(paramMap.get("position").toString().trim()) 
					|| paramMap.get("type") == null || StringUtils.isEmpty(paramMap.get("type").toString().trim())) {
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}

			SubscribeManagementInfo subscribeManagementInfo = JsonUtils.fromJSON(bizContent,
					SubscribeManagementInfo.class);
			Integer status = barberService.subscribeManagement(subscribeManagementInfo);

			result.put("status", status);// 0失败 1成功
			httpResponseBody.setBizContent(result);

		} catch (Exception e) {
			log.info("调用配置预约时间管理接口【subscribeManagement】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		} finally {
			log.info("调用配置预约时间管理接口【subscribeManagement】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}

	/**
	 * 获取理发师可预约时间
	 * @param request
	 * @param body
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/getsubscribemanagement")
	public HttpResponseBody getSubscribeManagement(HttpServletRequest request, @RequestBody(required = true) String body) {
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, List<Integer>> result = new HashMap<>();
		try {
			
			log.info("调用获取理发师可预约时间接口【getSubscribeManagement】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用获取理发师可预约时间接口【getSubscribeManagement】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if (paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())) {
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			Integer barberId = Integer.parseInt(String.valueOf(paramMap.get("barberId")));
			
			List<Integer> positions = barberService.getPosition(barberId);
			
			result.put("positions", positions);
			httpResponseBody.setBizContent(result);
			
		} catch (Exception e) {
			log.info("调用获取理发师可预约时间接口【getSubscribeManagement】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		} finally {
			log.info("调用获取理发师可预约时间接口【getSubscribeManagement】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}
	
	
	/**
	 * 获取理发师个人页
	 * 
	 * @param request
	 * @param body
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/getbarberhome")
	public HttpResponseBody getBarberHome(HttpServletRequest request, @RequestBody(required = true) String body) {

		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, BarberInfo> result = new HashMap<>();

		try {
			log.info("调用理发师个人页接口【getBarberHome】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用理发师个人页接口【getBarberHome】开始，请求参数：" + bizContent);

			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if (paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())) {
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}

			BarberInfo barberInfo = JsonUtils.fromJSON(bizContent, BarberInfo.class);

			Integer barberId = barberInfo.getBarberId();
			BarberInfo barber = barberService.getBarberHome(barberId);
			result.put("barberinfo", barber);
			httpResponseBody.setBizContent(result);

		} catch (Exception e) {
			log.info("调用理发师个人页接口【getBarberHome】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		} finally {
			log.info("调用理发师个人页接口【getBarberHome】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}

	/**
	 * 发送验证码
	 * 
	 * @param request
	 * @param body
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/sendsms")
	public HttpResponseBody sendSms(HttpServletRequest request, @RequestBody(required = true) String body) {
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, Integer> result = new HashMap<>();
		try {

			log.info("调用发送短信验证码接口【sendSms】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用发送短信验证码接口【sendSms】开始，请求参数：" + bizContent);

			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if (paramMap.get("mobile") == null || StringUtils.isEmpty(paramMap.get("mobile").toString().trim())) {
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}

			String mobile = paramMap.get("mobile") + "";
			int status = barberService.sendSms(mobile);// stauts 0失败 1成功
			result.put("status", status);
			httpResponseBody.setBizContent(result);

		} catch (Exception e) {
			log.info("调用发送短信验证码接口【sendSms】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		} finally {
			log.info("调用发送短信验证码接口【sendSms】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}

	/**
	 * 注册
	 * 
	 * @param request
	 * @param body
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@PostMapping("/register")
	public HttpResponseBody registerBarber(HttpServletRequest request, @RequestPart(required = true) String body,
			@RequestBody(required = true) MultipartFile faceImageFile,
			@RequestBody(required = true) MultipartFile faceIdCardImageFile,
			@RequestBody(required = true) MultipartFile backIdCardImageFile) {
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, Integer> result = new HashMap<>();
		try {
			log.info("调用理发师注册接口【registerBarber】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用理发师注册接口【registerBarber】开始，请求参数：" + bizContent);

			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if (paramMap.get("mobile") == null || StringUtils.isEmpty(paramMap.get("mobile").toString().trim())
					|| paramMap.get("code") == null || StringUtils.isEmpty(paramMap.get("code").toString().trim())
					|| paramMap.get("idCard") == null|| StringUtils.isEmpty(paramMap.get("idCard").toString().trim())
					|| paramMap.get("customerId") == null|| StringUtils.isEmpty(paramMap.get("customerId").toString().trim())) {
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}

			String mobile = paramMap.get("mobile") + "";
			String code = paramMap.get("code") + "";
			String idCard = paramMap.get("idCard") + "";
			Integer customerId = Integer.parseInt(paramMap.get("customerId") + "");
			int status = barberService.registerBarber(customerId,mobile, code, idCard, faceImageFile, faceIdCardImageFile,
					backIdCardImageFile);// stauts 0失败 1成功 2短信验证码失败
			if(status == 2){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.SMS_CODE_FAIL);
				return httpResponseBody;
			}else if(status == 3){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.REGISTER_FAIL);
				return httpResponseBody;
			}
			result.put("status", status);
			httpResponseBody.setBizContent(result);

		} catch (Exception e) {
			log.info("调用理发师注册接口【registerBarber】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		} finally {
			log.info("调用理发师注册接口【registerBarber】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/insertaddress")
	public HttpResponseBody insertBarberAddress(HttpServletRequest request, @RequestBody(required = true) String body) {
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, Integer> result = new HashMap<>();
		try {
			log.info("调用保存理发师位置接口【insertBarberAddress】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用保存理发师位置接口【insertBarberAddress】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if (paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())
					|| paramMap.get("longitude") == null || StringUtils.isEmpty(paramMap.get("longitude").toString().trim())
					|| paramMap.get("latitude") == null|| StringUtils.isEmpty(paramMap.get("latitude").toString().trim())) {
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			
			BarberAddressInfo barberAddressInfo = JsonUtils.fromJSON(bizContent, BarberAddressInfo.class);
			int status = barberService.insertBarberAddress(barberAddressInfo);// stauts 0失败 1成功 2短信验证码失败
			result.put("status", status);
			httpResponseBody.setBizContent(result);
			
		} catch (Exception e) {
			log.info("调用保存理发师位置接口【insertBarberAddress】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		} finally {
			log.info("调用保存理发师位置接口【insertBarberAddress】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}

}