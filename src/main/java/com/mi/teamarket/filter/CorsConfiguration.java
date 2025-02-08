package com.mi.teamarket.filter;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许所有来源
                .allowedOriginPatterns("*")
                // 允许所有方法（GET、POST等）
                .allowedMethods("*")
                // 允许所有请求头
                .allowedHeaders("*")
                // 允许跨域携带凭证（设置为*时需删除此项或设为false）
                .allowCredentials(false)
                // 预检请求缓存时间（秒）
                .maxAge(3600);
    }
}