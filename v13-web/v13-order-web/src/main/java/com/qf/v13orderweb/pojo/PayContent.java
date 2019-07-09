package com.qf.v13orderweb.pojo;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/217:51
 * @description: TODO
 */
public class PayContent {

    private String out_trade_no;  //订单号
    private String product_code; //销售产品码，与支付宝签约的产品码名称
    private String total_amount; //订单总金额  88.88
    private String subject;  //订单标题
    private String body;  //订单描述

    public PayContent() {
    }

    public PayContent(String out_trade_no, String product_code, String total_amount, String subject, String body) {
        this.out_trade_no = out_trade_no;
        this.product_code = product_code;
        this.total_amount = total_amount;
        this.subject = subject;
        this.body = body;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
