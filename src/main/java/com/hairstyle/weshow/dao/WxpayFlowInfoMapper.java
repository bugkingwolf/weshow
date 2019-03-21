package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.WxpayFlowInfo;

public interface WxpayFlowInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(WxpayFlowInfo record);

    int insertSelective(WxpayFlowInfo record);

    WxpayFlowInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WxpayFlowInfo record);

    int updateByPrimaryKey(WxpayFlowInfo record);
}