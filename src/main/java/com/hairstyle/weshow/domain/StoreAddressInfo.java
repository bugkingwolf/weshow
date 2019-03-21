package com.hairstyle.weshow.domain;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreAddressInfo {
    private Integer id;

    private Integer storeId;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private Date createdTime;

    private Date updateTime;

}