package com.qf.temptimer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  //开启定时支持
public class TempTimerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TempTimerApplication.class, args);
	}

}
