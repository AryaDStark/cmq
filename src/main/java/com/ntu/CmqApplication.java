package com.ntu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.ntu.cmqq.mapper")
@SpringBootApplication
public class CmqApplication {

    public static void main(String[] args) {
        SpringApplication.run(CmqApplication.class, args);
    }

}
