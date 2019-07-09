package com.qf.v13productservice.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 *分页1-4  分页类 这里相当于构建一个bean
 * 采用注解方式来实现分页配置  设置属性参数
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/140:45
 * @description: TODO
 */
@Configuration
public class CommonConfig {

    @Bean
    public PageHelper getPageHelper(){
        //创建一个PageHelper 对象
        PageHelper pageHelper = new PageHelper();

        //创建Properties 对象  设置相关参数
        Properties properties =new Properties();
        properties.setProperty("dialect","mysql");
        properties.setProperty("reasonable","true");

        //注入properties  得到属性
        pageHelper.setProperties(properties);

        //返回pagehelper
        return  pageHelper;
    }
}
