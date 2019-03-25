package com.hairstyle.weshow.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aliyuncs.exceptions.ClientException;
import com.hairstyle.weshow.domain.BarberAddressInfo;
import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.SubscribeManagementInfo;
import com.hairstyle.weshow.exception.ImgException;

public interface BarberService {

    BarberInfo getBarberInfo(Integer barberId);

    List<BarberInfo> list(BarberInfo barberInfo);

    Integer subscribeManagement(SubscribeManagementInfo subscribeManagementInfo);

    List<Integer> getPosition(Integer barberId);

    int sendSms(String mobile) throws ClientException;

    int registerBarber(Integer customerId, String mobile, String code, String idCard, MultipartFile faceImageFile,
                       MultipartFile faceIdCardImageFile, MultipartFile backIdCardImageFile) throws ImgException;

    BarberInfo getBarberHome(Integer barberId);

    int insertBarberAddress(BarberAddressInfo barberAddressInfo);

    int barberUpdate(BarberInfo barberInfo, String barberImageUrl) throws Exception;

}
