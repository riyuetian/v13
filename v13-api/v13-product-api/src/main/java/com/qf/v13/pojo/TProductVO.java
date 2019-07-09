package com.qf.v13.pojo;

import com.qf.v13.entity.TProduct;

import java.io.Serializable;

/**
 * 添加商品1  所需要的 类   序列化
 * 在添加中有商品描述属性  而在实体类TProduct中不包含  商品描述productDesc属性
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1417:22
 */
public class TProductVO implements Serializable{

    private TProduct product;

    private String productDesc;

    public TProduct getProduct() {
        return product;
    }

    public void setProduct(TProduct product) {
        this.product = product;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
}
