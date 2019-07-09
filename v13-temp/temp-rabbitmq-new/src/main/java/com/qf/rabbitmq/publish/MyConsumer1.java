package com.qf.rabbitmq.publish;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发布订阅模式-交换机   消费者1
 *      声明队列，每个消费者都有自己对应的队列，所以队列名要不同
 *      将队列绑定都交换机上面 ，

 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2021:34
 */
public class MyConsumer1 {

    //设置交换机名
    public static String exchange_name="fanout_exchange";
    //设置一个静态的队列名称
    public static String queue_name="fanout_exchange_queue";
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

        //这里我们要声明一个队列
        channel.queueDeclare(queue_name,false,false,false,null);

        //表示每次只接收1个消息 。起到限流作用
        channel.basicQos(1);

        //消费者监听列队
        //4 创建一个消费者对象
        Consumer consumer = new DefaultConsumer(channel){
            //等待有消息  回调该方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("消费者1 收到的消息->"+msg);

                //增加一个延时的效果  1s  同样是平分消息 ，但接受的消息要慢1s 不影响其他的消费者
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //处理结束之后再 回复
                //multiple: false 是否批量确认
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        //绑定队列到交换机
        channel.queueBind(queue_name,exchange_name,"");
        //5 、消费者需要监听列队
        //aotoAck： true 自动回复   false手动回复
        channel.basicConsume(queue_name,false,consumer);


    }
}
