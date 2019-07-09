package com.qf.tempspringbootrabbitmq;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TempSpringbootRabbitmqApplicationTests {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Test
	public void contextLoads() {

		//发消息到列队
		//rabbitTemplate.convertAndSend("","simple_queue","hello!!");
		//发消息到交换机
		//rabbitTemplate.convertAndSend("fanout_exchange","","发给交换机的！！");

		//发送对象
		Book book = new Book(1,"fang",520);
		rabbitTemplate.convertAndSend("","springboot-simple-queue",book);
	}

}
