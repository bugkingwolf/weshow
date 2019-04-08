package com.hairstyle.weshow.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baidu.aip.face.AipFace;
import com.hairstyle.weshow.common.BarberStatusEnum;
import com.hairstyle.weshow.common.Constant;
import com.hairstyle.weshow.common.HairCutStatusEnum;
import com.hairstyle.weshow.dao.BarberInfoMapper;
import com.hairstyle.weshow.dao.CustomerFaceInfoMapper;
import com.hairstyle.weshow.dao.FaceInfoMapper;
import com.hairstyle.weshow.dao.ImageInfoMapper;
import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.CameraDeviceInfo;
import com.hairstyle.weshow.domain.CustomerFaceInfo;
import com.hairstyle.weshow.domain.FaceInfo;
import com.hairstyle.weshow.domain.ImageInfo;
import com.hairstyle.weshow.exception.ImgException;
import com.hairstyle.weshow.service.FaceService;
import com.hairstyle.weshow.utils.AliyunOSSClientUtil;
import com.hairstyle.weshow.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FaceServiceImpl implements FaceService {

    @Autowired
    CustomerFaceInfoMapper customerFaceInfoMapper;
    @Autowired
    ImageInfoMapper imageInfoMapper;
    @Autowired
    FaceInfoMapper faceInfoMapper;
    @Autowired
    BarberInfoMapper barberInfoMapper;
    @Autowired
    private AliyunOSSClientUtil aliyunOSSClientUtil;

    @Override
    public CustomerFaceInfo getFaceInfo(Integer customerId, String url) {
        // 初始化一个AipFace
        AipFace client = new AipFace(Constant.APP_ID, Constant.API_KEY, Constant.SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用接口
//        String path = imageInfo.getUrl();
        //参数
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("face_field", "age,beauty,faceshape,gender,glasses,race");
        JSONObject res = client.detect(url, Constant.IMAGE_TYPE_URL, hashMap);
        //格式化百度数据
        CustomerFaceInfo customerFaceInfo = getBaiduFaceInfo(res, customerId);
        log.info("========百度人脸识别信息:" + customerFaceInfo);
        customerFaceInfo.setUrl(url);

        CustomerFaceInfo customerFace = customerFaceInfoMapper.selectByCustomerId(customerId);
        if (customerFace == null) {
            log.info("========插入人脸信息,customerId=" + customerId);
            customerFaceInfoMapper.insert(customerFaceInfo);
        } else {
            log.info("========更新人脸信息,customerId=" + customerId);
            customerFaceInfo.setId(customerFace.getId());
            customerFaceInfoMapper.updateByPrimaryKey(customerFaceInfo);
        }

        return customerFaceInfo;
    }


    private CustomerFaceInfo getBaiduFaceInfo(JSONObject res, Integer customerId) {

        CustomerFaceInfo customerFaceInfo = new CustomerFaceInfo();
        String faceListStr = res.getJSONObject("result").getJSONArray("face_list").get(0).toString();
        JSONObject faceListJsonObject = JsonUtils.transform(faceListStr, JSONObject.class);

        Integer age = Integer.parseInt(faceListJsonObject.get("age").toString());
        Double beauty = Double.parseDouble(faceListJsonObject.get("beauty").toString());
        String gender = JsonUtils.transform(faceListJsonObject.get("gender").toString(), JSONObject.class).get("type").toString();
        String faceShape = JsonUtils.transform(faceListJsonObject.get("face_shape").toString(), JSONObject.class).get("type").toString();
        String race = JsonUtils.transform(faceListJsonObject.get("race").toString(), JSONObject.class).get("type").toString();
        String glasses = JsonUtils.transform(faceListJsonObject.get("glasses").toString(), JSONObject.class).get("type").toString();
        customerFaceInfo.setAge(age);
        customerFaceInfo.setBeauty(beauty);
        customerFaceInfo.setGender(gender);
        customerFaceInfo.setFaceShape(faceShape);
        customerFaceInfo.setRace(race);
        customerFaceInfo.setGlasses(glasses);
        customerFaceInfo.setCustomerId(customerId);
        customerFaceInfo.setCreatedTime(new Date());
        customerFaceInfo.setUpdateTime(new Date());

        return customerFaceInfo;
    }


    @Override
    public int faceUpload(MultipartFile faceImgFile, FaceInfo faceInfo) throws ImgException {

        int status = 0;
        int row = 0;
        Date createTime = new Date();
        //上传阿里云
//        AliyunOSSClientUtil ossClient = new AliyunOSSClientUtil();
        String imgUrl = aliyunOSSClientUtil.uploadImg2Oss(faceImgFile);

        String deviceNo = faceInfo.getDeviceNo();
        Integer hairStatus = faceInfo.getHairStatus();
        String faceToken = faceInfo.getFaceToken();
        String trackId = faceInfo.getTrackId();
        String faceId = faceInfo.getFaceId();
        Integer storeId = faceInfo.getStoreId();
        //=========================================================
        //图片对象
        Integer id = 0;
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setUrl(imgUrl);
        imageInfo.setDeviceNo(deviceNo);
        imageInfo.setType(Constant.CUSTOMER_IMG_TYPE);
        imageInfo.setFaceToken(faceToken);
        imageInfo.setTrackId(trackId);
        imageInfo.setFaceId(faceId);
        imageInfo.setCreateTime(createTime);
        imageInfo.setDeleted(1);

        switch (hairStatus) {
            case 1:
                imageInfo.setHairStatus(1);
                break;
            case 2:
                imageInfo.setHairStatus(2);
                break;
            case 3:
                imageInfo.setHairStatus(3);
                break;
            case 6:
                imageInfo.setHairStatus(4);
                break;
        }

        int insert = imageInfoMapper.insert(imageInfo);

        //face对象
        if (insert > 0) {
            id = imageInfo.getId();

            switch (hairStatus) {
                case 1:
                    faceInfo.setComingImgId(id);
                    faceInfo.setComingTime(createTime);
                    break;
                case 2:
                    faceInfo.setShampooImgId(id);
                    break;
                case 3:
                    faceInfo.setHaircutImgId(id);
                    break;
                case 6:
                    faceInfo.setGetoutImgId(id);
                    break;
            }

            FaceInfo face = faceInfoMapper.selectByFaceId(faceId);

            faceInfo.setStatus(1);
            faceInfo.setCreatedTime(createTime);
            if (face == null) {
                row = faceInfoMapper.insert(faceInfo);
                log.info("插入顾客face=======条数:" + row);
            } else {
                row = faceInfoMapper.updateByFaceId(faceInfo);
                log.info("更新顾客face=======条数:" + row);
            }
            if (row > 0) {
                status = 1;
            }

        }
        //=========================================================

        //更新理发师位置
        BarberInfo barber = barberInfoMapper.getBarberByFaceId(faceId);
        if (barber != null) {
            Integer barberId = barber.getBarberId();
            if (hairStatus == HairCutStatusEnum.COMEING.getCode()) {
                BarberInfo barberInfo = new BarberInfo();
                barberInfo.setStoreId(storeId);
                barberInfo.setFaceId(faceId);
                barberInfo.setServiceStatus(BarberStatusEnum.FREEING.getCode());//设为空闲中 即在线
                int update = barberInfoMapper.updateStoreId(barberInfo);
                if (update > 0) {
                    log.info("理发师进店成功======,理发师:" + barberId + ",店铺:" + storeId);
                }
            } else if (hairStatus == HairCutStatusEnum.GETOUT.getCode()) {
                BarberInfo barberInfo = new BarberInfo();
                barberInfo.setStoreId(null);
                barberInfo.setFaceId(faceId);
                barberInfo.setServiceStatus(BarberStatusEnum.RESTING.getCode());//这位休息中,即离线
                int update = barberInfoMapper.updateStoreId(barberInfo);
                if (update > 0) {
                    log.info("理发师离店成功======,理发师:" + barberId + ",店铺:" + storeId);
                }
            }
        }

        return status;
    }


    @Override
    public List<FaceInfo> getFaceInfoBySeatNo(CameraDeviceInfo cameraDeviceInfo) {
        List<FaceInfo> list = faceInfoMapper.getFaceInfoBySeatNo(cameraDeviceInfo);
        for (FaceInfo faceInfo : list) {
            Integer haircutImgId = faceInfo.getHaircutImgId();
            ImageInfo imageInfo = imageInfoMapper.selectByPrimaryKey(haircutImgId);
            faceInfo.setHaircutImg(imageInfo);
        }
        return list;
    }
}
