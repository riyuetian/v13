package com.qf.v13ssoweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo  //开启dubbo
public class V13SsoWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(V13SsoWebApplication.class, args);
	}

}
