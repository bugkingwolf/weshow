package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.OrderRelatonInfo;

public interface OrderRelatonInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderRelatonInfo record);

    int insertSelective(OrderRelatonInfo record);

    OrderRelatonInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderRelatonInfo record);

    int updateByPrimaryKey(OrderRelatonInfo record);

	OrderRelatonInfo selectByOrderId(Integer orderId);

	int updateByOrderIdSelective(OrderRelatonInfo orderRelatonInfo);
}