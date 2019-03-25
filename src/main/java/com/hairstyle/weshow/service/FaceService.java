package com.hairstyle.weshow.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hairstyle.weshow.domain.CameraDeviceInfo;
import com.hairstyle.weshow.domain.CustomerFaceInfo;
import com.hairstyle.weshow.domain.FaceInfo;
import com.hairstyle.weshow.exception.ImgException;

public interface FaceService {

    CustomerFaceInfo getFaceInfo(Integer customerId, String url);

    int faceUpload(MultipartFile faceImgFile, FaceInfo faceInfo) throws ImgException;

    List<FaceInfo> getFaceInfoBySeatNo(CameraDeviceInfo cameraDeviceInfo);
}
