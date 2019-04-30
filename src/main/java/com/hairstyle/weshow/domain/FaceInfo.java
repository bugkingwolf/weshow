package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FaceInfo {
    private Integer id;

    private Integer storeId;

    private String faceId;

    private String deviceNo;

    private Integer seatNo;

    private String groupId;

    private String username;

    private String faceToken;

    private String trackId;

    private Integer hairStatus;

    private Integer status;

    private Integer comingImgId;

    private ImageInfo comingImg;

    private Integer shampooImgId;

    private ImageInfo shampooImg;

    private Integer haircutImgId;

    private ImageInfo haircutImg;

    private ImageInfo headImg;

    private Integer getoutImgId;

    private Date comingTime;

    private Date createdTime;

    private Date updateTime;

    private Integer inStoreTime;

}