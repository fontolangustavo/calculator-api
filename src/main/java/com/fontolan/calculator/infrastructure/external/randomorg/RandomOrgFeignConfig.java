package com.fontolan.calculator.infrastructure.external.randomorg;

import feign.Logger;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

public class RandomOrgFeignConfig {

    @Value("${random-org.timeouts.connect-ms:3000}")
    private int connectMs;
    @Value("${random-org.timeouts.read-ms:5000}")
    private int readMs;
    @Value("${random-org.timeouts.write-ms:5000}")
    private int writeMs;
    @Value("${random-org.feign-logger-level:basic}")
    private String loggerLevel;

    @Bean
    public OkHttpClient feignOkHttpClient() {
        return new okhttp3.OkHttpClient.Builder()
                        .connectTimeout(connectMs, TimeUnit.MILLISECONDS)
                        .readTimeout(readMs, TimeUnit.MILLISECONDS)
                        .writeTimeout(writeMs, TimeUnit.MILLISECONDS)
                        .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                        .build();
    }

    @Bean
    public Logger.Level feignLoggerLevel() {
        return switch (loggerLevel.toLowerCase()) {
            case "none" -> Logger.Level.NONE;
            case "full" -> Logger.Level.FULL;
            case "headers" -> Logger.Level.HEADERS;
            default -> Logger.Level.BASIC;
        };
    }

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(
                100,
                1000,
                3
        );
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new RandomOrgErrorDecoder();
    }
}
