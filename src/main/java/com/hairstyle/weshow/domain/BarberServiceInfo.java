package com.hairstyle.weshow.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BarberServiceInfo {
    private Integer id;

    private Integer barberId;

    private String service;

    private Integer onlinePrice;

    private Integer storePrice;

    private List<BarberServiceServiceInfo> barberServiceServiceList;
}