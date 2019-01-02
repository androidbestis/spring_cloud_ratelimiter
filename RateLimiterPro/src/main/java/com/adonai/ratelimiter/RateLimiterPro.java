package com.adonai.ratelimiter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class RateLimiterPro {

    public static void main(String [] args){
        SpringApplication.run(RateLimiterPro.class,args);
    }
}
