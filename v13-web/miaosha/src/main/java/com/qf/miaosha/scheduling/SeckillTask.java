package com.qf.miaosha.scheduling;

import com.qf.miaosha.entity.TSeckill;
import com.qf.miaosha.mapper.TSeckillMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/821:32
 * @description: TODO  秒杀活动的定时控制
 *      通过注解 及 引用cron表达式 来实现的
 *      @EnableScheduling  //开启定时任务
 *      @Scheduled( cron ="0/10 * * * * *")  //每过10秒扫描一次
 */
@Component
public class SeckillTask {

    @Autowired
    private TSeckillMapper seckillMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 定时扫描开启秒杀任务
     *  优化：创建秒杀列表到redis缓存，减少与数据库的交互
     */
    @Scheduled( cron ="0/10 * * * * *")  //每过10秒扫描一次
    public void startSeckill(){
        //查询秒杀表
        //操作符NOW()获得当前时间    BETWEEN ... AND 会选取介于两个值之间的数据范围。这些值可以是数值、文本或者日期。
        //select * from t_seckill where NOW() BETWEEN start_time and stop_time and status=0;
        List<TSeckill> seckills =seckillMapper.getCanStartSeckill();
        //获得符合秒杀条件的记录
        if (seckills!=null) {
            for (TSeckill seckill : seckills) {
                //修改状态值
                seckill.setStatus("1");
                seckillMapper.updateByPrimaryKey(seckill);
                System.out.println("秒杀活动"+seckill.getId()+"开启");

                //创建列表  添加到redis
                //拼接唯一标识符
                String key = new StringBuilder("eckill:product:").append(seckill.getId()).toString();
                //确定队列的长度(秒杀数量) 采用list类型 存入redis eg:eckill:product:1---list(1,1,1,1,1..)
                for (Integer i = 0; i < seckill.getCount(); i++) {
                    redisTemplate.opsForList().leftPush(key,seckill.getId());
                }
                System.out.println("秒杀列表准备就绪");
            }
        }
    }
    /**
     * 定时扫描停止秒杀任务
     */
    @Scheduled( cron ="0/10 * * * * *")  //每过10秒扫描一次
    public void stopSeckill(){
        //查询秒杀表
        List<TSeckill> seckills = seckillMapper.getCanStopSeckill();
        //获得符合停止秒杀条件的记录
        if (seckills!=null) {
            for (TSeckill seckill : seckills) {
                //修改状态值
                seckill.setStatus("2");
                seckillMapper.updateByPrimaryKey(seckill);
                System.out.println(seckill.getId()+"-->秒杀结束");

                //删除redis中的秒杀列表
                String key = new StringBuilder("eckill:product:").append(seckill.getId()).toString();
                redisTemplate.delete(key);
                System.out.println("秒杀列队清除");
            }
        }
    }
}
