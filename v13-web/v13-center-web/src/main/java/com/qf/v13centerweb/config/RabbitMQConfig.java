package com.qf.v13centerweb.config;

import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 整合rabbuit-3  声明创建列队 或交换机 及两者关联
 *      整合rabbuit-1  依赖   这里依赖的是rabbit的上一级
 *      整合rabbuit-2  配置
 *      整合rabbuit-4  controller层  发布消息
 *
 *
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2217:30
 */

@Configuration
public class RabbitMQConfig {

    //初始化交换机
    @Bean
    public TopicExchange initExchange(){
        return new TopicExchange(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE);
    }
}
