package com.qf.miaosha.mapper;

import com.qf.miaosha.entity.TSeckill;

import java.util.List;


public interface TSeckillMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TSeckill record);

    int insertSelective(TSeckill record);

    TSeckill selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TSeckill record);

    int updateByPrimaryKey(TSeckill record);


    List<TSeckill> getCanStartSeckill();//定时开启秒杀

    List<TSeckill> getCanStopSeckill();//定时停止秒杀
}