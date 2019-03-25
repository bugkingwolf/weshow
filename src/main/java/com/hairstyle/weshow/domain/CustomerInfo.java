package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CustomerInfo {
    private Integer customerId;

    private Integer headId;

    private Integer gender;

    private Integer age;

    private Date registerTime;

    private Date updatedTime;

    private Date comingTime;

    private Integer score;

    private Integer frequency;

    private Integer memberStatus;

    private String mobile;

    private String country;
    private String province;
    private String city;

    private Integer level;

    private String openId;

    private String alipayId;

    private String position;

    private Integer wechatAuthentication;

    private String idCard;

    private Integer status;

    private Integer haircutStatus;

    private Integer storeId;

    private String headurl;

    private String name;

    private CustomerFaceInfo customerFaceInfo;

    private Date hairCutTime;
}