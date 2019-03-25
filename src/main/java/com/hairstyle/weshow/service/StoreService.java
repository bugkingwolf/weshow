package com.hairstyle.weshow.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.CustomerInfo;
import com.hairstyle.weshow.domain.OrderInfo;
import com.hairstyle.weshow.domain.StoreInfo;
import com.hairstyle.weshow.exception.ImgException;

public interface StoreService {

    List<StoreInfo> list(StoreInfo storeInfo);

    StoreInfo detail(StoreInfo storeInfo);

    List<StoreInfo> getServicingStore(BarberInfo barberInfo);

    List<StoreInfo> getMyStoreList(CustomerInfo customerInfo);

    int addStore(StoreInfo storeInfo, MultipartFile storeImgFile) throws ImgException;

    int shareStore(StoreInfo storeInfo);

    StoreInfo getShareStoreDetail(StoreInfo storeInfo);

    List<BarberInfo> getHistoryBarber(Integer storeId);

    List<CustomerInfo> getHistoryCustomer(Integer storeId);

    List<OrderInfo> getHistoreOrder(Integer storeId);

    int bindBarberAndStore(Integer storeId, Integer barberId);

    int removeStore(Integer storeId, Integer barberId);

}
