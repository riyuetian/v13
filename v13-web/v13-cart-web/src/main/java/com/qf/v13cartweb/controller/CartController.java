package com.qf.v13cartweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.ICartService;
import com.qf.v13.common.pojo.ResultBean;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/120:10
 * @description: TODO
 */
@Controller
@RequestMapping("cart")
public class CartController {

    //@Autowired //本地用
    @Reference //远程用
    private ICartService cartService;

    /**
     * 添加
     * @return
     */
    @RequestMapping("add/{productId}/{count}")
    @ResponseBody
    public ResultBean add(@PathVariable("productId")Long productId,
                          @PathVariable("count")Integer count,
                          @CookieValue(name = "user_cart",required = false)String uuid,
                          HttpServletResponse response){
        //1、判断当前uuid是否为空 即购物车是否存在
        if (uuid==null||"".equals(uuid)) {
            //2、创建一个新的uuid
            uuid = UUID.randomUUID().toString();
        }
        //3、判断
        ResultBean resultBean = cartService.add(uuid, productId, count);
        if("200".equals(resultBean.getStatusCode())){
            //4、写入Cookie到客户端
            reflushCookie(uuid, response);
        }
        return  resultBean;
    }


    /**
     * 查看
     * @param uuid
     * @param response
     * @return
     */
    @RequestMapping("query")
    @ResponseBody
    public ResultBean query(@CookieValue(name = "user_cart",required = false)String uuid,
                            HttpServletResponse response){
        //判断 uuid
        if (uuid==null||"".equals(uuid)) {
            return new ResultBean("404","购物车为空");
        }

        ResultBean resultBean = cartService.query(uuid);
        if ("200".equals(resultBean.getStatusCode())) {
            reflushCookie(uuid, response);
        }
        return resultBean;

    }

    /**
     *  删除
     * @param uuid
     * @param productId
     * @param response
     * @return
     */
    @RequestMapping("del/{productId}")
    @ResponseBody
    public ResultBean del(@CookieValue(name="user_cart",required = false)String uuid,
                          @PathVariable("productId")Long productId,
                          HttpServletResponse response){
        if (uuid==null||"".equals(uuid)) {
            return new ResultBean("404","购物车为空");
        }
        ResultBean resultBean = cartService.del(uuid, productId);
        if ("200".equals(resultBean.getStatusCode())) {
            reflushCookie(uuid, response);
        }
        return resultBean;
    }

    /**
     * 修改
     * @param uuid
     * @param productId
     * @param count
     * @param response
     * @return
     */
    @RequestMapping("update/{productId/count}")
    public ResultBean update(@CookieValue(name = "user_cart",required = false)String uuid,
                             @PathVariable("productId")Long productId,
                             @PathVariable("count")Integer count,
                             HttpServletResponse response){
        if (uuid==null||"".equals(uuid)) {
            return new ResultBean("404","购物车为空");
        }
        ResultBean resultBean = cartService.update(uuid, productId, count);
        if ("200".equals(resultBean.getStatusCode())) {
            reflushCookie(uuid, response);
        }
        return resultBean;
    }


    private void reflushCookie(@CookieValue(name = "user_cart", required = false) String uuid, HttpServletResponse response) {
        Cookie cookie = new Cookie("user:cart:",uuid);
        cookie.setPath("/");
        cookie.setDomain("qf.com");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(7*24*60*60);

        response.addCookie(cookie);
    }


}
