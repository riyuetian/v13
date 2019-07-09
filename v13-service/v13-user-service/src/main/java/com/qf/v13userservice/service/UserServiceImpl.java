package com.qf.v13userservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import com.qf.v13.mapper.TUserMapper;
import com.qf.v13userservice.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
//import org.springframework.stereotype.Service;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2520:35
 * @description: TODO
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<TUser> implements IUserService{

    @Autowired
    private TUserMapper userMapper;

    /**
     * 声明一个redisTemplate 操作redis
     */
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    public IBaseDao<TUser> getBaseDao() {
        return userMapper;
    }

    /**
     * 登录校验
     */
    @Override
    public ResultBean checkLogin(TUser user) {
        //业务方法 操作两块存储层 数据库 redis
        //1、查询数据库做判断
        TUser currnetUser = userMapper.selectByUsername(user.getUsername());
        if (currnetUser!=null) {
            //验证密码
            if(currnetUser.getPassword().equals(user.getPassword())){
                //账户密码正确  登陆成功
                /*保存凭证方式一： 存到redis 改变序列化方式*/
//                String  uuid = UUID.randomUUID().toString();
//                //拼接唯一凭证
//                String key = new StringBuilder("user:token:").append(uuid).toString();
//                //这里保证账号安全性 密码设置为null
//                user.setPassword(null);
//                //保存uuid
//                redisTemplate.opsForValue().set(key,user);
//                //设置好有效期30分钟
//                redisTemplate.expire(key,30, TimeUnit.MINUTES);
                /*保存凭证方式二： 生成JWT方式  依赖 公共类 生成 解密*/
                JwtUtils jwtUtils = new JwtUtils();
                jwtUtils.setSecretKey("loveYOU");
                jwtUtils.setTtl(30*60*1000);
                //生成令牌
                String uuid = jwtUtils.createJwtToken(currnetUser.getId().toString(), currnetUser.getUsername());

                return  new ResultBean("200",uuid);
            }
        }
        return new ResultBean("500","");
    }

    /**
     * 登录状态检测
     */
    @Override
    public ResultBean checkIsLogin(String uuid) {
        /*方式一：redis */
//        //组装key 得到 user:token:uuid
//        String key = new StringBuilder("user:token:").append(uuid).toString();
//        //根据key 查找 redis是否存在
//        TUser currentUser = (TUser) redisTemplate.opsForValue().get(key);
//        //d判断user
//        if (currentUser!=null) {
//            //存在 就是登录状态
//            //刷新凭证的有效期
//            redisTemplate.expire(key,30,TimeUnit.MINUTES);
//            //返回user
//            return new ResultBean("200",currentUser);
//        }
//      //返回为null标识
        //return new ResultBean("404",null);
        /*方式二：JWT */
        try{
            JwtUtils jwtUtils = new JwtUtils();
            jwtUtils.setSecretKey("fang");
            //解析
            Claims claims = jwtUtils.parseJwtToken(uuid);
            return new ResultBean("200",claims.getSubject());
        }catch (Exception e){
            return new ResultBean("404",null);
        }
    }

    /**
     * 采用redis时:
     *       注销: 删除缓存中的uuid 和cookie 中的uuid   这个方式时删除redis中的uuid
     * 采用 JWT 时，
     *      服务端没有存储凭证信息，只需要注销客户端的即可
     */
    @Override
    public ResultBean logout(String uuid) {
        /* redis：*/
        //拼接key
        String key = new StringBuilder("user:token").append(uuid).toString();
        Boolean delete = redisTemplate.delete(key);
        if (delete) {
            return new ResultBean("200","删除成功");
        }
        return new ResultBean("404","删除失败");

    }
}
