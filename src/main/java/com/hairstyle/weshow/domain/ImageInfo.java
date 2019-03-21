package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ImageInfo {
    private Integer id;

    private String url;

    private Integer type;

    private String creatorOpenid;

    private Integer fileSize;

    private Integer storeId;

    private Integer barberId;

    private Integer customerId;

    private Date createTime;

    private Date updateTime;

    private Integer deleted;

    private String deviceNo;

    private String faceId;
    
    private String faceToken;

    private String trackId;

    private Integer hairStatus;

}