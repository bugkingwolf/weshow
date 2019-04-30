package com.hairstyle.weshow.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreBarberRelationInfo {
    private Integer id;

    private Integer barberId;

    private Integer storeId;

    private Date createdTime;

    private Integer deleted;

}