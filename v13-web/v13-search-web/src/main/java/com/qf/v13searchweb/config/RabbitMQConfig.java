package com.qf.v13searchweb.config;

import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 整合 rabbitmp-5  创建申明队列  绑定两者关系
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2411:19
 */
@Configuration
public class RabbitMQConfig {

    //声明队列
    @Bean
    public Queue initQueue(){
        return new Queue(RabbitMQConstant.CENTER_SEARCH_QUEUE,true,false,false);
    }

    //声明交换机.如果交换机已经存在，则不需要做此操作
    @Bean
    public TopicExchange initExchange(){
        return new TopicExchange(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE);
    }

    //绑定交换机
    @Bean
    public Binding initBinding(Queue initQueue, TopicExchange initExchange){
        return BindingBuilder.bind(initQueue).to(initExchange).with("product.add");
    }

}
