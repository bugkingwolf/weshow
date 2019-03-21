package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.BarberAddressInfo;

public interface BarberAddressInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BarberAddressInfo record);

    int insertSelective(BarberAddressInfo record);

    BarberAddressInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BarberAddressInfo record);

    int updateByPrimaryKey(BarberAddressInfo record);
}