package com.zhanghui.poem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//Spring Boot应用程序程序入口
@MapperScan("com.zhanghui.poem.dao")//dao映射扫描
public class PoemApplication {

    public static void main(String[] args) {

        SpringApplication.run(PoemApplication.class, args);
    }

}
