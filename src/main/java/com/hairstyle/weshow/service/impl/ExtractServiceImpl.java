package com.hairstyle.weshow.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hairstyle.weshow.dao.CustomerAccountInfoMapper;
import com.hairstyle.weshow.dao.ExtractInfoMapper;
import com.hairstyle.weshow.domain.CustomerAccountInfo;
import com.hairstyle.weshow.domain.CustomerIncomeInfo;
import com.hairstyle.weshow.domain.ExtractInfo;
import com.hairstyle.weshow.service.CustomerService;
import com.hairstyle.weshow.service.ExtractService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ExtractServiceImpl implements ExtractService {

    @Autowired
    ExtractInfoMapper extractInfoMapper;
    @Autowired
    CustomerAccountInfoMapper customerAccountInfoMapper;
    @Autowired
    CustomerService customerService;
    
    @Override
    public synchronized int apply(ExtractInfo extractInfo) {
        extractInfo.setCreatedTime(new Date());
        extractInfo.setStatus(1);
        Integer amount = extractInfo.getAmount();//提现金额
        
        CustomerAccountInfo customerAccount = customerAccountInfoMapper.getByCustomerId(extractInfo.getCustomerId());
        if(customerAccount != null && customerAccount.getAmount() >= amount){
        	int insert = extractInfoMapper.insert(extractInfo);
        	if (insert > 0) {
        		
        		CustomerIncomeInfo customerIncomeInfo = new CustomerIncomeInfo();
        		customerIncomeInfo.setTitle("提现");
        		customerIncomeInfo.setType(2);
        		customerIncomeInfo.setPayWay(1);
        		customerIncomeInfo.setAmount(amount);
        		
        		int count = customerService.income(customerIncomeInfo);
        		if(count == 0){
        			log.info("新增收入失败====");
        		}
        		
        		customerAccount.setAmount(customerAccount.getAmount() +  ( -1 * amount ));
        		int update = customerAccountInfoMapper.updateByPrimaryKeySelective(customerAccount);
        		if(update == 0){
        			log.info("更新用户账户失败====");
        		}
        		return 1;
        	}
        }else{
        	log.info("用户余额不足====");
        }
        
        return 0;
    }

}
