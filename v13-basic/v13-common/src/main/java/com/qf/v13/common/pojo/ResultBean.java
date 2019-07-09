package com.qf.v13.common.pojo;

import java.io.Serializable;

/**
 * 删除2
 * 统一结果类型类
 * @author Mr_Ma
 * Version: 1.0  2019/6/1421:45
 */
public class ResultBean<T> implements Serializable{

    private String statusCode;
    private  T data;

    public ResultBean() {
    }

    public ResultBean(String statusCode, T data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
