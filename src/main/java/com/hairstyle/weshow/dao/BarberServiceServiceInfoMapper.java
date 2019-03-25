package com.hairstyle.weshow.dao;

import java.util.List;

import com.hairstyle.weshow.domain.BarberServiceServiceInfo;

public interface BarberServiceServiceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BarberServiceServiceInfo record);

    int insertSelective(BarberServiceServiceInfo record);

    BarberServiceServiceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BarberServiceServiceInfo record);

    int updateByPrimaryKey(BarberServiceServiceInfo record);

    List<BarberServiceServiceInfo> getByBarberServiceId(Integer barberServiceId);

	int deleteByBarberServiceId(Integer barberServiceId);
}