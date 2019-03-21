package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.ExtractInfo;

public interface ExtractInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ExtractInfo record);

    int insertSelective(ExtractInfo record);

    ExtractInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExtractInfo record);

    int updateByPrimaryKey(ExtractInfo record);
}