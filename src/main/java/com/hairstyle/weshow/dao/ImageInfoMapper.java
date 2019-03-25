package com.hairstyle.weshow.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hairstyle.weshow.domain.ImageInfo;

public interface ImageInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ImageInfo record);

    int insertSelective(ImageInfo record);

    ImageInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ImageInfo record);

    int updateByPrimaryKey(ImageInfo record);

    List<ImageInfo> getByStoreId(Integer storeId);

    List<ImageInfo> getImageByfaceId(String faceId);

    List<ImageInfo> getImageByfaceIdAndDeviceNo(@Param("faceId") String faceId, @Param("deviceNo") String deviceNo);
}