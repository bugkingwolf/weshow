package com.hairstyle.weshow.dao;

import java.util.List;

import com.hairstyle.weshow.domain.BarberServiceInfo;

public interface BarberServiceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BarberServiceInfo record);

    int insertSelective(BarberServiceInfo record);

    BarberServiceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BarberServiceInfo record);

    int updateByPrimaryKey(BarberServiceInfo record);

	List<BarberServiceInfo> getByBarberId(Integer barberId);

	int deleteByBarberId(Integer barberId);
}