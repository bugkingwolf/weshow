package com.hairstyle.weshow.dao;

import java.util.List;

import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.CustomerInfo;
import com.hairstyle.weshow.domain.StoreInfo;

public interface StoreInfoMapper {
    int deleteByPrimaryKey(Integer storeId);

    int insert(StoreInfo record);

    int insertSelective(StoreInfo record);

    StoreInfo selectByPrimaryKey(Integer storeId);

    int updateByPrimaryKeySelective(StoreInfo record);

    int updateByPrimaryKey(StoreInfo record);

    List<StoreInfo> list(StoreInfo storeInfo);

	List<StoreInfo> getServicingStore(BarberInfo barberInfo);

	List<StoreInfo> getMyStoreList(CustomerInfo customerInfo);

	Integer getSumIncome(Integer storeId);

	Integer getOrderNum(Integer storeId);

	List<StoreInfo> listNearStore(StoreInfo storeInfo);

	List<StoreInfo> getListOrder(StoreInfo storeInfo);

}