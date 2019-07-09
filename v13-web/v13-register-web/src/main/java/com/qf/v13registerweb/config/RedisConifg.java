package com.qf.v13registerweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2517:36
 * @description: TODO
 *
 *提供公共类 提供两个方法
 *      1、统一的序列化方式方法
 *      2、不需要的序列化方法
 *
 */
@Configuration
public class RedisConifg {

    /**
     * 改变序列化方式  需要注入一个工厂
     */
    @Bean(name ="redisTemplate" )
    public RedisTemplate<String ,Object> getRedisTemplate(RedisConnectionFactory connectionFactory){
        //创建一个对象
        RedisTemplate<String , Object> redisTemplate = new RedisTemplate<>();
        //注入工厂 本身需要工厂来进行一些设置
        redisTemplate.setConnectionFactory(connectionFactory);
        //改变序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

}
