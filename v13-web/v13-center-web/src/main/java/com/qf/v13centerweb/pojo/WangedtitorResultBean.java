package com.qf.v13centerweb.pojo;

/**
 * 富文本框上传图片的返回值类
 * 根据引用的富文本框的规则，返回值不一样。为errno，根据引用规则写
 * @author Mr_Ma
 * Version: 1.0  2019/6/169:48
 */
public class WangedtitorResultBean {

    private String errno;
    private String[] data;

    public WangedtitorResultBean() {
    }

    public WangedtitorResultBean(String errno, String[] data) {
        this.errno = errno;
        this.data = data;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
