package com.worsun.spring_demo.home;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.worsun.spring_demo.home.mapper")
public class HomeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeApplication.class, args);
    }
}