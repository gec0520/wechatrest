package com.haipai.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;


@SuppressWarnings("deprecation")
@SpringBootApplication
@EnableScheduling
@EnableCaching
public class WeChatApplication extends SpringBootServletInitializer{
	
	@Override
    protected SpringApplicationBuilder configure(
            SpringApplicationBuilder application) {
        return application.sources(WeChatApplication.class);
    }
	
	public static void main(String[] args) {
        SpringApplication.run(WeChatApplication.class, args);
    }
}
