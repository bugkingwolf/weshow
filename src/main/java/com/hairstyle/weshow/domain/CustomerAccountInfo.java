package com.hairstyle.weshow.domain;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class CustomerAccountInfo {

    private Integer start = 0;

    private Integer limit = 10;

    private Integer id;

    private Integer customerId;

    private Integer amount;

    private Integer type;

    private Date createdTime;

    private Date updatedTime;

    private List<CustomerIncomeInfo> customerIncomeList;

}