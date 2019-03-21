package com.hairstyle.weshow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hairstyle.weshow.service.TestService;

@RestController
public class TestController {

	@Autowired 
	TestService testService;
	
	@GetMapping("/hello")
	public String test() {
		return testService.test();
	}

}
