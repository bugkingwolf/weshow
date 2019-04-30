package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WxpayFlowInfo {
    private Integer id;

    private Integer customerId;

    private Integer orderId;

    private String openId;

    private String transactionId;

    private Integer amount;

    private Integer tradeType;

    private Date payTime;

}