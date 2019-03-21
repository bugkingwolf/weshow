package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.SmsInfo;

public interface SmsInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SmsInfo record);

    int insertSelective(SmsInfo record);

    SmsInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsInfo record);

    int updateByPrimaryKey(SmsInfo record);

	SmsInfo getByMobile(String mobile);
}