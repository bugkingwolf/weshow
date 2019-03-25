package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class CustomerIncomeInfo {

    private Integer start = 0;

    private Integer limit = 10;

    private Integer id;

    private Integer customerId;

    private String title;

    private Integer amount;

    private Integer type;

    private Integer payWay;

    private Date createdTime;

}