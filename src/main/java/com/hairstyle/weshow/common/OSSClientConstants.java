package com.hairstyle.weshow.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.hairstyle.weshow.utils.AliyunOSSClientUtil;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix="oss")
public class OSSClientConstants {


//    //阿里云API的外网域名
//    @Value("${aliyun_oss_endpoint}")
//    public String ENDPOINT;
//    //阿里云API的密钥Access Key ID
//    @Value("${aliyun_oss_accesskeyid}")
//    public String ACCESS_KEY_ID;
//    //阿里云API的密钥Access Key Secret
//    @Value("${aliyun_oss_accesskeysecret}")
//    public String ACCESS_KEY_SECRET;
//    //阿里云API的bucket名称
//    @Value("${aliyun_oss_bucket}")
//    public String BACKET_NAME;//"uploadpicture";
//    //阿里云API的文件夹名称
//    public static final String FOLDER = "hair/";
    
    

//    	// 文件存储目录
//    	private String filedir = "hair/";
        //阿里云API的外网域名
//        @Value("${aliyun_oss_endpoint}")
		public String endpoint;
        //阿里云API的密钥Access Key ID
//        @Value("${aliyun_oss_accesskeyid}")
		public String accessKeyId;
        //阿里云API的密钥Access Key Secret
//        @Value("${aliyun_oss_accesskeysecret}")
		public String accessKeySecret;
        //阿里云API的bucket名称
//        @Value("${aliyun_oss_bucket}")
		public String bucketName;//"uploadpicture";
}
