package com.qf.rabbitmq.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *work-模式
 *    生成者类发送多个消息
 *    多个消费者 接受消息
 *         特点：其中1个消费者接受了消息 ，其他消费者将接受不到该消息
 *              消费者都已开启 ，在开启生产者 。将会 默认会平均分配，两个消费者处理的消息数量一样
 *                 若是有添加延时、  效果 将会有所不同
 *
 * 生产者类
 * queueDeclare()不带参数方法默认创建一个由RabbitMq命名的（amq.gen-LHQZz...）
            名称，这种队列也称之为匿名队列，排他 的，自动删除的，非持久化的队列
    queue:队列名称
    durable： 是不持久化， true ，表示持久化，会存盘，服务器重启仍然存在，false，非持久化
    exclusive ： 是否排他的，true，排他。如果一个队列声明为排他队列，该队列公对首次声明它的连接可见，并在连接断开时自动删除，
    autoDelete ：是否自动删除,true，自动删除，自动删除的前提：至少有一个消息者连接到这个队列，之后所有与这个队列连接的消息都断开时，才会自动删除，，
            备注：生产者客户端创建这个队列，或者没有消息者客户端连接这个队列时，不会自动删除这个队列
    arguments：其它一些参数。如：x-message-ttl,之类

 *channel.basicPublish(）
 *   exchange -- 交换机名称
 *   routingKey -- 路由关键字
 *   props -- 消息的基本属性，例如路由头等
 *   body -- 消息体
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2021:35
 */
public class Producer {

    //设置一个静态的队列名称
    public static String queue_name="work_queue";

    public static void main(String[] args) throws IOException, TimeoutException {

        //1、创建连接对象
        ConnectionFactory factory = new ConnectionFactory();
        //设置地址
        factory.setHost("192.168.199.129");
        //设置端口
        factory.setPort(5672);
        //设置虚拟主机
        factory.setVirtualHost("/fang");
        //设施账号密码
        factory.setUsername("fang");
        factory.setPassword("123");

        //2、拿到连接
        Connection connection = factory.newConnection();
        //3、创建本次交互的通信
        Channel channel = connection.createChannel();
        //4、声明队列
        channel.queueDeclare(queue_name,false,false,false,null);
        //5、将消息发送到队列

        for (int i = 0; i <34 ; i++) {
            String hope="南瓜。你最美+"+i;
            channel.basicPublish("",queue_name,null,hope.getBytes());
        }

        System.out.println("发送消息成功");

    }
}
