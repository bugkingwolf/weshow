package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CustomerFaceInfo {
    private Integer id;

    private Integer customerId;

    private String url;

    private Integer age;

    private String gender;

    private Double beauty;

    private String faceShape;

    private String race;

    private String glasses;

    private Date createdTime;

    private Date updateTime;


}