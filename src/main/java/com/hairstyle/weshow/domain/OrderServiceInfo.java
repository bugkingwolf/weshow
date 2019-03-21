package com.hairstyle.weshow.domain;


import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderServiceInfo {
    private Integer id;

    private Integer orderId;

    private String service;

    private Integer onlinePrice;

    private Integer storePrice;

    private List<OrderServiceServiceInfo> barberServiceServiceList;
}