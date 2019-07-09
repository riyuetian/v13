package com.qf.miaosha.service;

import com.qf.miaosha.entity.TProduct;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/819:00
 * @description: TODO
 */
public interface IProductService {

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    public TProduct getById(Long id);

    public Boolean sale(Long id );
}
