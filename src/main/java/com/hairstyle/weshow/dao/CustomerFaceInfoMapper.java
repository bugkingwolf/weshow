package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.CustomerFaceInfo;

public interface CustomerFaceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerFaceInfo record);

    int insertSelective(CustomerFaceInfo record);

    CustomerFaceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerFaceInfo record);

    int updateByPrimaryKey(CustomerFaceInfo record);

    CustomerFaceInfo selectByCustomerId(Integer customerId);
}