package com.hairstyle.weshow.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.hairstyle.weshow.common.Constant;
import com.hairstyle.weshow.dao.BarberInfoMapper;
import com.hairstyle.weshow.dao.CustomerInfoMapper;
import com.hairstyle.weshow.dao.FaceInfoMapper;
import com.hairstyle.weshow.dao.ImageInfoMapper;
import com.hairstyle.weshow.dao.OrderInfoMapper;
import com.hairstyle.weshow.dao.StoreBarberRelationInfoMapper;
import com.hairstyle.weshow.dao.StoreInfoMapper;
import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.CustomerInfo;
import com.hairstyle.weshow.domain.FaceInfo;
import com.hairstyle.weshow.domain.ImageInfo;
import com.hairstyle.weshow.domain.OrderInfo;
import com.hairstyle.weshow.domain.StoreAddressInfo;
import com.hairstyle.weshow.domain.StoreBarberRelationInfo;
import com.hairstyle.weshow.domain.StoreInfo;
import com.hairstyle.weshow.exception.ImgException;
import com.hairstyle.weshow.service.StoreService;
import com.hairstyle.weshow.utils.AliyunOSSClientUtil;
import com.hairstyle.weshow.utils.DistanceUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class StoreServiceImpl implements StoreService {

	@Autowired
	StoreInfoMapper storeInfoMapper;
	@Autowired
	ImageInfoMapper imageInfoMapper;
	@Autowired
	BarberInfoMapper barberInfoMapper;
	@Autowired
	CustomerInfoMapper customerInfoMapper;
	@Autowired
	FaceInfoMapper faceInfoMapper;
	@Autowired
	OrderInfoMapper orderInfoMapper;
	@Autowired
	StoreBarberRelationInfoMapper storeBarberRelationInfoMapper;

	@Override
	public List<StoreInfo> list(StoreInfo storeInfo) {
		
		List<StoreInfo> list = storeInfoMapper.getListOrder(storeInfo);
		
		for (StoreInfo store : list) {
			Integer storeId = store.getStoreId();
			
			List<ImageInfo> imageInfoList= imageInfoMapper.getByStoreId(storeId);//店铺图片
			if(imageInfoList != null && !imageInfoList.isEmpty()){
				store.setHeadImage(imageInfoList.get(0).getUrl());
			}
		
		}
		return list;
	}
	
//	@Override
//	public List<StoreInfo> list(StoreInfo storeInfo) {
//		
//		List<StoreInfo> list = storeInfoMapper.listNearStore(storeInfo);
//		
//		BigDecimal longitude = new BigDecimal(0);
//		BigDecimal latitude = new BigDecimal(0);
//		if (storeInfo.getLongitude() != null && storeInfo.getLatitude() != null) {
//			longitude = storeInfo.getLongitude();
//			latitude = storeInfo.getLatitude();
//		}
//		log.info("==============longitude="+longitude+",latitude"+latitude);
//		
//		for (StoreInfo store : list) {
//			Integer storeId = store.getStoreId();
//			// 头像
//			ImageInfo headImageInfo = imageInfoMapper.selectByPrimaryKey(store.getHeadId());
//			if(headImageInfo != null){
//				store.setHeadImage(headImageInfo.getUrl());
//			}
//			// 店铺图片
//			store.setCustomerNum(0);
//			double distance = getDistance(store,longitude,latitude);
//			store.setDistance(distance);
//			
//			//历史订单数
//			Integer historyOrderNum = storeInfoMapper.getOrderNum(storeId);
//			if(historyOrderNum != null){
//				store.setHistoryOrderNum(historyOrderNum);
//			}
//			
//		}
//		
//		Collections.sort(list, new Comparator<StoreInfo>() {
//			@Override
//			public int compare(StoreInfo o1, StoreInfo o2) {
//				if(storeInfo.getOrderType() == 1){
//					log.info("=======按照距离升序排序");
//					//按照距离升序
//					if(o1.getDistance() > o2.getDistance()){
//						return 1;
//					}
//					if(o1.getDistance() == o2.getDistance()){
//						return 0;
//					}
//					return -1;
//				}else if(storeInfo.getOrderType() == 2){
//					log.info("=======按照人气降序排序");
//					//按照人气降序(订单数)
//					if(o1.getHistoryOrderNum() < o2.getHistoryOrderNum()){
//						return 1;
//					}
//					if(o1.getHistoryOrderNum() == o2.getHistoryOrderNum()){
//						return 0;
//					}
//					return -1;
//				}else if(storeInfo.getOrderType() == 3){
//					log.info("=======按照人气降序排序");
//					//按照面积降序
//					if(o1.getAcreage() < o2.getAcreage()){
//						return 1;
//					}
//					if(o1.getAcreage() == o2.getAcreage()){
//						return 0;
//					}
//					return -1;
//				}
//				return 0;
//			}
//		});
//		return list;
//	}

	@Override
	public StoreInfo detail(StoreInfo storeInfo) {
		
		//店铺id
		Integer storeId = storeInfo.getStoreId();
		StoreInfo store = storeInfoMapper.selectByPrimaryKey(storeId);
		List<ImageInfo> imageInfoList= imageInfoMapper.getByStoreId(storeId);//店铺图片
		List<BarberInfo> barberInfoList = barberInfoMapper.getByStoreId(storeId);//店铺理发师
		for (BarberInfo barber : barberInfoList) {
			Integer customerId = barber.getUserId();
			CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
			if(customerInfo != null){
				barber.setHeadImageUrl(customerInfo.getHeadurl());
			}
		}
		//获取当前在店顾客信息
		List<FaceInfo> faceList = getCustomerList(storeId);
		
		store.setImageList(imageInfoList);
		store.setBarberList(barberInfoList);
		store.setFaceList(faceList);
		store.setBarberNum(barberInfoList.size());
		store.setFaceNum(faceList.size());
		
		return store;
	}
	
	
	@Override
	public List<StoreInfo> getServicingStore(BarberInfo barberInfo) {
		List<StoreInfo> list = storeInfoMapper.getServicingStore(barberInfo);
		for (StoreInfo storeInfo : list) {
//			Integer storeId = storeInfo.getStoreId();
//			List<ImageInfo> imageInfoList= imageInfoMapper.getByStoreId(storeId);//店铺图片
//			if(imageInfoList != null && !imageInfoList.isEmpty()){
//				storeInfo.setHeadImage(imageInfoList.get(0).getUrl());
//			}
			ImageInfo imageInfo= imageInfoMapper.selectByPrimaryKey(storeInfo.getHeadId());//店铺图片
			if(imageInfo != null){
				storeInfo.setHeadImage(imageInfo.getUrl());
			}
		}
		return list;
	}
	

	@Override
	public List<StoreInfo> getMyStoreList(CustomerInfo customerInfo) {
		List<StoreInfo> list = storeInfoMapper.getMyStoreList(customerInfo);
		for (StoreInfo storeInfo : list) {
			Integer storeId = storeInfo.getStoreId();
//			List<ImageInfo> imageInfoList= imageInfoMapper.getByStoreId(storeId);//店铺图片
//			if(imageInfoList != null && !imageInfoList.isEmpty()){
//				storeInfo.setHeadImage(imageInfoList.get(0).getUrl());
//			}
			ImageInfo imageInfo= imageInfoMapper.selectByPrimaryKey(storeInfo.getHeadId());//店铺图片
			if(imageInfo != null){
				storeInfo.setHeadImage(imageInfo.getUrl());
			}
			List<FaceInfo> faceList = getCustomerList(storeId);
			storeInfo.setFaceNum(faceList.size());//顾客数
		}
		return list;
	}
	
	@Override
	public int addStore(StoreInfo storeInfo, MultipartFile storeImgFile) throws ImgException {
		//上传阿里云
		AliyunOSSClientUtil ossClient=new AliyunOSSClientUtil();
		String imgUrl = ossClient.uploadImg2Oss(storeImgFile);
		
		ImageInfo imageInfo = new ImageInfo();
		imageInfo.setUrl(imgUrl);
		imageInfo.setType(Constant.STORE_IMG_TYPE);
		imageInfo.setCreateTime(new Date());
		imageInfo.setDeleted(1);
		int insert = imageInfoMapper.insert(imageInfo);
		
		if(insert > 0){
			storeInfo.setHeadId(imageInfo.getId());
			storeInfo.setHeadImage(imgUrl);
		}
		int insertstore = storeInfoMapper.insertSelective(storeInfo);
		if(insertstore > 0){
			return 1;
		}
		return 0;
	}
	
	@Override
	public int shareStore(StoreInfo storeInfo) {
		return storeInfoMapper.updateByPrimaryKeySelective(storeInfo);
	}
	
	@Override
	public StoreInfo getShareStoreDetail(StoreInfo storeInfo) {
		
		Integer storeId = storeInfo.getStoreId();
		StoreInfo store = storeInfoMapper.selectByPrimaryKey(storeId);
		
		List<ImageInfo> imageInfoList= imageInfoMapper.getByStoreId(storeId);//店铺图片
		if(imageInfoList != null && !imageInfoList.isEmpty()){
			store.setHeadImage(imageInfoList.get(0).getUrl());
		}
		
		//收益
		Integer income = storeInfoMapper.getSumIncome(storeId);
		if(income != null){
			store.setIncome(income);
		}
		
		//历史订单数
		Integer historyOrderNum = storeInfoMapper.getOrderNum(storeId);
		if(historyOrderNum != null){
			store.setHistoryOrderNum(historyOrderNum);
		}
		
		//历史理发师数
		List<BarberInfo> barberList = barberInfoMapper.getHistoryBarberList(storeId);
		if(barberList != null){
			store.setHistoryBarberNum(barberList.size());
		}
		
		//历史顾客数
		List<CustomerInfo> customerList = customerInfoMapper.getHistoryCustomerList(storeId);
		if(customerList != null){
			store.setHistoryCustomerNum(customerList.size());
		}
		
		return store;
	}
	
	@Override
	public List<BarberInfo> getHistoryBarber(Integer storeId) {
		return barberInfoMapper.getHistoryBarberList(storeId);
	}

	@Override
	public List<CustomerInfo> getHistoryCustomer(Integer storeId) {
		return customerInfoMapper.getHistoryCustomerList(storeId);
	}

	@Override
	public List<OrderInfo> getHistoreOrder(Integer storeId) {
		return orderInfoMapper.getHistoryOrderList(storeId);
	}

	@Override
	public int bindBarberAndStore(Integer storeId, Integer barberId) {
		StoreBarberRelationInfo storeBarberRelationInfo = new StoreBarberRelationInfo();
		storeBarberRelationInfo.setBarberId(barberId);
		storeBarberRelationInfo.setStoreId(storeId);
		storeBarberRelationInfo.setCreatedTime(new Date());
		storeBarberRelationInfo.setDeleted(1);
		StoreBarberRelationInfo  storeBarberRelation = storeBarberRelationInfoMapper.getByStoreIdAndBarberId(storeBarberRelationInfo);
		if(storeBarberRelation == null){
			int insert = storeBarberRelationInfoMapper.insert(storeBarberRelationInfo);
			if(insert > 0){
				log.info("理发师添加共享店铺成功,理发师:" + barberId + ",店铺:" + storeId);
				return 1;
			}
			log.info("理发师添加共享店铺失败,理发师:" + barberId + ",店铺:" + storeId);
		}else{
			storeBarberRelationInfo.setId(storeBarberRelation.getId());
			int update = storeBarberRelationInfoMapper.updateByPrimaryKeySelective(storeBarberRelationInfo);
			if(update > 0){
				log.info("理发师添加共享店铺成功,理发师:" + barberId + ",店铺:" + storeId);
				return 1;
			}
			log.info("理发师添加共享店铺失败,理发师:" + barberId + ",店铺:" + storeId);
		}
		return 0;
	}
	
	@Override
	public int removeStore(Integer storeId, Integer barberId) {
		StoreBarberRelationInfo storeBarberRelationInfo = new StoreBarberRelationInfo();
		storeBarberRelationInfo.setBarberId(barberId);
		storeBarberRelationInfo.setStoreId(storeId);
		int delete = storeBarberRelationInfoMapper.deleteByStoreIdAndBarberId(storeBarberRelationInfo);
		if(delete > 0){
			log.info("理发师移除共享店铺成功,理发师:" + barberId + ",店铺:" + storeId);
			return 1;
		}
		log.info("理发师移除共享店铺失败,理发师:" + barberId + ",店铺:" + storeId);
		return 0;
	}
	
	
	/**
	 * 获取进店时间(分钟)
	 * @param comingTime
	 * @return
	 */
	private int getInstoreTime(Date comingTime) {
		Date date = new Date();
		long between = date.getTime() - comingTime.getTime();
		Long min = (between / (60 * 1000));
		return min.intValue();
	}

	/**
	 * 获取当前在店顾客信息
	 * @param storeId
	 * @return
	 */
	protected List<FaceInfo> getCustomerList(Integer storeId) {
		List<FaceInfo> faces = faceInfoMapper.getCustomerByStoreId(storeId);// 店铺顾客
		List<FaceInfo> faceList = new ArrayList<FaceInfo>();// 店铺顾客

		for (FaceInfo faceInfo : faces) {
			int count = barberInfoMapper.getBarberCountByFaceId(faceInfo.getFaceId());
			if (count != 0) {
				continue;
			}

			Date comingTime = faceInfo.getComingTime();// 进店时间
			faceInfo.setInStoreTime(getInstoreTime(comingTime));// 在店时间

			Integer hairStatus = faceInfo.getHairStatus();// 理发状态
			// 设置头像
			switch (hairStatus) {
			case 1:
				ImageInfo comingImg = imageInfoMapper.selectByPrimaryKey(faceInfo.getComingImgId());
				faceInfo.setHeadImg(comingImg);
				break;
			case 2:
				ImageInfo shampooImg = imageInfoMapper.selectByPrimaryKey(faceInfo.getShampooImgId());
				faceInfo.setHeadImg(shampooImg);
				break;
			case 3:
				ImageInfo haircutImg = imageInfoMapper.selectByPrimaryKey(faceInfo.getHaircutImgId());
				faceInfo.setHeadImg(haircutImg);
				break;
			case 4:
				ImageInfo getoutImg = imageInfoMapper.selectByPrimaryKey(faceInfo.getGetoutImgId());
				faceInfo.setHeadImg(getoutImg);
				break;
			}
			faceList.add(faceInfo);
		}
		return faceList;
	}


	/**
	 * 根据经纬度获取距离
	 * @param store
	 * @param longitude
	 * @param latitude
	 * @return
	 */
	private double getDistance(StoreInfo store, BigDecimal longitude, BigDecimal latitude) {
		double distance = 0.00;
		StoreAddressInfo storeAddress = store.getStoreAddress();
		BigDecimal storeLongitude = storeAddress.getLongitude();
		BigDecimal storeLatitude = storeAddress.getLatitude();
		
		if (longitude != null && latitude != null 
				&& storeLongitude != null && storeLatitude != null
				&& longitude.compareTo(BigDecimal.ZERO) != 0 
				&& latitude.compareTo(BigDecimal.ZERO) != 0
				&& storeLongitude.compareTo(BigDecimal.ZERO) != 0
				&& storeLatitude.compareTo(BigDecimal.ZERO) != 0) {
		distance = DistanceUtils.getDistance(longitude,latitude,storeLongitude,storeLatitude);
		}
		return distance;
	}
	
	public static void main(String[] args) {
		
		BigDecimal bigDecimal = new BigDecimal("1.22");
		double a = 13.3333333;
		System.out.println(a);
		System.out.println(bigDecimal);
	}
}
