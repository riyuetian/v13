package com.qf.v13.common.constant;

/**
 *
 *创建一个公共的交换机名称.队列名称  的接口
 *  这里属性整个项目不要需更改  设置为final   保持参数的一致性  命名规范 ：大写字母和下划线（JING_DONG）
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/248:49
 */
public interface RabbitMQConstant {

    //小写转大写  ctrl+shift+u

    //交换机名称  用于 发布商品服务系统  通知 搜索系统
    public static final String CENTER_PRODUCT_EXCHANGE= "center-product-exchange";

    //搜索队列名称   用于 发布商品服务系统  通知 搜索系统
    public static final String CENTER_SEARCH_QUEUE="center-search-exchange";

    //交换机  用于  注册系统 通知 邮件系统
    public static final String REGISTER_EXCHANGE="register-exchange";

    //邮件队列名称 用于  注册系统 通知 邮件系统
    public static final String EMAIL_QUEUE="email-queue";

}
