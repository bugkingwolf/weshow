package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.OrderServiceInfo;

public interface OrderServiceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderServiceInfo record);

    int insertSelective(OrderServiceInfo record);

    OrderServiceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderServiceInfo record);

    int updateByPrimaryKey(OrderServiceInfo record);
}