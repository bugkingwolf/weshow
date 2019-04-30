package com.hairstyle.weshow.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BarberAddressInfo {
    private Integer id;

    private Integer barberId;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private Date createdTime;

    private Date updateTime;

}