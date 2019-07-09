package com.qf.jedis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2419:41
 * @description: TODO
 *
 * 方法一：
 * 测试通过jedis来实现远程客户端连接上Redis  这个是最原生的方式
 */
public class JedisTest {

    /**
     *注意：Redis的低版本默认没有设置仅限本机访问，
     * 而高版本有设置，所以需要将高版本的本机绑定注释掉在重启 redis.conf中的bind 127.0.0.1 注释掉
     */
    @Test
    public void stringTest(){
        //创建jedis对象
        Jedis jedis = new Jedis("192.168.199.129", 6379);
        //打开权限需要设置访问密码密码 不然会异常错误 在文件中先配置密码 requirepass 123123
        jedis.auth("123123");
        //赋值
        jedis.set("home","LOVE南瓜");
        String s = jedis.get("home");
        System.out.println(s);

        jedis.mget("home1","nangua","home2","caipiao");
        List<String> mget = jedis.mget("home1", "home2");
        for (String s1 : mget) {
            System.out.println(s1);
        }
    }

    @Test
    public void otherTest() {
        //创建jedis对象
        Jedis jedis = new Jedis("192.168.199.129", 6379);
        //打开权限需要设置访问密码密码 不然会异常错误 在文件中先配置密码 requirepass 123123
        jedis.auth("123123");

        //hash类型
        Map<String , String> map  = new HashMap<>();
        map.put("name","iafang");
        map.put("price","wujia");
        jedis.hmset("xiannv:1",map);

        Map<String, String> stringMap = jedis.hgetAll("xiannv:1");
        //迭代遍历 entrySet()：迭代后可以e.getKey()，e.getValue()取key和value
        Set<Map.Entry<String, String>> entries = stringMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey() + "--" + entry.getValue());
        }

    }
}
