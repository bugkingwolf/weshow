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
public class StoreInfo {

    /**
     * 开始下标
     */
    private Integer start = 0;

    /**
     * 界限值
     */
    private Integer limit = 20;

    private Integer storeId;

    private String name;

    private String mobile;

    private Date createdTime;

    private Date updatedTime;

    private Integer orderType;

    private Integer category;

    private Integer startingPrice;

    private Integer customerId;

    private Integer shopownerId;

    // 面积
    private Integer acreage;

    private Integer seatNum;

    private String intelligentDevice;

    private Integer headId;

    private Integer barberNum;

    private Integer customerNum;

    private Integer faceNum;

    private String address;

    private Double distance;

    private String headImage;

    private List<BarberInfo> barberList;

    private List<CustomerInfo> customerList;

    private List<FaceInfo> faceList;

    private List<ImageInfo> imageList;

    private BigDecimal longitude;

    private BigDecimal latitude;

    private StoreAddressInfo storeAddress;

    private String businessTime;

    private Integer status;

    private Integer share;

    private Integer income;

    private Integer historyBarberNum;

    private Integer historyCustomerNum;

    private Integer historyOrderNum;

}