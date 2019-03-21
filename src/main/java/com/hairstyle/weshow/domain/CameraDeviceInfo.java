package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CameraDeviceInfo {
    private Integer id;

    private Integer storeId;

    private Integer seatNo;

    private String deviceNo;

    private Integer status;

    private String ip;

    private Date createdTime;

}