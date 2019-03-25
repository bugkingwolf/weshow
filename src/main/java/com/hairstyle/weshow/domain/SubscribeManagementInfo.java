package com.hairstyle.weshow.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class SubscribeManagementInfo {
    private Integer id;

    private Integer barberId;

    private Integer time;

    private Integer position;

    private Integer type;

}