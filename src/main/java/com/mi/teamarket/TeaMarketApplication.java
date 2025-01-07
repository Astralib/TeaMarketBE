package com.mi.teamarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mi.teamarket.mapper")
public class TeaMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeaMarketApplication.class, args);
    }

}
