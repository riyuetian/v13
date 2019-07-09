package com.qf.tempspringbootredis.entity;

import java.io.Serializable;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2518:04
 * @description: TODO
 */
public class Student implements Serializable {


    private String  name;

    public Student() {
    }

    public Student(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
