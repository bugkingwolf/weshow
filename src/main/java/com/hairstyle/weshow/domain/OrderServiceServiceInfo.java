package com.hairstyle.weshow.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderServiceServiceInfo {
    private Integer id;

    private Integer orderServiceId;

    private String service;

    private Integer onlinePrice;

    private Integer storePrice;

}