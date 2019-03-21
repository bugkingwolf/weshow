package com.hairstyle.weshow.domain;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderInfo {
	
	private Integer start = 0;
	
	private Integer limit = 10;
	
    private Integer orderId;

    private Integer barberId;

    private Integer customerId;

    private Integer storeId;

    private Date settlementTime;

    private Date startTime;

    private Date endTime;

    private int useTime;

    private Date createdTime;

    private Date updatedTime;

    private String serviceIds;

    private Integer serviceNum;

    private Integer browseNum;

    private Integer payStatus;

    private Integer payMethod;

    private Integer amount;

    private String openId;

    private String alipayId;

    private List<OrderServiceInfo> barberServiceList;
    
	private Integer[] positions;

	private Integer[] storeIds;

	private List<String> timeList;
	
	private List<StoreInfo> storeList;
	
	private OrderRelatonInfo orderRelaton;
	
	private Integer status;

	private String statusStr;

	private Integer beginImgId;
	
	private ImageInfo beginImg;

	private Integer endImgId;
	
	private ImageInfo endImg;
	
	private CustomerInfo customer;

	private BarberInfo barber;

	private Integer type;

	private String remark;

	private Integer code;
}