package com.qf.v13itemweb;

import java.util.Date;

/**
 * 用于静态页面的实体类  测试
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1911:54
 */
public class Friends {

    private Integer id;
    private String name;
    private Date entryDate;


    public Friends() {
    }

    public Friends(Integer id, String name, Date entryDate) {
        this.id = id;
        this.name = name;
        this.entryDate = entryDate;
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

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }
}
