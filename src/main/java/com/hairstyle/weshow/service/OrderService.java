package com.hairstyle.weshow.service;

import java.util.List;

import com.hairstyle.weshow.domain.OrderInfo;

public interface OrderService {

    OrderInfo insert(OrderInfo orderInfo) throws Exception;

    List<OrderInfo> getBarberOrderlist(OrderInfo orderInfo);

    OrderInfo getOrderDetail(OrderInfo orderInfo);

    int orderConfirm(OrderInfo orderInfo);

    int updateImage(Integer orderId, Integer haircutImgId);

    List<OrderInfo> getCustomerOrderlist(OrderInfo orderInfo);

    int finishHaircut(Integer orderId);

    int validateCode(int orderId, int code);

}
