package com.qf.jedis;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2420:43
 * @description: TODO
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:redis.xml")
public class SpringDataRedisTest {

    //声明redisTemplate对象
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *
     */
    @Test
    public void stringTest(){
        //redisTemplate默认对保存进去的信息都会做序列化操作
        //自己设置序列化 1、在xml中改变序列化
        //2、在程序中改变序列化
        redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());

        redisTemplate.opsForValue().set("target","通过用SpringDataRedis来操作aRedis");
        System.out.println(redisTemplate.opsForValue().get("target"));
    }

    /**
     * 自增设置
     */
    @Test
    public void incrTest(){
        //设置序列化  自增的时候默认的序列化方式是不支持的
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set("money","10000");
        //自增
        redisTemplate.opsForValue().increment("money",10000);
        Object money = redisTemplate.opsForValue().get("money");
        System.out.println(money);
    }

    @Test
    public void otherTest(){
        //hash
        redisTemplate.opsForHash().put("home:1","mielifang","17");

        //List
        redisTemplate.opsForList().leftPushAll("homes:list","1","2","3");
        //拿到集合
        List range = redisTemplate.opsForList().range("homes:list", 0, -1);
        for (Object ran : range) {
            System.out.println(ran);
        }
    }

    /**
     *单个传输
     * 10000的数据为例
     */
    @Test
    public void noBatchTest(){
        //开始时间
        long start = System.currentTimeMillis();
        //遍历添加
        for (int i = 0; i <10000 ; i++) {
            redisTemplate.opsForValue().set("K"+i,"v"+i);
        }
        //结束时间
        long ent = System.currentTimeMillis();
        System.out.println(ent - start);//5283

    }

    /**
     * 批处理文件方式  （流水线技术）
     * 10000的数据为例
     */
    @Test
    public void BatchTest(){
        //开始时间
        long start = System.currentTimeMillis();
        //
        redisTemplate.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                //遍历添加
                for (int i = 10000; i <20000 ; i++) {
                    operations.opsForValue().set("K"+i,"v"+i);
                }
                return null;
            }
        });
        //结束时间
        long ent = System.currentTimeMillis();
        System.out.println(ent - start);//1598
    }


    /**
     * 有效期的设置
     */
    @Test
    public void ttlTest(){
        redisTemplate.opsForValue().set("ttl01","tt1test");
        //设置有效期
        redisTemplate.expire("ttl01",60, TimeUnit.SECONDS);
        //获得有效期
        Long tis = redisTemplate.getExpire("ttl01");
        System.out.println(tis);
        System.out.println(redisTemplate.opsForValue().get("ttl01"));
    }
}

