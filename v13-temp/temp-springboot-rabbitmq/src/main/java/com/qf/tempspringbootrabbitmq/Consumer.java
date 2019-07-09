package com.qf.tempspringbootrabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费者类
 *
 *  注解的含义：
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2216:21
 */
@Component
@RabbitListener(queues = "springboot-simple-queue")  //引入队列
public class Consumer {


    @RabbitHandler
    public void process(Book book){
        System.out.println("-->"+book.getName()+book.getPrice());
    }
}
