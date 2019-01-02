package com.adonai.ratelimiter;

import com.adonai.ratelimiter.filter.RateLimitZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class RateLimiterApp {

    public static void main(String [] args){
        SpringApplication.run(RateLimiterApp.class,args);
    }

    //配置filter
    @Bean
    public RateLimitZuulFilter accessFilter(){
        return new RateLimitZuulFilter();
    }

}
