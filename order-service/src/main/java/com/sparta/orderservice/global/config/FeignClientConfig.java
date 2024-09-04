package com.sparta.orderservice.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.sparta.orderservice.global.feign")
@Configuration
public class FeignClientConfig {

}
