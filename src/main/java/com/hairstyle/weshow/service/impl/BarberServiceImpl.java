package com.hairstyle.weshow.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.baidu.aip.face.AipFace;
import com.hairstyle.weshow.common.Constant;
import com.hairstyle.weshow.dao.BarberAddressInfoMapper;
import com.hairstyle.weshow.dao.BarberInfoMapper;
import com.hairstyle.weshow.dao.BarberServiceInfoMapper;
import com.hairstyle.weshow.dao.BarberServiceServiceInfoMapper;
import com.hairstyle.weshow.dao.CustomerIncomeInfoMapper;
import com.hairstyle.weshow.dao.CustomerInfoMapper;
import com.hairstyle.weshow.dao.ImageInfoMapper;
import com.hairstyle.weshow.dao.SmsInfoMapper;
import com.hairstyle.weshow.dao.SubscribeManagementInfoMapper;
import com.hairstyle.weshow.domain.BarberAddressInfo;
import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.BarberServiceInfo;
import com.hairstyle.weshow.domain.BarberServiceServiceInfo;
import com.hairstyle.weshow.domain.CustomerInfo;
import com.hairstyle.weshow.domain.ImageInfo;
import com.hairstyle.weshow.domain.OrderInfo;
import com.hairstyle.weshow.domain.SmsInfo;
import com.hairstyle.weshow.domain.SubscribeManagementInfo;
import com.hairstyle.weshow.exception.ImgException;
import com.hairstyle.weshow.service.BarberService;
import com.hairstyle.weshow.service.CustomerService;
import com.hairstyle.weshow.service.OrderService;
import com.hairstyle.weshow.utils.AliyunOSSClientUtil;
import com.hairstyle.weshow.utils.SmsUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BarberServiceImpl implements BarberService {

    @Autowired
    BarberInfoMapper barberInfoMapper;
    @Autowired
    BarberServiceInfoMapper barberServiceInfoMapper;
    @Autowired
    BarberServiceServiceInfoMapper barberServiceServiceInfoMapper;
    @Autowired
    ImageInfoMapper imageInfoMapper;
    @Autowired
    SubscribeManagementInfoMapper subscribeManagementInfoMapper;
    @Autowired
    SmsInfoMapper smsInfoMapper;
    @Autowired
    CustomerIncomeInfoMapper customerIncomeInfoMapper;
    @Autowired
    OrderService orderServiceImpl;
    @Autowired
    CustomerService customerServiceImpl;
    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    BarberAddressInfoMapper barberAddressInfoMapper;

    @Autowired
    private AliyunOSSClientUtil aliyunOSSClientUtil;

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public BarberInfo getBarberInfo(Integer barberId) {
        BarberInfo barberInfo = barberInfoMapper.selectByPrimaryKey(barberId);

        ImageInfo headImageInfo = imageInfoMapper.selectByPrimaryKey(barberInfo.getHeadId());
        if (headImageInfo != null) {
            barberInfo.setHeadImageUrl(headImageInfo.getUrl());
        }

        List<BarberServiceInfo> barberServiceList = barberServiceInfoMapper.getByBarberId(barberId);
        barberInfo.setBarberServiceList(barberServiceList);

        List<Integer> positions = getPosition(barberId);
        barberInfo.setPositions(positions);

        return barberInfo;
    }

    @Override
    public List<BarberInfo> list(BarberInfo barberInfo) {

        List<BarberInfo> list = barberInfoMapper.getListOrder(barberInfo);

//		List<BarberInfo> list = barberInfoMapper.list(barberInfo);
//		BigDecimal longitude = new BigDecimal(0);
//		BigDecimal latitude = new BigDecimal(0);
//		if (barberInfo.getLongitude() != null && barberInfo.getLatitude() != null) {
//			longitude = barberInfo.getLongitude();
//			latitude = barberInfo.getLatitude();
//		}
//		log.info("==============longitude=" + longitude + ",latitude" + latitude);

        for (BarberInfo barber : list) {
            ImageInfo headImageInfo = imageInfoMapper.selectByPrimaryKey(barber.getHeadId());
            barber.setHeadImageUrl(headImageInfo.getUrl());

//			BarberAddressInfo barberAddressInfo = barber.getBarberAddress();
//			if (barberAddressInfo != null) {
//				log.info("==============理发师位置信息,barberId:" + barber.getBarberId());
//				BigDecimal barberLongitude = barberAddressInfo.getLongitude();
//				BigDecimal barberLatitude = barberAddressInfo.getLatitude();
//				if (longitude != null && latitude != null && barberLongitude != null && barberLatitude != null
//						&& longitude.compareTo(BigDecimal.ZERO) != 0 && latitude.compareTo(BigDecimal.ZERO) != 0
//						&& barberLongitude.compareTo(BigDecimal.ZERO) != 0
//						&& barberLatitude.compareTo(BigDecimal.ZERO) != 0) {
//					// 计算距离
//					double distance = DistanceUtils.getDistance(longitude, latitude, barberLongitude, barberLatitude);
//					barber.setDistance(distance);
//				}
//			}
        }
        return list;
    }

    @Override
    public Integer subscribeManagement(SubscribeManagementInfo subscribeManagementInfo) {

        Integer barberId = subscribeManagementInfo.getBarberId();
        Integer position = subscribeManagementInfo.getPosition();
        Integer type = subscribeManagementInfo.getType();// 0:设置不可预约 1:设置可预约

        int time = getsubscribeTime(position);

        SubscribeManagementInfo subscribeManagement = new SubscribeManagementInfo();
        subscribeManagement.setBarberId(barberId);
        subscribeManagement.setTime(time);

        SubscribeManagementInfo subscribeManagementVo = subscribeManagementInfoMapper.getByTime(time);

        if (type == 0) {
            log.info("========设置不可预约时间,barberId:" + barberId);
            if (subscribeManagementVo == null) {
                log.info("========即将插入不可预约时间,barberId:" + barberId);
                int insert = subscribeManagementInfoMapper.insert(subscribeManagement);
                if (insert == 0) {
                    return 0;
                }
                log.info("========插入不可预约时间成功,barberId:" + barberId);
            }
        } else if (type == 1) {
            log.info("========设置可预约时间,barberId:" + barberId);
            if (subscribeManagementVo != null) {
                log.info("========即将删除不可预约时间,barberId:" + barberId);
                int deleted = subscribeManagementInfoMapper.deleteByTime(subscribeManagementVo.getTime());
                if (deleted == 0) {
                    return 0;
                }
                log.info("========删除不可预约时间成功,barberId:" + barberId);
            }
        }
        return 1;
    }

    @Override
    public List<Integer> getPosition(Integer barberId) {
        List<Integer> positions = new ArrayList<Integer>();// 不可预约时间对应位置
        List<Integer> times = subscribeManagementInfoMapper.getByBarberId(barberId);// 获取理发师不可预约时间对应位置
        if (times != null && !times.isEmpty()) {
            positions = getPositions(times);// 不可预约时间对应位置
        }
        return positions;
    }

    @Override
    public BarberInfo getBarberHome(Integer barberId) {
        BarberInfo barberInfo = barberInfoMapper.selectByPrimaryKey(barberId);

        ImageInfo headImageInfo = imageInfoMapper.selectByPrimaryKey(barberInfo.getHeadId());
        if (headImageInfo != null) {
            barberInfo.setHeadImageUrl(headImageInfo.getUrl());
        }

        List<Integer> positions = getPosition(barberId);
        barberInfo.setPositions(positions);

        //订单数
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setBarberId(barberId);
        orderInfo.setStatusStr("6");
        List<OrderInfo> orderlist = orderServiceImpl.getBarberOrderlist(orderInfo);
        barberInfo.setOrderNum(orderlist.size());

        //理发师顾客数
        List<CustomerInfo> myCustomerList = customerServiceImpl.getMyCustomerList(barberId);
        barberInfo.setCustomerNum(myCustomerList.size());

        //理发师总收入
        Integer amount = customerIncomeInfoMapper.getTotalIncomeByBarberId(barberId);
        barberInfo.setTotalIncome(amount);

        return barberInfo;
    }

    /**
     * 获取时间字符串
     *
     * @param position
     * @return
     */
    public int getsubscribeTime(Integer position) {
        String times = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar now = Calendar.getInstance();
        Date timeToday = now.getTime();

        now.add(Calendar.DATE, 1);
        Date timeTomorrow = now.getTime();

        now.add(Calendar.DATE, 1);
        Date timeAfterTomorrow = now.getTime();

        switch (position) {
            case 1:
                times = sdf.format(timeToday) + Constant.AM;
                break;
            case 2:
                times = sdf.format(timeToday) + Constant.PM;
                break;
            case 3:
                times = sdf.format(timeToday) + Constant.MN;
                break;
            case 4:
                times = sdf.format(timeTomorrow) + Constant.AM;
                break;
            case 5:
                times = sdf.format(timeTomorrow) + Constant.PM;
                break;
            case 6:
                times = sdf.format(timeTomorrow) + Constant.MN;
                break;
            case 7:
                times = sdf.format(timeAfterTomorrow) + Constant.AM;
                break;
            case 8:
                times = sdf.format(timeAfterTomorrow) + Constant.PM;
                break;
            case 9:
                times = sdf.format(timeAfterTomorrow) + Constant.MN;
                break;

        }
        int time = Integer.parseInt(times);
        return time;
    }

    /**
     * 不可预约时间对应位置
     *
     * @param times
     * @return
     */
    private List<Integer> getPositions(List<Integer> times) {

        List<Integer> positions = new ArrayList<Integer>();
        for (Integer time : times) {

            int position = 0;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            Calendar now = Calendar.getInstance();
            Date timeToday = now.getTime();
            int time1 = Integer.parseInt(sdf.format(timeToday) + Constant.AM);
            int time2 = Integer.parseInt(sdf.format(timeToday) + Constant.PM);
            int time3 = Integer.parseInt(sdf.format(timeToday) + Constant.MN);

            now.add(Calendar.DATE, 1);
            Date timeTomorrow = now.getTime();
            int time4 = Integer.parseInt(sdf.format(timeTomorrow) + Constant.AM);
            int time5 = Integer.parseInt(sdf.format(timeTomorrow) + Constant.PM);
            int time6 = Integer.parseInt(sdf.format(timeTomorrow) + Constant.MN);

            now.add(Calendar.DATE, 1);
            Date timeAfterTomorrow = now.getTime();
            int time7 = Integer.parseInt(sdf.format(timeAfterTomorrow) + Constant.AM);
            int time8 = Integer.parseInt(sdf.format(timeAfterTomorrow) + Constant.PM);
            int time9 = Integer.parseInt(sdf.format(timeAfterTomorrow) + Constant.MN);

            if (time == time1) {
                position = 1;
            } else if (time == time2) {
                position = 2;
            } else if (time == time3) {
                position = 3;
            } else if (time == time4) {
                position = 4;
            } else if (time == time5) {
                position = 5;
            } else if (time == time6) {
                position = 6;
            } else if (time == time7) {
                position = 7;
            } else if (time == time8) {
                position = 8;
            } else if (time == time9) {
                position = 9;
            }

            if (position != 0) {
                positions.add(position);
            }

        }
        return positions;
    }

    @Override
    public int sendSms(String mobile) throws ClientException {

        SmsInfo sms = smsInfoMapper.getByMobile(mobile);
        if (sms != null) {
            //此手机号已注册
            return 2;
        }

        int status = 0;
        // 发送短信
        String code = SmsUtils.getCode();

        // ValueOperations<String, String> opsForValue =
        // redisTemplate.opsForValue();
        // opsForValue.set("redisKey","cluster test");
        // System.out.println(opsForValue.get("redisKey"));

        // 保存到缓存
        // String key = mobile + CacheNameSpace.SMS_VERIFY_CODE;
        // redisUtil.set(key, code);

        SendSmsResponse sendSms = SmsUtils.sendSms(mobile, code);
        String rescode = sendSms.getCode();

        SmsInfo smsInfo = new SmsInfo();
        smsInfo.setMobile(mobile);
        smsInfo.setCode(code);
        smsInfo.setCreatedTime(new Date());

        if ("OK".equals(rescode)) {
            log.info("验证码发送成功,手机号:" + mobile + ",验证码:" + code);
            smsInfo.setStatus(1);
            status = 1;
        } else {
            smsInfo.setStatus(0);
            log.info("验证码发送失败,手机号:" + mobile + ",错误信息:" + sendSms.getMessage());
        }
        smsInfoMapper.insert(smsInfo);
        return status;
    }

    @Override
    public int registerBarber(Integer customerId, String mobile, String code, String idCard, MultipartFile faceImageFile,
                              MultipartFile faceIdCardImageFile, MultipartFile backIdCardImageFile) throws ImgException {
        SmsInfo smsInfo = smsInfoMapper.getByMobile(mobile);
        if (smsInfo != null && smsInfo.getCode() != null) {
            if (!code.equals(smsInfo.getCode())) {
                return 2;//短信验证码不正确
            }
        }
        //上传阿里云
//        AliyunOSSClientUtil ossClient = new AliyunOSSClientUtil();
        String faceimgUrl = aliyunOSSClientUtil.uploadImg2Oss(faceImageFile);
        String faceIdCardImgUrl = aliyunOSSClientUtil.uploadImg2Oss(faceIdCardImageFile);
        String backIdCardImgUrl = aliyunOSSClientUtil.uploadImg2Oss(backIdCardImageFile);

        // 初始化一个AipFace
        AipFace client = new AipFace(Constant.APP_ID, Constant.API_KEY, Constant.SECRET_KEY);
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject res = client.addUser(faceimgUrl, Constant.IMAGE_TYPE_URL, "imcou01", String.valueOf(customerId), options);

        String errorMsg = res.getJSONObject("error_msg").toString();
        if ("SUCCESS".equals(errorMsg)) {

            BarberInfo barberInfo = new BarberInfo();
            barberInfo.setUserId(customerId);
            barberInfo.setMobile(mobile);
            barberInfo.setIdCard(idCard);
            barberInfo.setRegisterfaceurl(faceimgUrl);
            barberInfo.setIdcardfaceurl(faceIdCardImgUrl);
            barberInfo.setIdcardbackurl(backIdCardImgUrl);
            barberInfo.setStatus(1);
            barberInfo.setServiceStatus(1);
            barberInfo.setRegisterTime(new Date());
            barberInfo.setFaceId(customerId + "");
            BarberInfo barber = barberInfoMapper.getByCustomerId(customerId);
            if (barber == null) {
                int insert = barberInfoMapper.insertSelective(barberInfo);
                if (insert > 0) {
                    log.info("理发师注册申请成功 : customerid=" + customerId);
                    return 1;
                }
            } else {
                log.info("此理发师已经注册过 : customerid=" + customerId);
                return 3;
            }


        }
        return 0;
    }

    @Override
    public int insertBarberAddress(BarberAddressInfo barberAddressInfo) {
        barberAddressInfo.setCreatedTime(new Date());
        return barberAddressInfoMapper.insertSelective(barberAddressInfo);
    }

    @Override
    public int barberUpdate(BarberInfo barberInfo, String barberImageUrl) throws Exception {

        Integer barberId = barberInfo.getBarberId();

        if (barberImageUrl != null) {
            ImageInfo imageInfo = new ImageInfo();
            imageInfo.setUrl(barberImageUrl);
            imageInfo.setCreateTime(new Date());
            imageInfo.setType(2);
            imageInfo.setBarberId(barberId);
            imageInfo.setDeleted(1);
            int insertimage = imageInfoMapper.insertSelective(imageInfo);
            if (insertimage > 0) {
                log.info("保存头像成功,barberId:" + barberId);
            }
            barberInfo.setHeadId(imageInfo.getId());
        }
        //更新理发师基本信息
        int updatebarber = barberInfoMapper.updateByPrimaryKeySelective(barberInfo);//

        if (updatebarber > 0) {

            log.info("理发师信息更新成功,barberId:" + barberId);
            List<BarberServiceInfo> barberServiceList = barberInfo.getBarberServiceList();

            if (barberServiceList != null && !barberServiceList.isEmpty()) {
                //删除所有理发师大项目
                int deleted = barberServiceInfoMapper.deleteByBarberId(barberId);
                for (BarberServiceInfo barberServiceInfo : barberServiceList) {
//					List<BarberServiceInfo> byBarberId = barberServiceInfoMapper.getByBarberId(barberId);
                    //新增大项目
                    int insertBarberService = barberServiceInfoMapper.insertSelective(barberServiceInfo);
                    Integer barberServiceId = barberServiceInfo.getId();//大项目id

                    //删除理发师小项目
                    int deleted2 = barberServiceServiceInfoMapper.deleteByBarberServiceId(barberServiceId);

                    List<BarberServiceServiceInfo> barberServiceServiceList = barberServiceInfo.getBarberServiceServiceList();
                    for (BarberServiceServiceInfo barberServiceServiceInfo : barberServiceServiceList) {
                        barberServiceServiceInfo.setBarberServiceId(barberServiceId);
                        //新增小项目
                        int insertbarberServiceService = barberServiceServiceInfoMapper.insertSelective(barberServiceServiceInfo);
                    }

                }
            }

        }
        log.info("修改理发师信息成功: 理发师id:" + barberId);
        return 1;
    }


    public static void main(String[] args) {
        // 初始化一个AipFace
        AipFace client = new AipFace(Constant.APP_ID, Constant.API_KEY, Constant.SECRET_KEY);
        // 参数
        // HashMap<String, String> hashMap = new HashMap<String, String>();
        // hashMap.put("face_field",
        // "age,beauty,faceshape,gender,glasses,race");
        // JSONObject res =
        // client.detect("http://www.hkcgart.com/ueditor/php/upload/image/20170512/1494564069312798.jpg",
        // Constant.IMAGE_TYPE_URL, hashMap);
        // System.out.println(res.toString());

        // AipFace client = new AipFace(Constant.APP_ID, Constant.API_KEY,
        // Constant.SECRET_KEY);
        HashMap<String, String> options = new HashMap<String, String>();
        JSONObject res = client.addUser("http://www.hkcgart.com/ueditor/php/upload/image/20170512/1494564069312798.jpg",
                Constant.IMAGE_TYPE_URL, "imcou01", "222", options);
        System.out.println(res.toString());
    }


}
