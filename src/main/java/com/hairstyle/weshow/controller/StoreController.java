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
import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.CustomerInfo;
import com.hairstyle.weshow.domain.OrderInfo;
import com.hairstyle.weshow.domain.StoreInfo;
import com.hairstyle.weshow.service.StoreService;
import com.hairstyle.weshow.utils.ConvertUtils;
import com.hairstyle.weshow.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("store")
public class StoreController {

	@Autowired
	StoreService storeService;

	@PostMapping("/list")
	public HttpResponseBody list(HttpServletRequest request, @RequestBody(required = true) String body) {
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, List<StoreInfo>> result = new HashMap<>();
		try {
			
			log.info("调用店铺列表接口【list】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用店铺列表接口【list】开始，请求参数：" + bizContent);

			StoreInfo storeInfo = JsonUtils.fromJSON(bizContent, StoreInfo.class);
			List<StoreInfo> list = storeService.list(storeInfo);
			
			result.put("list", list);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用店铺列表接口【list】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用店铺列表接口【list】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
		return httpResponseBody;
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/detail")
	public HttpResponseBody detail(HttpServletRequest request, @RequestBody(required = true) String body) {

		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, StoreInfo> result = new HashMap<>();
		try {
			
			log.info("调用店铺详情接口【detail】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用店铺详情接口【detail】开始，请求参数：" + bizContent);

			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			
			StoreInfo storeInfo = JsonUtils.fromJSON(bizContent, StoreInfo.class);
			StoreInfo store = storeService.detail(storeInfo);
			result.put("store", store);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用店铺详情接口【detail】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用店铺详情接口【detail】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
		return httpResponseBody;
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/getservicingstore")
	public HttpResponseBody getServicingStore(HttpServletRequest request, @RequestBody(required = true) String body) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, List<StoreInfo>> result = new HashMap<>();
		try {
			log.info("调用获取理发师服务的共享店铺接口【getServicingStore】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用获取理发师服务的共享店铺接口【getServicingStore】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			
			BarberInfo barberInfo = JsonUtils.fromJSON(bizContent, BarberInfo.class);
			List<StoreInfo> list = storeService.getServicingStore(barberInfo);
			result.put("list", list);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用获取理发师服务的共享店铺接口【getServicingStore】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用获取理发师服务的共享店铺接口【getServicingStore】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/mystore")
	public HttpResponseBody getMyStoreList(HttpServletRequest request, @RequestBody(required = true) String body) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, List<StoreInfo>> result = new HashMap<>();
		try {
			log.info("调用获取个人共享店铺列表接口【getMyStoreList】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用获取个人共享店铺列表接口【getMyStoreList】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("customerId") == null || StringUtils.isEmpty(paramMap.get("customerId").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			
			CustomerInfo customerInfo = JsonUtils.fromJSON(bizContent, CustomerInfo.class);
			List<StoreInfo> list = storeService.getMyStoreList(customerInfo);
			result.put("list", list);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用获取个人共享店铺列表接口【getMyStoreList】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用获取个人共享店铺列表接口【getMyStoreList】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/add")
	public HttpResponseBody addStore(HttpServletRequest request, @RequestPart(required = true) String body,
			@RequestBody(required = true) MultipartFile storeImgFile) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, Integer> result = new HashMap<>();
		try {
			log.info("调用添加共享店铺接口【addStore】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用添加共享店铺接口【addStore】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("customerId") == null || StringUtils.isEmpty(paramMap.get("customerId").toString().trim())
				||paramMap.get("name") == null || StringUtils.isEmpty(paramMap.get("name").toString().trim())
				||paramMap.get("mobile") == null || StringUtils.isEmpty(paramMap.get("mobile").toString().trim())
				||paramMap.get("category") == null || StringUtils.isEmpty(paramMap.get("category").toString().trim())
				||paramMap.get("seatNum") == null || StringUtils.isEmpty(paramMap.get("seatNum").toString().trim())
				||paramMap.get("businessTime") == null || StringUtils.isEmpty(paramMap.get("businessTime").toString().trim())
				||paramMap.get("address") == null || StringUtils.isEmpty(paramMap.get("address").toString().trim())
				||paramMap.get("acreage") == null || StringUtils.isEmpty(paramMap.get("acreage").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			
			StoreInfo storeInfo = JsonUtils.fromJSON(bizContent, StoreInfo.class);
			int status = storeService.addStore(storeInfo,storeImgFile);
			result.put("status", status);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用添加共享店铺接口【addStore】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用添加共享店铺接口【addStore】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/share")
	public HttpResponseBody shareStore(HttpServletRequest request, @RequestBody(required = true) String body,
			@RequestBody(required = true) MultipartFile storeImgFile) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, Integer> result = new HashMap<>();
		try {
			int status = 0;
			log.info("调用开启关闭共享店铺接口【shareStore】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用开启关闭共享店铺接口【shareStore】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())
				||paramMap.get("share") == null || StringUtils.isEmpty(paramMap.get("share").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			
			StoreInfo storeInfo = JsonUtils.fromJSON(bizContent, StoreInfo.class);
			int update = storeService.shareStore(storeInfo);
			if(update > 0){
				status = 1;
			}
			result.put("status", status);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用开启关闭共享店铺接口【shareStore】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用开启关闭共享店铺接口【shareStore】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/sharestoredetail")
	public HttpResponseBody getShareStoreDetail(HttpServletRequest request, @RequestBody(required = true) String body,
			@RequestBody(required = true) MultipartFile storeImgFile) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, StoreInfo> result = new HashMap<>();
		try {
			log.info("调用获取共享店铺详情接口【getShareStoreDetail】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用获取共享店铺详情接口【getShareStoreDetail】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			StoreInfo storeInfo = JsonUtils.fromJSON(bizContent, StoreInfo.class);
			StoreInfo  store = storeService.getShareStoreDetail(storeInfo);
			result.put("store", store);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用获取共享店铺详情接口【getShareStoreDetail】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用获取共享店铺详情接口【getShareStoreDetail】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/historybarber")
	public HttpResponseBody getHistoryBarber(HttpServletRequest request, @RequestBody(required = true) String body) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, List<BarberInfo>> result = new HashMap<>();
		try {
			log.info("调用店铺历史理发师接口【getHistoryBarber】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用店铺历史理发师接口【getHistoryBarber】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			Integer storeId = Integer.parseInt(paramMap.get("storeId") + "");
			List<BarberInfo>  list = storeService.getHistoryBarber(storeId);
			result.put("list", list);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用店铺历史理发师接口【getHistoryBarber】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用店铺历史理发师接口【getHistoryBarber】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/historycustomer")
	public HttpResponseBody getHistoryCustomer(HttpServletRequest request, @RequestBody(required = true) String body) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, List<CustomerInfo>> result = new HashMap<>();
		try {
			log.info("调用获取获取店铺历史顾客接口【getHistoryCustomer】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用获取获取店铺历史顾客接口【getHistoryCustomer】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			Integer storeId = Integer.parseInt(paramMap.get("storeId") + "");
			List<CustomerInfo>  list = storeService.getHistoryCustomer(storeId);
			result.put("list", list);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用获取获取店铺历史顾客接口【getHistoryCustomer】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用获取获取店铺历史顾客接口【getHistoryCustomer】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/historeorder")
	public HttpResponseBody getHistoreOrder(HttpServletRequest request, @RequestBody(required = true) String body) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, List<OrderInfo>> result = new HashMap<>();
		try {
			log.info("调用获取店铺历史订单接口【getHistoreOrder】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用获取店铺历史订单接口【getHistoreOrder】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			Integer storeId = Integer.parseInt(paramMap.get("storeId") + "");
			List<OrderInfo>  list = storeService.getHistoreOrder(storeId);
			result.put("list", list);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用获取店铺历史订单接口【getHistoreOrder】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用获取店铺历史订单接口【getHistoreOrder】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/bindbarberandstore")
	public HttpResponseBody bindBarberAndStore(HttpServletRequest request, @RequestBody(required = true) String body) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, Integer> result = new HashMap<>();
		try {
			log.info("调用理发师添加共享店铺接口【bindBarberAndStore】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用理发师添加共享店铺接口【bindBarberAndStore】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())
				||paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			Integer storeId = Integer.parseInt(paramMap.get("storeId") + "");
			Integer barberId = Integer.parseInt(paramMap.get("barberId") + "");
			int status = storeService.bindBarberAndStore(storeId, barberId);
			result.put("status", status);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用理发师添加共享店铺接口【bindBarberAndStore】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用理发师添加共享店铺接口【bindBarberAndStore】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/removestore")
	public HttpResponseBody removeStore(HttpServletRequest request, @RequestBody(required = true) String body) {
		
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, Integer> result = new HashMap<>();
		try {
			log.info("调用理发师移除共享店铺接口【removeStore】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用理发师移除共享店铺接口【removeStore】开始，请求参数：" + bizContent);
			
			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("storeId") == null || StringUtils.isEmpty(paramMap.get("storeId").toString().trim())
				||paramMap.get("barberId") == null || StringUtils.isEmpty(paramMap.get("barberId").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			Integer storeId = Integer.parseInt(paramMap.get("storeId") + "");
			Integer barberId = Integer.parseInt(paramMap.get("barberId") + "");
			int status = storeService.removeStore(storeId, barberId);
			result.put("status", status);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用理发师移除共享店铺接口【removeStore】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用理发师移除共享店铺接口【removeStore】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
		}
		return httpResponseBody;
	}
}
