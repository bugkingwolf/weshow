package com.hairstyle.weshow.body;

public class HttpResponseBody {

    /**
     * 网关返回码
     */
    private String code;

    /**
     * 网关返回码描述
     */
    private String msg;

    /**
     * 响应参数的集合，最大长度不限
     */
    private Object bizContent;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public Object getBizContent() {
        return bizContent;
    }

    public void setBizContent(Object bizContent) {
        this.bizContent = bizContent;
    }

    public HttpResponseBody() {
    }

    public HttpResponseBody(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public HttpResponseBody(GlobalErrorMessage globalErrorMessage) {
        setErrorMessage(globalErrorMessage);
    }

    /**
     * 设置异常信息
     *
     * @param globalErrorMessage
     */
    public void setErrorMessage(GlobalErrorMessage globalErrorMessage) {
        setCode(globalErrorMessage.getCode().toString());
        setMsg(globalErrorMessage.getMessage());
    }

    @Override
    public String toString() {
        return "HttpResponseBody{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", bizContent='" + bizContent + '\'' +
                '}';
    }
}
