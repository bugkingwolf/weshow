package com.hairstyle.weshow.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hairstyle.weshow.dao.ExtractInfoMapper;
import com.hairstyle.weshow.domain.ExtractInfo;
import com.hairstyle.weshow.service.ExtractService;

@Service
public class ExtractServiceImpl implements ExtractService {

	@Autowired
	ExtractInfoMapper extractInfoMapper;
	
	@Override
	public int apply(ExtractInfo extractInfo) {
		extractInfo.setCreatedTime(new Date());
		int insert = extractInfoMapper.insert(extractInfo);
		if(insert > 0){
			return 1;
		}
		return 0;
	}

}
