package com.qf.v13searchweb.consumer;


import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.ISearchService;
import com.qf.v13.common.constant.RabbitMQConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *针对平台系统商品业务发送的消息做处理的消费端
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2411:58
 */
@Component
@RabbitListener(queues = RabbitMQConstant.CENTER_SEARCH_QUEUE)
public class ProductHandler {

    @Reference
    private ISearchService searchService;


    /**
     *添加的队列
     */
    @RabbitHandler
    public void processAdd(Long id){
        //添加信息到索引库
        searchService.synDataById(id);

    }
}
