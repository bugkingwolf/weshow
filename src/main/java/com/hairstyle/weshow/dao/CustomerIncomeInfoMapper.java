package com.hairstyle.weshow.dao;

import java.util.List;

import com.hairstyle.weshow.domain.CustomerAccountInfo;
import com.hairstyle.weshow.domain.CustomerIncomeInfo;

public interface CustomerIncomeInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CustomerIncomeInfo record);

    int insertSelective(CustomerIncomeInfo record);

    CustomerIncomeInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CustomerIncomeInfo record);

    int updateByPrimaryKey(CustomerIncomeInfo record);

    List<CustomerIncomeInfo> getByCustomerId(CustomerAccountInfo customerAccountInfo);

    Integer getTotalIncomeByBarberId(Integer barberId);
}