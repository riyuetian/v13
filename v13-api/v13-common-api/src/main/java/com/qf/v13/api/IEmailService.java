package com.qf.v13.api;

import com.qf.v13.common.pojo.ResultBean;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2521:31
 * @description: 邮件系统
 */
public interface IEmailService {


    //发送邮件的方法 统一的返回结果
    //3个参数 发送给谁  主题 附加内容
    public ResultBean send(String to ,String subject,String text);
}
