package com.qf.v13cartservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.mapper.TProductMapper;
import com.qf.v13.pojo.CartItem;
import com.qf.v13.api.ICartService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.vo.CartItemVO;
import javafx.scene.effect.SepiaTone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/115:50
 * @description:
 * TODO  购物车的实现类 在没有登录的情况下，将数据存入redis缓存中
 */
@Service
public class CartServiceImpl implements ICartService{

    @Resource(name = "redisTemplate")
    private RedisTemplate<String ,Object> redisTemplate;

    @Autowired
    private TProductMapper productMapper;


    /**
     * 添加
     * @param uuid  定位购物车的标识
     * @param productId 商品id
     * @param count 购买的数量
     * @return
     */
    @Override
    public ResultBean add(String uuid, Long productId, Integer count) {
        //1、创建一个key
        String key = new StringBuilder("user:cart:").append(uuid).toString();
        //这里需要判断一下购物车是否有该商品  toString
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key, productId.toString());
        if (cartItem!=null) {
            //有 就修改 数量和时间
            cartItem.setCount(cartItem.getCount()+count);
            cartItem.setUpdateTime(new Date());
        }else {
            //2、无  则构建一个新的CartItem对象
            cartItem = new CartItem(productId, count, new Date());
        }
        //3、将key-cart 保存到redis中  注意：修改了序列化方式，数据为String,cartItem对象也要toString
        redisTemplate.opsForHash().put(key,productId.toString(),cartItem);
        //4、刷新有效期 7天
        redisTemplate.expire(key,7, TimeUnit.DAYS);
        return new ResultBean("200","添加到购物车成功");
    }

    /**
     * 删除
     * @param uuid  定位购物车的标识
     * @param productId  商品id
     * @return
     */
    @Override
    public ResultBean del(String uuid, Long productId) {
        //1、创建一个key
        String key = new StringBuilder("user:cart:").append(uuid).toString();
        //2、判断有没有该对象
        CartItem  cartItem = (CartItem) redisTemplate.opsForHash().get(key, productId);
        Long result = redisTemplate.opsForHash().delete(key);
        if (result!=null) {
            //4、刷新购物车有效期 7天
            redisTemplate.expire(key,7, TimeUnit.DAYS);
            return new ResultBean("200","删除成功");
        }
        return new ResultBean("200","不存在改商品，删除失败");
    }

    /**
     * 修改
     * @param uuid  定位购物车的标识
     * @param productId  商品id
     * @param count  购买的数量
     * @return
     */
    @Override
    public ResultBean update(String uuid, Long productId, Integer count) {
        //1、创建一个key
        String key = new StringBuilder("user:cart:").append(uuid).toString();
        //这里需要判断一下购物车是否有该商品  toString
        CartItem cartItem = (CartItem) redisTemplate.opsForHash().get(key, productId.toString());
        if (cartItem!=null) {
            //有 就修改 数量和时间
            cartItem.setCount(cartItem.getCount()+count);
            cartItem.setUpdateTime(new Date());
            redisTemplate.opsForHash().put(key,productId.toString(),cartItem);
            //4、刷新有效期 7天
            redisTemplate.expire(key,7, TimeUnit.DAYS);
            return new ResultBean("200","修改成功");
        }
        return new ResultBean("200","不存在改商品，修改失败");
    }

    /**
     * 查看  注意点：购物车是有根据添加的时间来排序的
     * @param uuit
     * @return
     */
    @Override
    public ResultBean query(String uuit) {
        String key = new StringBuilder("user:cart:").append(uuit).toString();
        Map<Object, Object> cartMap = redisTemplate.opsForHash().entries(key);
        //map不具有排序功能  TreeSet 具备实现排序器的功能 这里将map转化为TreeSet
        TreeSet<CartItemVO> cart = new TreeSet<>();
        Set<Map.Entry<Object,Object>> entrySet =  cartMap.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            CartItem cartItem = (CartItem) entry.getValue();

            CartItemVO cartItemVO = new CartItemVO();
            cartItemVO.setCount(cartItem.getCount());
            cartItemVO.setUpdateTime(cartItem.getUpdateTime());
            //这里查询数据库 改为批量查询 获得ids集合   select * from t_product where id in(1、2、3.....n)
            cartItemVO.setProduct(productMapper.selectByPrimaryKey(cartItem.getProductId()));

            cart.add(cartItemVO);
        }
        //4、刷新有效期 7天
        redisTemplate.expire(key,7, TimeUnit.DAYS);
        return new ResultBean("200",cart);
    }



    public ResultBean query11(String uuit) {
        String key = new StringBuilder("user:cart:").append(uuit).toString();
        Map<Object, Object> cartMap = redisTemplate.opsForHash().entries(key);
        //map不具有排序功能  TreeSet 具备实现排序器的功能 这里将map转化为TreeSet
        TreeSet<Object> cart = new TreeSet<>();
        Set<Map.Entry<Object,Object>> entrySet =  cartMap.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            CartItem cartItem = (CartItem) entry.getValue();
            cart.add(cartItem);
        }
        //4、刷新有效期 7天
        redisTemplate.expire(key,7, TimeUnit.DAYS);
        return new ResultBean("200",cart);
    }
}
