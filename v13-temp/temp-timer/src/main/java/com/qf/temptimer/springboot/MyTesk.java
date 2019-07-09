package com.qf.temptimer.springboot;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2711:33
 * @description:
 *
 * springboot的定时任务默认是单线程模式 改多线程需要修改配置
 * spring自带的定时器
 *      1、启动类  注解开启定时任务@EnableScheduling
 *      2、调用类 采用注解@Scheduled() 来实现  支持cron表达式
 *
 *      cron是一个通用性表达式。用于描述定时任务执行的规律
 */
@Component
public class MyTesk {

    //@Scheduled(fixedDelay = 1000)//常规的延时方式
    @Scheduled(cron ="0,2,4,6,8,10,11,12,13,20 * * * * ?")    //重点、：cron  表达式
    public void doSomething(){
        System.out.println(Thread.currentThread().getName()+"--->"+new Date());
    }

}
