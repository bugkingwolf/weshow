package com.hairstyle.weshow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hairstyle.weshow.domain.CustomerInfo;

public interface CustomerInfoMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insert(CustomerInfo record);

    int insertSelective(CustomerInfo record);

    CustomerInfo selectByPrimaryKey(Integer customerId);

    int updateByPrimaryKeySelective(CustomerInfo record);

    int updateByPrimaryKey(CustomerInfo record);

    List<CustomerInfo> getByStoreId(Integer storeId);

    CustomerInfo getByOrderId(Integer orderId);

    List<CustomerInfo> getMyCustomerList(@Param("barberId") Integer barberId, @Param("count") Integer count);

    Integer getMyCustomerOrderCount(Integer barberId);

    CustomerInfo getbyOpenId(String openId);

    int updateByOpenId(CustomerInfo customerInfo);

    List<CustomerInfo> getHistoryCustomerList(Integer storeId);

}