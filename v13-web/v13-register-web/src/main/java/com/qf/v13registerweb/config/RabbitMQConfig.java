package com.qf.v13registerweb.config;

import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2615:14
 * @description: 发送邮件的队列信息   声明一个交换机(注册系统用)
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 声明交换机
     */
    @Bean
    public TopicExchange initExchange(){
        return new TopicExchange(RabbitMQConstant.REGISTER_EXCHANGE);
    }


}
