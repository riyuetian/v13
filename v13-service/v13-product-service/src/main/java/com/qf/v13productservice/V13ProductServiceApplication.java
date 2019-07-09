package com.qf.v13productservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 启动类  发布启动
 */
@SpringBootApplication
@MapperScan("com.qf.v13.mapper")  //不加注解会找不到mapper错误
@EnableDubbo  //用来开启dubbo服务
public class V13ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(V13ProductServiceApplication.class, args);
	}

}
