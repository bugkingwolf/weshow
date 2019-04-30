package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SmsInfo {
    private Integer id;

    private String mobile;

    private String code;

    private Date createdTime;

    private Integer status;

}