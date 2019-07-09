package com.qf.rabbitmq.publish;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *发布订阅-模式      交换机
 *   生产者类
 *      设置一个交换机
 *
 * type : 交换机类型四种交换机类型，
 *      Direct exchange、  处理路由键。
 *                  需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。这是一个完整的匹配。
 *                  如果一个队列绑定到该交换机上要求路由键 “dog”，则只有被标记为“dog”的消息才被转发，
 *                  不会转发dog.puppy，也不会转发dog.guard，只会转发dog。
 *      Fanout exchange、 不处理路由键。
 *                  你只需要简单的将队列绑定到交换机上。一个发送到交换机的消息都会被转发到与该交换机绑定的所有队列上。
 *                  很像子网广播，每台子网内的主机都获得了一份复制的消息。Fanout交换机转发消息是最快的。
 *      Topic exchange、   将路由键和某模式进行匹配。
 *                  此时队列需要绑定要一个模式上。符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。
 *                  因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”
 *      Headers exchange
 *
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

    //设置一个静态的交换机名称
    public static String exchange_name="fanout_exchange";

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

        //4、声明一个交换机
        //type : 交换机类型四种交换机类型，分别是Direct exchange、Fanout exchange、Topic exchange、Headers exchange。
        channel.exchangeDeclare(exchange_name,"fanout");
        //channel.queueDeclare(exchange_name,false,false,false,null);//队列
        //5、将消息发送到交换机
        for (int i = 0; i <34 ; i++) {
            String hope="南瓜。你最美+"+i;
            //channel.basicPublish("",exchange_name,null,hope.getBytes());//队列
            channel.basicPublish(exchange_name,"",null,hope.getBytes());//交换机
        }

        System.out.println("发送消息成功");

    }
}
