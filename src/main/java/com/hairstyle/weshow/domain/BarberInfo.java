package com.hairstyle.weshow.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class BarberInfo {
	
	/**
	 * 开始下标
	 */
	private Integer start = 0;

	/**
	 * 界限值
	 */
	private Integer limit = 20;
	
    private Integer barberId;

    private Integer userId;

    private Integer headId;

    private String headImageUrl;

    private int gender;

    private Integer age;

    private Integer empiricalValue;

    private String goodAt;

    private Date registerTime;

    private Date updatedTime;

    private Integer score;

    private String mobile;

    private String address;

    private Integer level;

    private Integer years;

    private String openId;

    private String alipayId;

    private Integer haircutAbilityValue;

    private Integer hairdyeAbilityValue;

    private Integer shampooAbilityValue;

    private Integer cosmetologyAbilityValue;

    private Integer permAbilityValue;

    private String growthPlan;

    private String idCard;

    private Integer serviceStatus;

    private int status;

    private Integer storeId;

    private String name;
    
    private String nickname;

    private String introduction;

    private Double distance;
    
    private BigDecimal longitude;
	
	private BigDecimal latitude;

    private List<BarberServiceInfo> barberServiceList;
    
    private BarberAddressInfo barberAddress;

    private int orderType;
    
    private int type;

    private Integer staringPrice;

    private String faceId;

    private Date hairCutTime;

    private String idcardfaceurl;

    private String idcardbackurl;
    
    private String registerfaceurl;
    
    private List<Integer> positions;
    
    private Integer orderNum;
    
    private Integer customerNum;

    private Integer totalIncome;
    
}