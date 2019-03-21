package com.hairstyle.weshow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hairstyle.weshow.dao")
@SpringBootApplication(scanBasePackages = {"com.hairstyle.weshow"})
public class WeshowApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeshowApplication.class, args);
    }

}
