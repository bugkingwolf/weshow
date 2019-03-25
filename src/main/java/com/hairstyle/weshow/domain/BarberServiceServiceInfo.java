package com.hairstyle.weshow.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BarberServiceServiceInfo {

    private Integer id;

    private Integer barberServiceId;

    private String service;

    private Integer onlinePrice;

    private Integer storePrice;

}