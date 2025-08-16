package com.fontolan.calculator.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.fontolan.calculator.infrastructure.external.randomorg")
public class FeignConfig { }
