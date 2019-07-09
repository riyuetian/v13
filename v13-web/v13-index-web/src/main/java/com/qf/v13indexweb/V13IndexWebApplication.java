package com.qf.v13indexweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo  //启动dubbo
public class V13IndexWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(V13IndexWebApplication.class, args);
	}

}
