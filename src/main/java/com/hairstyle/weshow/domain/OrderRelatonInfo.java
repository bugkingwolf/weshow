package com.hairstyle.weshow.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderRelatonInfo {
    private Integer id;

    private Integer orderId;

    private Integer noLimitTime;

    private Integer adjustTime;

    private String positions;

    private List<String> timeList;

    private Integer noLimitStore;

    private Integer adjustStore;

    private String storeIds;

    private List<StoreInfo> storeList;

}