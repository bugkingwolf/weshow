package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class ExtractInfo {
    private Integer id;

    private Integer customerId;

    private Integer amount;

    private Date createdTime;

    private Integer status;

}