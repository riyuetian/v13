package com.qf.v13.vo;

import com.qf.v13.entity.TProduct;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/121:27
 * @description: TODO
 */
public class CartItemVO implements Serializable,Comparable<CartItemVO>{

    private TProduct product;

    private Integer count;

    private Date updateTime;


    public CartItemVO() {
    }

    public CartItemVO(TProduct product, Integer count, Date updateTime) {
        this.product = product;
        this.count = count;
        this.updateTime = updateTime;
    }

    @Override
    public int compareTo(CartItemVO o) {
        return (int) (o.getUpdateTime().getTime()-this.getUpdateTime().getTime());
    }

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


}
