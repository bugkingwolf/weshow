package com.hairstyle.weshow.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DecryptDataInfo {

    private String openId;

    private String unionId;

    private String nickName;

    private Integer gender;

    private String language;

    private String country;

    private String province;

    private String city;

    private String avatarUrl;

}