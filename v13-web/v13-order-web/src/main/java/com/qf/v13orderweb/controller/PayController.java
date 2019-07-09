package com.qf.v13orderweb.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.v13orderweb.pojo.PayContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/219:55
 * @description: TODO
 */
@Controller
@RequestMapping("aliPay")
public class PayController {

    @Autowired
    private AlipayClient alipayClient;

    @Value("${alipay.alipayPublicKey}")
    private String alipayPublicKey;


    @RequestMapping("toPay")
    public void toPay(HttpServletResponse response, String orderNo) throws IOException {

//        AlipayClient alipayClient = new DefaultAlipayClient(
//                "https://openapi.alipaydev.com/gateway.do",
//                "2016101100661786",
//                "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDvQKceT3OFeVkn6FM/hkgW94pC1ds/G3zo7bUv6+b/zez7Y582+bgC1I39fA5MCuGftXIovmIPGmDXS4napnukAWnhb9yufl1RV3iBMlKBpHeBVguuWxuv3UJDhRK8yCRxBbEUKH0f9rVtH6eBK2MENX8MdC1FVRGHhTaB5aCoV4UkDEcMneNsbpAKzrDG/6YPBM2MVJZp2Ax2OrvDHwpjpjkMqPIG/QxvroVRPxVNAGr5E76mLvwSkHgxyUNKiwpazv5hsoVPLmWN71/tU/0Wgivbf9GLEVct2oiHggDY/4S3xWQWajtXZnB8dj7yPSfn64+tLiiNNljkwQ5qs5FDAgMBAAECggEAZOdnlCj+F7TuHR6mvYvsq4MLFNUM+rilHbfjSqq7afd3ky6Y357CNK9gkx/XB8svDSSBWR2yTZxaa3K4WTcNvkW6WhHLiOxSfxPdBuAqW/L5U9ruYkKakrlGUSO2m1ftxp0X6eCJpQlvyu4mvxuFTykgxCfbT/powjc0neQytcwi4TqTcJdB7bK6A8qsFPvOuS1ipzK9KSgxAOn3a5komV7Nsx1iCX0qJ79DzHKRq/78oxULibqc+eJB5h3+2VbpbmqmDWIsF6HJStErq4UMp/IQp/Dhd48/HdkjZZBrS+A1ffFzgA3EytkeF+U5BG1uriSgY3l5H1PT7HOe6rorwQKBgQD4hO88pXGPjd2NMwbTWI8/87e3kZUBWry5fLpkoGIkDQIcXedFIcBTCuRjpM9oSBPAeZmWDf+cANEQ7gh5MsPioShCW0siUwNS7m/8xsiswcCCdSgiZl1cYqmoG9ikIJvZB7HCtfeMf44i5Jhb84ENp4Y7CYM4NYnNH/7FF++R/wKBgQD2dE9O6XtwM7uxWf9KIfk4DBLPscnXVqIobv1DJweyhbJ+xHAfYittGptYLGoP4D24e5vPiwNf4SXqoe3xEsKcFHRh2bjgyFIvxicUV/WQrH9XNZLMoRpRXTsXHXSZJHZUfpc17J/8C4JE/R/luKeflyGL/NyKTjfQv7mDDxs4vQKBgQDjh8IJQmfgSMCJICilozU0Fg0kEjPcE5kqMdntezGfVBGUqxysnKQa46QK3r/N9/+tfydwzMwzpkPDPkufuyzA90+GwVLe0CYA7bC2O8FK+fEo/96I8DOV+/bcpPIKApjtcS/81uEYT0hxBd3Aggar2673qlPrSIkrtplpFesPPQKBgCaIwwm8me9ON2+gebkWnMf1o09UJhIEWrBlwjWouZxhKSqG/lz51X5mpZlgwGihECVzNkZ6Htnz8yC/ltuVqlYLILNX/n8u8yLDIcMdiajIP9Tb7IsQOXQ1VpLjqSJ0wAewD2aAC/e1uPWeeklJ2POsBrPDopwBZNq/z3q9du0lAoGBAKTAQGIVMg62dw+em1dQQhIgXhXW6S4yhzjR+NnC4+6HmksXlESjGHU99XmARSFv9BzL31WS/2xNp4iaf5cu+Wjg/lvfA/y4pHsyDe7vcbuEPR0YxPvBveSppj6j7qLNriEENZX47gHrEmJuIlfC2bcEMpDmKJ/A8aTFL2xkZ9cO",
//                "json",
//                "utf-8",
//                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnaDP+pXio0iJtEOXSB4F4KrFvhBbZZPNVSAYY5frN6v5Ug9isTCZTratyqLdsDzTc9eqFX0bGIumXKEq8VbdN5EDhjDA7xxAGyLXK0yGF7/Tg2T0wiuQQF3sxt/gLbPQ+t9wmevk5ljSxclTCIvxe33aLIO9PmWVhJpGbk7IuokpGNMuZkR9n4sTcq3EASK7egyv5fjTRWjf7Vy91bfap6/Rvm8ktmnTTpVXB3s1aemk6BpbM2QK0OFVj2dtQXtDyJY6KO7uw1l4QqSULMo/ivOW2xwqQrtNxmsl7XHf9gVYx3y6xAT19gAM6b+TS1eENkDEtIey+9dUia1addFxjQIDAQAB",
//                "RSA2"); //获得初始化的AlipayClient

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://domain.com/CallBack/return_url.jsp");
        alipayRequest.setNotifyUrl("http://uxu24m.natappfree.cc/aliPay/notityPayResult");//在公共参数中设置回跳和通知地址

        //创建一个业务对象
        PayContent payContent = new PayContent(orderNo,"FAST_INSTANT_TRADE_PAY","5201314","爱的承诺","水滴石穿，海枯石烂");
        //转换为json类型
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(payContent);
        //添加到alipayRequest
        alipayRequest.setBizContent(json);

        /*默认的代码方式json*/
//        alipayRequest.setBizContent("{" +
//                "    \"out_trade_no\":\""+orderNo+"\"," +
//                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
//                "    \"total_amount\":43434," +
//                "    \"subject\":\"Iphone6 16G\"," +
//                "    \"body\":\"Iphone6 16G\"," +
//                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\"" +
//                "  }");//填充业务参数

        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(form);//直接将完整的表单html输出到页面
        response.getWriter().flush();
        response.getWriter().close();
    }

    @RequestMapping("notityPayResult")
    public void notityPayResult(HttpServletRequest request,HttpServletResponse response) throws AlipayApiException, IOException {
        //获取到所有请求参数
        Map<String, String[]> sourceMap = request.getParameterMap();
        //创建SDK需要的参数类型
        Map<String, String> paramsMap =new HashMap<>(); //将异步通知中收到的所有参数都存放到map中
        //parameterMap--->转paramsMap
        Set<Map.Entry<String, String[]>> entries = sourceMap.entrySet();
        for (Map.Entry<String, String[]> entry : entries) {
            //构建Value
            StringBuilder value = new StringBuilder();
            String[] sourceArray = entry.getValue();
            for (int i = 0; i < sourceArray.length-1; i++) {
                value.append(sourceArray[i]).append(",");
            }
            //拼接集合最后一个元素
            value.append(sourceArray[sourceArray.length-1]);
            //添加到paramsMap
            paramsMap.put(entry.getKey(),value.toString());
                
        }
        //验签 是否是支付宝发过来的信息
        boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, alipayPublicKey, "utf-8", "RSA2"); //调用SDK验证签名
        if(signVerified){
            // TODO 验签成功后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续商户自身业务处理，校验失败返回failure
            System.out.println("是支付宝发过来的");
            //1、获取订单号
            String orderNo = request.getParameter("out_trade_no");
            //2、获取到订单的金额
            String money = request.getParameter("total_amount");
            //3、根据订单号去查询我们数据库中的金额
            //4、匹配金额是否相同
            //5、相同  就修改订单状态 改为已支付
            //6、返回一个标识success给支付宝
            PrintWriter writer = response.getWriter();
            writer.write("success");
            writer.flush();
            writer.close();

            //如果金额对不上
        }else{
            // TODO 验签失败则记录异常日志，并在response中返回failure.
            System.out.println("不是支付宝发过来的");
        }
    }

}
