package com.hairstyle.weshow.controller;

import java.util.HashMap;
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
import com.hairstyle.weshow.domain.ExtractInfo;
import com.hairstyle.weshow.service.ExtractService;
import com.hairstyle.weshow.utils.ConvertUtils;
import com.hairstyle.weshow.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("extract")
public class ExtractController {

	@Autowired
	ExtractService extractService;

	@SuppressWarnings("rawtypes")
	@PostMapping("/apply")
	public HttpResponseBody apply(HttpServletRequest request, @RequestBody(required = true) String body) {
		HttpResponseBody httpResponseBody = new HttpResponseBody(GlobalErrorMessage.SUCCESS);
		Map<String, Integer> result = new HashMap<>();
		try {
			
			log.info("调用提现申请接口【apply】开始，body：" + body);
			HttpRequestBody httpRequestBody = ConvertUtils.convertData(body);
			String bizContent = httpRequestBody.getBizContent();
			log.info("调用提现申请接口【apply】开始，请求参数：" + bizContent);

			Map paramMap = JsonUtils.fromJSON(bizContent, Map.class);
			if(paramMap.get("customerId") == null || StringUtils.isEmpty(paramMap.get("customerId").toString().trim())
				||paramMap.get("amount") == null || StringUtils.isEmpty(paramMap.get("amount").toString().trim())){
				httpResponseBody.setErrorMessage(GlobalErrorMessage.MISSING_PARAMETERS);
				return httpResponseBody;
			}
			
			ExtractInfo extractInfo = JsonUtils.fromJSON(bizContent, ExtractInfo.class);
			int status = extractService.apply(extractInfo);
			
			result.put("status", status);
			httpResponseBody.setBizContent(result);
		} catch (Exception e) {
			log.info("调用提现申请接口【apply】异常 :异常[" + e.getMessage() + "]", e);
			httpResponseBody = new HttpResponseBody(GlobalErrorMessage.BUSINESS_FAILED);
		}finally {
			log.info("调用提现申请接口【apply】结束，结果：" + JsonUtils.toJSON(httpResponseBody));
        }
		return httpResponseBody;
	}

}
