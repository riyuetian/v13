package com.qf.v13.api;

import com.qf.v13.common.pojo.ResultBean;
import com.sun.org.apache.regexp.internal.RE;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/115:27
 * @description: TODO  购物车业务层接口
 */
public interface ICartService {

    /**
     *  添加商品到购物车
     * @param uuid  定位购物车的标识
     * @param productId 商品id
     * @param count 购买的数量
     * @return
     */
    public ResultBean add(String uuid, Long productId,Integer count);

    /**
     *  删除
     * @param uuid  定位购物车的标识
     * @param productId  商品id
     * @return
     */
    public ResultBean del(String uuid, Long productId);

    /**
     *    修改
     * @param uuid  定位购物车的标识
     * @param productId  商品id
     * @param count  购买的数量
     * @return
     */
    public ResultBean update(String uuid,Long productId,Integer count);

    /**
     *   查看购物车
     * @param uuit
     * @return
     */
    public ResultBean query(String uuit);


}
