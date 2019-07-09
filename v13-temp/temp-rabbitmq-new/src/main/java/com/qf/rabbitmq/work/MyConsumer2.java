package com.qf.rabbitmq.work;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 普通队列  消费者
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2021:34
 */
public class MyConsumer2 {

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

        channel.basicQos(1);

        //消费者监听列队
        //4 创建一个消费者对象
        Consumer consumer = new DefaultConsumer(channel){
            //等待有消息  回调该方法
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                System.out.println("消费者2 收到的消息-->"+msg);

               channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        //5 、消费者需要监听列队
        channel.basicConsume(queue_name,false,consumer);
    }
}
