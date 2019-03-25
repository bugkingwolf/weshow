package com.hairstyle.weshow.body;

import com.hairstyle.weshow.utils.JsonUtils;

public class HttpRequestBody {

    /**
     * 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
     */
    private String timestamp;

    /**
     * 调用的接口版本，固定为：1.0
     */
    private String version;


    /**
     * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，
     */
    private Object bizContent;


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getBizContent() {
        return JsonUtils.toJSON(bizContent);
    }

    public void setBizContent(Object bizContent) {
        this.bizContent = bizContent;
    }

    @Override
    public String toString() {
        return JsonUtils.toJSON(this);
    }

}
