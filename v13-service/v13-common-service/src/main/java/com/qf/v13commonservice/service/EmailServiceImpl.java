package com.qf.v13commonservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IEmailService;
import com.qf.v13.common.pojo.ResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/260:50
 * @description: 发送邮件的实现
 */
@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    //获得发送人的邮箱
    @Value("${mail.fromAddr}")
    private String fromAddr;


    @Override
    public ResultBean send(String to, String subject, String text) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            //由谁发送的 在yml中声明
            helper.setFrom(fromAddr);
            //发送给谁的
            helper.setTo(to);
            //标题
            helper.setSubject(subject);
            //附加内容
            helper.setText(text,true);
            javaMailSender.send(message);

            System.out.println("发送邮件成功");
            return new ResultBean("200","发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();

        }
        return new ResultBean("500","发送邮件失败");
    }
}
