package com.qf.tempspringbootredis.config;

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
     * @return
     */
    @Bean(name ="redisTemplate1" )
    public RedisTemplate<String ,Object> getRedisTemplate(RedisConnectionFactory connectionFactory){
        //创建一个对象
        RedisTemplate<String , Object> redisTemplate = new RedisTemplate<>();
        //注入工厂 本身需要工厂来进行一些设置
        redisTemplate.setConnectionFactory(connectionFactory);
        //改变序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        //返回改变完成的后的对象
        return redisTemplate;
    }

    /**
     * 不做改变
     */
    @Bean(name ="redisTemplate2" )
    public RedisTemplate<String ,Object> getRedisTemplate2(RedisConnectionFactory connectionFactory){
        //创建一个对象
        RedisTemplate<String , Object> redisTemplate = new RedisTemplate<>();
        //注入工厂 本身需要工厂来进行一些设置
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //返回改变完成的后的对象
        return redisTemplate;
    }


}
