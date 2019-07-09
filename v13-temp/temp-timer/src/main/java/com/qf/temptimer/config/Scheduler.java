package com.qf.temptimer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2712:15
 * @description: TODO  修改定时任务为多线程模式
 */
@Configuration
public class Scheduler implements SchedulingConfigurer{

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(getTaskExecutors());
    }

    @Bean
    public Executor getTaskExecutors(){
        //z自动的多线程 需要改手工模式
        return Executors.newScheduledThreadPool(100);
    }
}
