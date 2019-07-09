package com.qf.tempspringbootredis.entity;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/610:01
 * @description: TODO
 */
public class Home {
    private Long id;
    private String name;

    public Home() {
    }

    public Home(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "home{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
