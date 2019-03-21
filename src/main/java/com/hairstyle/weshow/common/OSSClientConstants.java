package com.hairstyle.weshow.common;

import org.springframework.beans.factory.annotation.Value;

public class OSSClientConstants {

//    //阿里云API的外网域名
//    public static final String ENDPOINT = "oss-cn-beijing.aliyuncs.com";
//    //阿里云API的密钥Access Key ID
//    public static final String ACCESS_KEY_ID = "LTAI49csnneu5RSn";
//    //阿里云API的密钥Access Key Secret
//    public static final String ACCESS_KEY_SECRET = "jnxvpJlK2sdeWrk674B1qgKn3Olhgy";
//    //阿里云API的bucket名称
//    public static final String BACKET_NAME = "barbertest";//"uploadpicture";
//    //阿里云API的文件夹名称
//    public static final String FOLDER="hair/";
    
    
    
    //阿里云API的外网域名
    @Value("${aliyun_oss_endpoint}")
    public String ENDPOINT;
    //阿里云API的密钥Access Key ID
    @Value("${aliyun_oss_accesskeyid}")
    public String ACCESS_KEY_ID;
    //阿里云API的密钥Access Key Secret
    @Value("${aliyun_oss_accesskeysecret}")
    public String ACCESS_KEY_SECRET;
    //阿里云API的bucket名称
    @Value("${aliyun_oss_bucket}")
    public String BACKET_NAME;//"uploadpicture";
    //阿里云API的文件夹名称
    public static final String FOLDER="hair/";
}
