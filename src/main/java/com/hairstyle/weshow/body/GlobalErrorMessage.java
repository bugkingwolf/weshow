package com.hairstyle.weshow.body;

public enum GlobalErrorMessage {
    SUCCESS(10000, "接口调用成功"), UNKNOW(20000, "服务不可用"), UNAUTHORIZED(20001, "权限不足"), SMS_CODE_FAIL(20002, "短信验证码不正确"), REGISTER_FAIL(20003, "理发师重复注册"), MISSING_PARAMETERS(40001, "缺少必选参数"), ILLEGAL_PARAMETERS(40002, "非法的参数"), BUSINESS_FAILED(40004, "业务请求异常");
    private Integer code;
    private String message;

    GlobalErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
