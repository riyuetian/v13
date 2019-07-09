package com.qf.v13.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/117:11
 * @description: TODO  未登录的公共购物车实体类 实现排序器
 */
public class CartItem implements Serializable,Comparable<CartItem> {

    private Long serialVersionUID=1L;

    private Long productId;
    private Integer count;
    private Date updateTime;

    public CartItem() {
    }

    public CartItem(Long productId, Integer count, Date updateTime) {
        this.productId = productId;
        this.count = count;
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "productId=" + productId +
                ", count=" + count +
                ", updateTime=" + updateTime +
                '}';
    }

    @Override
    public int compareTo(CartItem o) {
        return (int) (o.getUpdateTime().getTime()-this.getUpdateTime().getTime());
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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
