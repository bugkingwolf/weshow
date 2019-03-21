package com.hairstyle.weshow.dao;

import java.util.List;

import com.hairstyle.weshow.domain.BarberInfo;

public interface BarberInfoMapper {
    int deleteByPrimaryKey(Integer barberId);

    int insert(BarberInfo record);

    int insertSelective(BarberInfo record);

    BarberInfo selectByPrimaryKey(Integer barberId);

    int updateByPrimaryKeySelective(BarberInfo record);

    int updateByPrimaryKey(BarberInfo record);

	List<BarberInfo> getByStoreId(Integer storeId);

	List<BarberInfo> list(BarberInfo barberInfo);

	BarberInfo getByOrderId(Integer orderId);

	String getBarberHeamUrlById(Integer barberId);

	int getBarberCountByFaceId(String faceId);

	List<BarberInfo> getMyBarberList(Integer customerId);

	List<BarberInfo> getHistoryBarberList(Integer storeId);

	BarberInfo getByCustomerId(Integer customerId);

	int updateStoreId(BarberInfo barberInfo);

	BarberInfo getBarberByFaceId(String faceId);

	List<BarberInfo> getListOrder(BarberInfo barberInfo);

}