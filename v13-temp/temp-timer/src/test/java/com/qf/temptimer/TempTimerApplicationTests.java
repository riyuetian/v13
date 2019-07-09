package com.qf.temptimer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TempTimerApplicationTests {

	@Test
	public void contextLoads() throws InterruptedException {

		System.out.println(new Date());

		//注意：测试类  让顺序执行 到}后运行停止
		//设置一个延时（睡眠） 等待运行一下
		Thread.sleep(5000);
	}

}
