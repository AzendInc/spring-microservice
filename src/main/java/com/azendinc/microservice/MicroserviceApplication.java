package com.azendinc.microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableAutoConfiguration
@Configuration
@EnableZuulProxy
@ComponentScan
@EnableResourceServer
public class MicroserviceApplication {
	public static void main(String[] args) {
        SpringApplication.run(MicroserviceApplication.class, args);
	}
}