package com.hairstyle.weshow.utils;

import com.hairstyle.weshow.body.HttpRequestBody;

public class ConvertUtils {

	public static HttpRequestBody convertData(String body) {
		return JsonUtils.fromJSON(body, HttpRequestBody.class);
	}
}
