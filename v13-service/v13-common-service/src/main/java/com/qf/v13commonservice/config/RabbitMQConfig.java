package com.qf.v13commonservice.config;

import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2617:34
 * @description: 邮件系统
 *      注解
 *      声明队列  绑定交换机  rabbitmq 相关依赖和配置信息
 *
 *  1、TemplateBinding的数据绑定是单向的，从数据源到目标。
    2、TemplateBinding不能对数据对象进行自动转换，数据源和目标的数据类型不同时候，需要自己写转换器。
    3、Binding的数据绑定方式是可以选择的，比TemplateBinding要灵活
    4、Binding会对数据源和目标的数据类型进行自动转换
    5、TemplateBinding是Binding的特殊情况。TemplateBinding等价于
            {Binding RelativeSource={RelativeSource TemplatedParent}}
 *
 */
@Configuration
public class RabbitMQConfig {

    //声明队列  名称在公共类中定义
    @Bean
    public Queue initQueue(){
        return new Queue(RabbitMQConstant.EMAIL_QUEUE,true,false,false);
    }
    //声明交换机  如果已经存在 。则不做此操作
    @Bean
    public TopicExchange initExchange(){
        return new TopicExchange(RabbitMQConstant.REGISTER_EXCHANGE);
    }

    //绑定交换机
    @Bean
    public Binding initBinding(Queue initQueue,TopicExchange initExchange){
        return BindingBuilder.bind(initQueue).to(initExchange).with("user.add");
    }

}
