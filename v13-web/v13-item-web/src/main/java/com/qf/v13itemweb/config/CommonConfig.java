package com.qf.v13itemweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 用来  多线程生成静态页面的  中的所需的 单线程
 *
 *  一 @Configuration  用于定义配置类，可替换xml配置文件，
 *       被注解的类内部包含有一个或多个被@Bean注解的方法，
 *       这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，
 *       并用于构建bean定义，初始化Spring容器。
 *    标注在类上，相当于把该类作为spring的xml配置文件中的<beans>，作用为：配置spring容器(应用上下文)
 *
 * 二 @Configuration 注解的配置类有如下要求：
         1 @Configuration 不可以是final类型；
         2 @Configuration 不可以是匿名类；
         3 嵌套的configuration必须是静态类。

    a、 @Configuation等价于<Beans></Beans>
    b、 @Bean等价于<Bean></Bean>
    c、 @ComponentScan等价于<context:component-scan base-package=”com.dxz.demo”/>
 *
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2017:51
 */
@Configuration
public class CommonConfig {

    /**
     * 用来产生一个ThreadPoolExecutor线程池
     *   初始化
     *
     *   ThreadPoolExecutor最核心的构造方法
     *      corePoolSize	核心线程池大小
            maximumPoolSize	最大线程池大小
            keepAliveTime	线程池中超过corePoolSize数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间
            TimeUnit	keepAliveTime时间单位   TimeUnit.DAYS//天  TimeUnit.HOURS //小时  TimeUnit.MINUTES //分钟  TimeUnit.SECONDS//秒  TimeUnit.MILLISECONDS  //毫秒
            workQueue	阻塞任务队列
            threadFactory	新建线程工厂
            RejectedExecutionHandler	当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理
     *
     *   1.当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程。
         2.当线程池达到corePoolSize时，新提交任务将被放入workQueue中，等待线程池中任务调度执行
         3.当workQueue已满，且maximumPoolSize>corePoolSize时，新提交任务会创建新线程执行任务
         4.当提交任务数超过maximumPoolSize时，新提交任务由RejectedExecutionHandler处理
         5.当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程
         6.当设置allowCoreThreadTimeOut(true)时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭
     *
     */
    @Bean
    public ThreadPoolExecutor initThreadPoolExecutor(){
        //查看当前的硬件的核数
        int cpus = Runtime.getRuntime().availableProcessors();
        //自定义线程池参数,   初始线程数,最大线程 ，10S等待，100等待队列长度
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                cpus,cpus*2,10, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<>(10));

        return pool;
    }



}
