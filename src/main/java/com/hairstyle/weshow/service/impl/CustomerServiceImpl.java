package com.hairstyle.weshow.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hairstyle.weshow.dao.BarberInfoMapper;
import com.hairstyle.weshow.dao.CustomerAccountInfoMapper;
import com.hairstyle.weshow.dao.CustomerFaceInfoMapper;
import com.hairstyle.weshow.dao.CustomerIncomeInfoMapper;
import com.hairstyle.weshow.dao.CustomerInfoMapper;
import com.hairstyle.weshow.dao.OrderInfoMapper;
import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.CustomerAccountInfo;
import com.hairstyle.weshow.domain.CustomerFaceInfo;
import com.hairstyle.weshow.domain.CustomerIncomeInfo;
import com.hairstyle.weshow.domain.CustomerInfo;
import com.hairstyle.weshow.domain.DecryptDataInfo;
import com.hairstyle.weshow.domain.OrderInfo;
import com.hairstyle.weshow.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerInfoMapper customerInfoMapper;
	@Autowired
	CustomerFaceInfoMapper customerFaceInfoMapper;
	@Autowired
	BarberInfoMapper barberInfoMapper;
	@Autowired
	OrderInfoMapper orderInfoMapper;
	@Autowired
	CustomerIncomeInfoMapper customerIncomeInfoMapper;
	@Autowired
	CustomerAccountInfoMapper customerAccountInfoMapper;
	
	@Override
	public CustomerInfo getByCustomerId(Integer customerId) {
		CustomerInfo CustomerInfo = customerInfoMapper.selectByPrimaryKey(customerId);
		CustomerFaceInfo customerFaceInfo = customerFaceInfoMapper.selectByCustomerId(customerId);
		if(customerFaceInfo != null){
			CustomerInfo.setCustomerFaceInfo(customerFaceInfo);
		}
		return CustomerInfo;
	}

	@Override
	public List<CustomerInfo> getMyCustomerList(Integer barberId) {
		Integer count = customerInfoMapper.getMyCustomerOrderCount(barberId);
		return customerInfoMapper.getMyCustomerList(barberId, count);
	}

	@Override
	public int login(DecryptDataInfo decryptDataInfo) {
		
		int status = 0;
		
		String openId = decryptDataInfo.getOpenId();
		CustomerInfo customer = customerInfoMapper.getbyOpenId(openId);

		CustomerInfo customerInfo = new CustomerInfo();
		
		customerInfo.setOpenId(openId);
		customerInfo.setRegisterTime(new Date());
		
		if(decryptDataInfo.getAvatarUrl() != null){
			customerInfo.setHeadurl(decryptDataInfo.getAvatarUrl());
		}
		if(decryptDataInfo.getCountry() != null){
			customerInfo.setCountry(decryptDataInfo.getCountry());
		}
		if(decryptDataInfo.getProvince() != null){
			customerInfo.setProvince(decryptDataInfo.getProvince());
		}
		if(decryptDataInfo.getCity() != null){
			customerInfo.setCity(decryptDataInfo.getCity());
		}
		if(decryptDataInfo.getNickName() != null){
			customerInfo.setName(decryptDataInfo.getNickName());
		}
		if(decryptDataInfo.getGender() != null){
			customerInfo.setGender(decryptDataInfo.getGender());
		}
		
		
		if(customer == null){
			customerInfo.setStatus(1);
			int insert = customerInfoMapper.insert(customerInfo);
			log.info("顾客注册登录=======注册,条数:" + insert);
			if(insert > 0){
				status = 1;
			}
//			customer = customerInfoMapper.selectByPrimaryKey(customerInfo.getCustomerId());
		}else{
			int update = customerInfoMapper.updateByOpenId(customerInfo);
			log.info("顾客注册登录=======登录,条数:" + update);
			if(update > 0){
				status = 1;
			}
		}
		
		return status;
	}

	@Override
	public List<BarberInfo> getMyBarberList(Integer customerId) {
		List<BarberInfo> barberList = barberInfoMapper.getMyBarberList(customerId);
		for (BarberInfo barberInfo : barberList) {
			Integer userId = barberInfo.getUserId();
			Integer barberId = barberInfo.getBarberId();

			CustomerInfo customerInfo = customerInfoMapper.selectByPrimaryKey(userId);
			OrderInfo order = orderInfoMapper.getOrderByBarberIdAndCustomerId(customerId,barberId);
			barberInfo.setHeadImageUrl(customerInfo.getHeadurl());
			barberInfo.setHairCutTime(order.getCreatedTime());
			barberInfo.setHaircutAbilityValue(null);
			barberInfo.setShampooAbilityValue(null);
			barberInfo.setHairdyeAbilityValue(null);
			barberInfo.setPermAbilityValue(null);
			barberInfo.setServiceStatus(null);
			barberInfo.setScore(null);
			barberInfo.setBarberAddress(null);
			barberInfo.setIntroduction(null);
			barberInfo.setEmpiricalValue(null);
			barberInfo.setCosmetologyAbilityValue(null);
			barberInfo.setRegisterTime(null);
			barberInfo.setHeadId(null);
		}
		return barberList;
	}

	@Override
	public synchronized int income(CustomerIncomeInfo customerIncomeInfo) {
		int status = 0;//0 失败 1成功 2余额不足
		Date date = new Date();
		Integer amount = customerIncomeInfo.getAmount();
		Integer customerId = customerIncomeInfo.getCustomerId();
		Integer type = customerIncomeInfo.getType();
		
		CustomerAccountInfo customerAccountInfo = new CustomerAccountInfo();
		CustomerAccountInfo customerAccount = customerAccountInfoMapper.getByCustomerId(customerId);
		if(customerAccount == null){
			if(type == 1){
				customerAccountInfo.setAmount(amount);
			}else if(type == 2){
				customerAccountInfo.setAmount(0);
			}
			customerAccountInfo.setCustomerId(customerId);
			customerAccountInfo.setCreatedTime(date);
			int insert2 = customerAccountInfoMapper.insert(customerAccountInfo);
			log.info("新建账户: customerId:" + customerId +"==条数:" + insert2);
			
			if(insert2 > 0 ){
				status = 1;
			}
		}else{
			
			Integer amountNow = customerAccount.getAmount();
			amountNow = amountNow +  amount;
			if(amountNow < 0){
				return 2;
			}
			customerAccountInfo.setAmount(amountNow);
			customerAccountInfo.setUpdatedTime(date);
			int update = customerAccountInfoMapper.updateByCustomerIdSelective(customerAccountInfo);
			log.info("更新账户: customerId:" + customerId +"==条数:" + update);

			if(update > 0 ){
				status = 1;
			}
		}
		customerIncomeInfo.setCreatedTime(date);
		int insert = customerIncomeInfoMapper.insert(customerIncomeInfo);
		if(insert > 0){
			log.info("新增消费记录成功: customerId:" + customerId +"==条数:" + insert);
		}else{
			log.info("新增消费记录失败: customerId:" + customerId );
			status = 0;
		}
		
		return status;
	}

	@Override
	public CustomerAccountInfo getIncome(CustomerAccountInfo customerAccountInfo) {
		
		Integer customerId = customerAccountInfo.getCustomerId();
		
		CustomerAccountInfo customerAccount = customerAccountInfoMapper.getByCustomerId(customerId);
		List<CustomerIncomeInfo> list = customerIncomeInfoMapper.getByCustomerId(customerAccountInfo);
		customerAccount.setCustomerIncomeList(list);
		
		return customerAccount;
	}

	@Override
	public synchronized int incomeOtherWay(CustomerIncomeInfo customerIncomeInfo) {
		int status = 0;//0 失败 1成功 2余额不足
		Date date = new Date();
		Integer customerId = customerIncomeInfo.getCustomerId();
		CustomerAccountInfo customerAccountInfo = new CustomerAccountInfo();
		CustomerAccountInfo customerAccount = customerAccountInfoMapper.getByCustomerId(customerId);
		
		if(customerAccount == null){
			customerAccountInfo.setCustomerId(customerId);
			customerAccountInfo.setAmount(0);
			customerAccountInfo.setCreatedTime(date);
			int insert = customerAccountInfoMapper.insert(customerAccountInfo);
			log.info("新建账户: customerId:" + customerId +"==条数:" + insert);
			if(insert > 0 ){
				status = 1;
			}
		}
		customerIncomeInfo.setCreatedTime(date);
		int insert = customerIncomeInfoMapper.insert(customerIncomeInfo);
		if(insert > 0){
			log.info("新增消费记录成功: customerId:" + customerId +"==条数:" + insert);
		}else{
			log.info("新增消费记录失败: customerId:" + customerId );
			status = 0;
		}
		return status;
	}

}
