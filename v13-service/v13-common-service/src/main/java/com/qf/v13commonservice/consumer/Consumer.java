package com.qf.v13commonservice.consumer;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IEmailService;
import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2618:17
 * @description:  邮件系统
 */
@Component
public class Consumer {

    //@Resource的作用相当于@Autowired，只不过@Autowired按byType自动注入，而@Resource默认按 byName自动注入。
    //@Reference注入的是分布式中的远程服务对象，@Resource和@Autowired注入的是本地spring容器中的对象。
    //@Reference  ???????????????
    @Autowired
    private IEmailService emailService;

    @RabbitListener(queues = RabbitMQConstant.EMAIL_QUEUE)
    @RabbitHandler
    public void process(Map<String,String> map){
        System.out.println(map.get("to"));
        System.out.println(map.get("subject"));
        System.out.println(map.get("text"));

        emailService.send(map.get("to"),map.get("subject"),map.get("text"));

    }


}
