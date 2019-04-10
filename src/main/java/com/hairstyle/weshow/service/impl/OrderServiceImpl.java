package com.hairstyle.weshow.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hairstyle.weshow.common.OrderStatusEnum;
import com.hairstyle.weshow.dao.BarberInfoMapper;
import com.hairstyle.weshow.dao.CustomerInfoMapper;
import com.hairstyle.weshow.dao.ImageInfoMapper;
import com.hairstyle.weshow.dao.OrderInfoMapper;
import com.hairstyle.weshow.dao.OrderRelatonInfoMapper;
import com.hairstyle.weshow.dao.OrderServiceInfoMapper;
import com.hairstyle.weshow.dao.OrderServiceServiceInfoMapper;
import com.hairstyle.weshow.dao.StoreInfoMapper;
import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.CustomerInfo;
import com.hairstyle.weshow.domain.ImageInfo;
import com.hairstyle.weshow.domain.OrderInfo;
import com.hairstyle.weshow.domain.OrderRelatonInfo;
import com.hairstyle.weshow.domain.OrderServiceInfo;
import com.hairstyle.weshow.domain.OrderServiceServiceInfo;
import com.hairstyle.weshow.domain.StoreInfo;
import com.hairstyle.weshow.service.OrderService;
import com.hairstyle.weshow.utils.SmsUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderInfoMapper orderInfoMapper;
    @Autowired
    OrderServiceInfoMapper orderServiceInfoMapper;
    @Autowired
    OrderServiceServiceInfoMapper orderServiceServiceInfoMapper;
    @Autowired
    OrderRelatonInfoMapper orderRelatonInfoMapper;
    @Autowired
    StoreInfoMapper storeInfoMapper;
    @Autowired
    CustomerInfoMapper customerInfoMapper;
    @Autowired
    BarberInfoMapper barberInfoMapper;
    @Autowired
    ImageInfoMapper imageInfoMapper;

    @SuppressWarnings("unused")
    @Override
    public OrderInfo insert(OrderInfo orderInfo) throws Exception {
        Integer orderId = 0;
        Integer amount = orderInfo.getAmount();

        String code = SmsUtils.getCode();//六位预约码
        // 插入订单
        orderInfo.setCreatedTime(new Date());
        orderInfo.setCode(Integer.parseInt(code));
        orderInfo.setStatus(OrderStatusEnum.WAIT_PAY.getCode());

        int row = orderInfoMapper.insert(orderInfo);
        List<OrderServiceInfo> barberServiceList = orderInfo.getBarberServiceList();

        orderId = orderInfo.getOrderId();
        OrderRelatonInfo orderRelaton = orderInfo.getOrderRelaton();
        orderRelaton.setOrderId(orderId);
        orderRelaton.setPositions(orderRelaton.getPositions());
        orderRelaton.setStoreIds(orderRelaton.getStoreIds());
        int row2 = orderRelatonInfoMapper.insert(orderRelaton);

        for (OrderServiceInfo orderServiceInfo : barberServiceList) {

            // 插入项目(5个大项目)
            orderServiceInfo.setOrderId(orderId);
            int row3 = orderServiceInfoMapper.insert(orderServiceInfo);

            // 插入项目(子项目)
            if (orderServiceInfo.getBarberServiceServiceList() != null
                    && orderServiceInfo.getBarberServiceServiceList().size() != 0) {
                OrderServiceServiceInfo orderServiceService = orderServiceInfo.getBarberServiceServiceList().get(0);
                orderServiceService.setOrderServiceId(orderServiceInfo.getId());
                int row4 = orderServiceServiceInfoMapper.insert(orderServiceService);
            }

        }

        OrderInfo order = new OrderInfo();
        order.setOrderId(orderId);
        order.setAmount(amount);
        return order;
    }

    @Override
    public List<OrderInfo> getBarberOrderlist(OrderInfo orderInfo) {
        List<OrderInfo> orders = orderInfoMapper.getOrderlist(orderInfo);
        return setDate(orderInfo, orders);
    }

    @Override
    public List<OrderInfo> getCustomerOrderlist(OrderInfo orderInfo) {
        List<OrderInfo> orders = orderInfoMapper.getOrderlist(orderInfo);
        return setDate(orderInfo, orders);
    }

    @Override
    public OrderInfo getOrderDetail(OrderInfo orderInfo) {

        List<String> timeList = new ArrayList<String>();
        List<StoreInfo> storeList = new ArrayList<StoreInfo>();

        Integer orderId = orderInfo.getOrderId();
        OrderInfo order = orderInfoMapper.selectByPrimaryKey(orderId);

        //理发时长
        if (order.getStartTime() != null && order.getEndTime() != null) {
            order.setUseTime(getInstoreTime(order.getStartTime(), order.getEndTime()));
        }

        //set顾客信息
        CustomerInfo customerInfo = customerInfoMapper.getByOrderId(orderId);
        if (customerInfo != null) {
            order.setCustomer(customerInfo);
        }

        //set理发师信息
        BarberInfo barberInfo = barberInfoMapper.getByOrderId(orderId);
        if (barberInfo != null) {
            String headUrl = barberInfoMapper.getBarberHeamUrlById(barberInfo.getBarberId());
            barberInfo.setHeadImageUrl(headUrl);
            order.setBarber(barberInfo);
        }

        //set relation
        OrderRelatonInfo orderRelatonInfo = orderRelatonInfoMapper.selectByOrderId(orderId);
        if (orderRelatonInfo != null) {
            String positions = orderRelatonInfo.getPositions();
            String[] positionArr = positions.split(",");
            for (String position : positionArr) {
                String subscribeTime = getsubscribeTime(Integer.parseInt(position)) + "";
                timeList.add(subscribeTime);
            }
            orderRelatonInfo.setTimeList(timeList);

            String storeIds = orderRelatonInfo.getStoreIds();
            String[] storeIdArr = storeIds.split(",");
            for (String storeId : storeIdArr) {
                StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(Integer.parseInt(storeId));
                storeList.add(storeInfo);
            }
            orderRelatonInfo.setStoreList(storeList);
            order.setOrderRelaton(orderRelatonInfo);
        }

        return order;
    }


    @SuppressWarnings("unused")
    @Override
    public int orderConfirm(OrderInfo orderInfo) {

        int status = 1;//0失败 1成功

        try {

            Integer orderId = orderInfo.getOrderId();//订单号
            Integer type = orderInfo.getType();//请求类型 0拒绝 1接受

            OrderInfo order = new OrderInfo();
            order.setOrderId(orderId);
            order.setRemark(orderInfo.getRemark());

            if (type == 0) {
                order.setStatus(OrderStatusEnum.REFUSE.getCode());
            } else if (type == 1) {
                order.setStatus(OrderStatusEnum.APPOINMENT_SUCCESS.getCode());
            }

            int row = orderInfoMapper.updateByPrimaryKeySelective(order);

            if (orderInfo.getOrderRelaton() != null) {
                OrderRelatonInfo orderRelaton = orderInfo.getOrderRelaton();
                OrderRelatonInfo orderRelatonInfo = new OrderRelatonInfo();
                orderRelatonInfo.setOrderId(orderId);
                if (orderRelaton.getPositions() != null && !StringUtils.isEmpty(orderRelaton.getPositions().toString().trim())) {
                    orderRelatonInfo.setPositions(orderRelaton.getPositions());
                }
                if (orderRelaton.getStoreIds() != null && !StringUtils.isEmpty(orderRelaton.getStoreIds().toString().trim())) {
                    orderRelatonInfo.setStoreIds(orderRelaton.getStoreIds());
                }

                int row2 = orderRelatonInfoMapper.updateByOrderIdSelective(orderRelatonInfo);
            }
        } catch (Exception e) {
            status = 0;
            log.info("确认订单service出现异常:" + e.getMessage());
        }

        return status;
    }

    @Override
    public int updateImage(Integer orderId, Integer haircutImgId) {

        int status = 0;//0失败 1成功

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(orderId);
        orderInfo.setStartTime(new Date());

//		//第一张 和 最后一张图片//
        //第一张 图片
//		List<ImageInfo> imgList = imageInfoMapper.getImageByfaceId(faceId);
//		if(imgList != null && !imgList.isEmpty()){
//			ImageInfo beginImageInfo = imgList.get(0);
////			ImageInfo endImageInfo = imgList.get(imgList.size() - 1);
//			if(beginImageInfo != null){
//				orderInfo.setBeginImgId(beginImageInfo.getId());
//			}
////			if(endImageInfo != null){
////				orderInfo.setEndImgId(endImageInfo.getId());
////			}
//			
//		}

        ImageInfo beginImageInfo = imageInfoMapper.selectByPrimaryKey(haircutImgId);
        if (beginImageInfo != null) {
            orderInfo.setBeginImgId(beginImageInfo.getId());
        }

        int update = orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
        if (update > 0) {
            status = 1;
        }

        return status;
    }


    @Override
    public int finishHaircut(Integer orderId) {
        int status = 0;
        ImageInfo endImageInfo = null;
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        orderInfo.setEndTime(new Date());
        //开始理发图片id
        Integer beginImgId = orderInfo.getBeginImgId();

        if (beginImgId != null) {
            //开始理发图片
            ImageInfo beginImageInfo = imageInfoMapper.selectByPrimaryKey(beginImgId);
            if (beginImageInfo != null) {
                String faceId = beginImageInfo.getFaceId();//faceid
                String deviceNo = beginImageInfo.getDeviceNo();
                //根据faceid获取当天
                List<ImageInfo> imageList = imageInfoMapper.getImageByfaceIdAndDeviceNo(faceId, deviceNo);
                if (imageList != null && !imageList.isEmpty()) {
                    endImageInfo = imageList.get(imageList.size() - 1);
                }
            }
        }
        if (endImageInfo != null) {
            orderInfo.setEndImgId(endImageInfo.getId());
        }
        int update = orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
        if (update > 0) {
            status = 1;
        }
        return status;
    }


    @Override
    public int validateCode(int orderId, int code) {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        if (orderInfo != null) {
            Integer orderCode = orderInfo.getCode();
            if (code == orderCode) {
                log.info("预约码验证成功,orderId:" + orderId);
                return 1;
            }
        }
        log.info("预约码验证失败,orderId:" + orderId);
        return 0;
    }

    /**
     * 获取时间字符串
     *
     * @param position
     * @return
     */
    public String getsubscribeTime(Integer position) {
        String times = "";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar now = Calendar.getInstance();
        Date timeToday = now.getTime();

        now.add(Calendar.DATE, 1);
        Date timeTomorrow = now.getTime();

        now.add(Calendar.DATE, 1);
        Date timeAfterTomorrow = now.getTime();

        switch (position) {
            case 1:
                times = sdf.format(timeToday) + "  上午";
                break;
            case 2:
                times = sdf.format(timeToday) + "  下午";
                break;
            case 3:
                times = sdf.format(timeToday) + "  晚上";
                break;
            case 4:
                times = sdf.format(timeTomorrow) + "  上午";
                break;
            case 5:
                times = sdf.format(timeTomorrow) + "  下午";
                break;
            case 6:
                times = sdf.format(timeTomorrow) + "  晚上";
                break;
            case 7:
                times = sdf.format(timeAfterTomorrow) + "  上午";
                break;
            case 8:
                times = sdf.format(timeAfterTomorrow) + "  下午";
                break;
            case 9:
                times = sdf.format(timeAfterTomorrow) + "  晚上";
                break;

        }
        return times;
    }

    private List<OrderInfo> setDate(OrderInfo orderInfo, List<OrderInfo> orders) {
        if (orders != null && !orders.isEmpty()) {
            for (OrderInfo order : orders) {
                List<String> timeList = new ArrayList<String>();
                List<StoreInfo> storeList = new ArrayList<StoreInfo>();
                Integer orderId = order.getOrderId();
                if (order.getStartTime() != null && order.getEndTime() != null) {
                    order.setUseTime(getInstoreTime(order.getStartTime(), order.getEndTime()));
                }
                if (orderInfo.getBarberId() != null) {
                    //set 开始结束头像
                    Integer beginImgId = order.getBeginImgId();
                    Integer endImgId = order.getEndImgId();
                    ImageInfo beginImageInfo = imageInfoMapper.selectByPrimaryKey(beginImgId);
                    ImageInfo endImageInfo = imageInfoMapper.selectByPrimaryKey(endImgId);
                    order.setBeginImg(beginImageInfo);
                    order.setEndImg(endImageInfo);
                }

                //set 顾客
                CustomerInfo customerInfo = customerInfoMapper.getByOrderId(orderId);
                if (customerInfo != null) {
                    order.setCustomer(customerInfo);
                }

                //set 理发师
                BarberInfo barberInfo = barberInfoMapper.getByOrderId(orderId);
                if (barberInfo != null) {
                    String headUrl = barberInfoMapper.getBarberHeamUrlById(barberInfo.getBarberId());
                    barberInfo.setHeadImageUrl(headUrl);
                    order.setBarber(barberInfo);
                }

                //set orderrelation
                OrderRelatonInfo orderRelatonInfo = orderRelatonInfoMapper.selectByOrderId(orderId);
                if (orderRelatonInfo != null) {
                	if(orderRelatonInfo.getPositions() != null){
                		String positions = orderRelatonInfo.getPositions();
                		String[] positionArr = positions.split(",");
                		for (String position : positionArr) {
                			String subscribeTime = getsubscribeTime(Integer.parseInt(position)) + "";
                			timeList.add(subscribeTime);
                		}
                		orderRelatonInfo.setTimeList(timeList);
                	}

                	if(orderRelatonInfo.getStoreIds() != null){
                		String storeIds = orderRelatonInfo.getStoreIds();
                		String[] storeIdArr = storeIds.split(",");
                		for (String storeId : storeIdArr) {
                			StoreInfo storeInfo = storeInfoMapper.selectByPrimaryKey(Integer.parseInt(storeId));
                			storeList.add(storeInfo);
                		}
                		orderRelatonInfo.setStoreList(storeList);
                	}
                    order.setOrderRelaton(orderRelatonInfo);
                }

            }

        }
        return orders;
    }

    /**
     * 获取时间差(分钟)
     *
     * @param startTime
     * @param endTime
     * @return
     */
    private int getInstoreTime(Date startTime, Date endTime) {
        long between = endTime.getTime() - startTime.getTime();
        Long min = (between / (60 * 1000));
        return min.intValue();
    }

}
