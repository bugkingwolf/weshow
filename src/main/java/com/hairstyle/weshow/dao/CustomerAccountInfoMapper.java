package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.CustomerAccountInfo;

public interface CustomerAccountInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerAccountInfo record);

    int insertSelective(CustomerAccountInfo record);

    CustomerAccountInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerAccountInfo record);

    int updateByPrimaryKey(CustomerAccountInfo record);

	int updateByCustomerIdSelective(CustomerAccountInfo customerAccountInfo);

	CustomerAccountInfo getByCustomerId(Integer customerId);
}