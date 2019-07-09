package com.qf.v13centerweb;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(FdfsClientConfig.class)//整合fastDFS  2启动类导入配置
@SpringBootApplication
@EnableDubbo   //调用服务4  注解启动Dubbo
public class V13CenterWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(V13CenterWebApplication.class, args);
	}

}
