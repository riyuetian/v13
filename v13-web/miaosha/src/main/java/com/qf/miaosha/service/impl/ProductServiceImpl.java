package com.qf.miaosha.service.impl;


import com.qf.miaosha.entity.TProduct;
import com.qf.miaosha.mapper.TProductMapper;
import com.qf.miaosha.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/819:01
 * @description: TODO  秒杀商品业务逻辑
 */
@Service
public class ProductServiceImpl implements IProductService{


    @Autowired
    private TProductMapper  productMapper;

    /**
     * 根据id查询商品
     * @param id
     * @return
     */
    @Override
    //@Cacheable  redis 缓存注解
    //value和key构成最终的key
    //value作为前缀
    //key product::1
    @Cacheable(value = "product",key = "#id")
    public TProduct getById(Long id) {

        return productMapper.selectByPrimaryKey(id);
    }

    /**
     * 悲观锁解决超卖问题
     * @param id
     * @return
     */
    @Override
    public Boolean sale(Long id) {
        //查看库存的信息
       int store= productMapper.selectStoreById(id);
        //更新库存
        if (store>0) {
            productMapper.updatestoreById(id);
            return true;
        }
        return false;
    }

    /**
     * 乐观锁解决超卖问题
     * @param id
     * @return
     */
}

