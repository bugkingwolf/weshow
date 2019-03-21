package com.hairstyle.weshow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hairstyle.weshow.domain.OrderInfo;

public interface OrderInfoMapper {
    int deleteByPrimaryKey(Integer orderId);

    int insert(OrderInfo record);

    int insertSelective(OrderInfo record);

    OrderInfo selectByPrimaryKey(Integer orderId);

    int updateByPrimaryKeySelective(OrderInfo record);

    int updateByPrimaryKey(OrderInfo record);

	List<OrderInfo> getOrderlist(OrderInfo orderInfo);

	int updateImage(String string);

	OrderInfo getOrderByBarberIdAndCustomerId(@Param("customerId")Integer customerId, @Param("barberId")Integer barberId);

	List<OrderInfo> getHistoryOrderList(Integer storeId);
}