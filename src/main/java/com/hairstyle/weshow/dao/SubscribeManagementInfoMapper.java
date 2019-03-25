package com.hairstyle.weshow.dao;

import java.util.List;

import com.hairstyle.weshow.domain.SubscribeManagementInfo;

public interface SubscribeManagementInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SubscribeManagementInfo record);

    int insertSelective(SubscribeManagementInfo record);

    SubscribeManagementInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SubscribeManagementInfo record);

    int updateByPrimaryKey(SubscribeManagementInfo record);

    List<Integer> getByBarberId(Integer barberId);

    SubscribeManagementInfo getByTime(int time);

    int deleteByTime(Integer id);
}