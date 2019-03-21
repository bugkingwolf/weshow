package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.StoreAddressInfo;

public interface StoreAddressInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StoreAddressInfo record);

    int insertSelective(StoreAddressInfo record);

    StoreAddressInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StoreAddressInfo record);

    int updateByPrimaryKey(StoreAddressInfo record);
}