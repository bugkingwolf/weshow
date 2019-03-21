package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.OrderServiceServiceInfo;

public interface OrderServiceServiceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderServiceServiceInfo record);

    int insertSelective(OrderServiceServiceInfo record);

    OrderServiceServiceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderServiceServiceInfo record);

    int updateByPrimaryKey(OrderServiceServiceInfo record);
}