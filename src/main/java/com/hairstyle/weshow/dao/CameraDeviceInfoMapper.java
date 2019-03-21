package com.hairstyle.weshow.dao;

import com.hairstyle.weshow.domain.CameraDeviceInfo;

public interface CameraDeviceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CameraDeviceInfo record);

    int insertSelective(CameraDeviceInfo record);

    CameraDeviceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CameraDeviceInfo record);

    int updateByPrimaryKey(CameraDeviceInfo record);
}