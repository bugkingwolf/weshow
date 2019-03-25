package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.StoreBarberRelationInfo;

public interface StoreBarberRelationInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(StoreBarberRelationInfo record);

    int insertSelective(StoreBarberRelationInfo record);

    StoreBarberRelationInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StoreBarberRelationInfo record);

    int updateByPrimaryKey(StoreBarberRelationInfo record);

    int deleteByStoreIdAndBarberId(StoreBarberRelationInfo storeBarberRelationInfo);

    StoreBarberRelationInfo getByStoreIdAndBarberId(StoreBarberRelationInfo storeBarberRelationInfo);
}