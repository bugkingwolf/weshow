package com.hairstyle.weshow.dao;

import java.util.List;

import com.hairstyle.weshow.domain.CameraDeviceInfo;
import com.hairstyle.weshow.domain.FaceInfo;

public interface FaceInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FaceInfo record);

    int insertSelective(FaceInfo record);

    FaceInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FaceInfo record);

    int updateByPrimaryKey(FaceInfo record);

    List<FaceInfo> getCustomerByStoreId(Integer storeId);

    List<FaceInfo> getFaceInfoBySeatNo(CameraDeviceInfo cameraDeviceInfo);

    FaceInfo selectByFaceId(String faceId);

    int updateByFaceId(FaceInfo faceInfo);
}