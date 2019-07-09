package com.qf.tempspringbootrabbitmq.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *  用来声明创建列队  交换机 以及绑定关系
 *
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2216:02
 */
@Configuration
public class RabbitMQConfig {

    //声明一个列队名
    @Bean
    public Queue initQueue(){
        return new Queue("springboot-simple-queue",false,false,false);
    }
}
