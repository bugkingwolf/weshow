package com.hairstyle.weshow.service;

import java.util.List;

import com.hairstyle.weshow.domain.BarberInfo;
import com.hairstyle.weshow.domain.CustomerAccountInfo;
import com.hairstyle.weshow.domain.CustomerIncomeInfo;
import com.hairstyle.weshow.domain.CustomerInfo;
import com.hairstyle.weshow.domain.DecryptDataInfo;

public interface CustomerService {

	CustomerInfo getByCustomerId(Integer customerId);

	List<CustomerInfo> getMyCustomerList(Integer barberId);

	int login(DecryptDataInfo decryptDataInfo);

	List<BarberInfo> getMyBarberList(Integer customerId);

	int income(CustomerIncomeInfo customerIncomeInfo);

	CustomerAccountInfo getIncome(CustomerAccountInfo customerAccountInfo);

	int incomeOtherWay(CustomerIncomeInfo customerIncomeInfo);

}
