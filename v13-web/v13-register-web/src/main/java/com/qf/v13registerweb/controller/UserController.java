package com.qf.v13registerweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.entity.TUser;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2521:09
 * @description:  注册系统
 *
 *      UUID.randomUUID().toString()是javaJDK提供的一个自动生成主键的方法。
 *      UUID(Universally Unique Identifier)全局唯一标识符,是指在一台机器上生成的数字，
 *      它保证对在同一时空中的所有机器都是唯一的，是由一个十六位的数字组成,表现出来的形式。
 *      由以下几部分的组合：
 *             当前日期和时间(UUID的第一个部分与时间有关，如果你在生成一个UUID之后，过几秒又生成一个UUID，
 *             则第一个部分不同，其余相同)，时钟序列，全局唯一的IEEE机器识别号
 *             （如果有网卡，从网卡获得，没有网卡以其他方式获得），UUID的唯一缺陷在于生成的结果串会比较长。
！
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Reference  //dubbo的注解
    private IUserService userService;

    //声明一个rabbitTemplate 对象
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //声明templateEngine 对象  用于拼接生存模板对象
    @Autowired
    private TemplateEngine templateEngine;

    //声明redisTemplate对象  用于数据添加到redis缓存  调用修改修序列化方式的
    //@Autowired
    @Resource(name = "redisTemplate")
    private RedisTemplate<String ,Object> redisTemplate;

    /**
     * 根据id查用户
     */
    @RequestMapping("/getById/{id}")
    @ResponseBody
    public TUser getById(@PathVariable("id") Long id){
        TUser user = userService.selectByPrimaryKey(id);
        //System.out.println(user.getUsername());
        return user;
    }

    /**
     * 手机验证
     */
    @RequestMapping("/register1")
    public String register1(){
        return "register1";
    }

    /**
     * 邮箱验证
     */
    @RequestMapping("/register2")
    public String register2(){
        return "register2";
    }

    /**
     * 注册添加用户add
     */
    @PostMapping("/add")
    public String add(TUser user){
        //1 前端 用户名 密码校验
        //2、 邮箱的唯一性校验

        //表单信息保存到数据库 注册成功发送激活邮件
        int id = userService.insertSelective(user);

        if(id>0){
            //根据模板生存邮件的内容   得到text   对应的Html文件
            Context context = new Context();
            //生成激活码 创建唯一标识  采用UUID随机生成
            String uuid = UUID.randomUUID().toString();
            //拼接地址
            String href = new StringBuilder("http://localhost:9094/user/active").append(uuid).toString();
            //设置模板的数据 及改变原模板的用户名和激活连接
            context.setVariable("usernae",user.getUsername());
            context.setVariable("href",href);
            //得到新的附加内容
            String text = templateEngine.process("jihuo.html", context);


            //这里同时将uuid保存到redis缓存中，用来接下来验证用户是否点击激活 （引入依赖和配置文件）  注意需要序列化
            //id是自增 由于这是不同的系统服务  所以要拿到user.getId()  除了需要主键回填 还需要重写方法  insertSelective 修改返回值为id
            redisTemplate.opsForValue().set(uuid,id);


            //发送邮件 异步通知邮件文件
            //邮件的相关参数信息 to  subject text 这里采用map集合 键值对
            Map<String ,String> map = new HashMap<>();
            map.put("to",user.getEmail());
            map.put("subject","疯狂购物商成激活邮件");
            //附加内容采用html模板对象
            map.put("text",text);

            // 队列方式（需添加依赖和配置  采用交换机方式 ）
            //发送给交换机  发布邮件的service来接收这个信号实现发送
            rabbitTemplate.convertAndSend(RabbitMQConstant.REGISTER_EXCHANGE,"user.add",map);


            //返回到页面  登录页面
            return "success";

        }
        //返回到页面  注册页面
        return "register1";

    }



}
