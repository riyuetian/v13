package com.qf.tempspringbootrabbitmq;

import java.io.Serializable;

/**
 * 用于整合rabbutmq测试的 实体类
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/2216:29
 */
public class Book implements Serializable{

    private  Integer id;
    private String  name;
    private Integer price;

    public Book() {
    }

    public Book(Integer id, String name, Integer price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
