package com.qf.v13ssoweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qf.v13.api.IUserService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2718:41
 * @description:   登录系统
 */
@Controller
@RequestMapping("sso")
public class SsoController {


    @Reference
    private IUserService userService;


    /**
     * 进入登录页的控制 先获取到前一个页面的referer
     */
    @RequestMapping("showLogin")
    public String showLogin(HttpServletRequest request, Model model){
        //获得头部信息
        String referer = request.getHeader("Referer");
        model.addAttribute("referer",referer);
        return "index";
    }

    //登录认证  接口
    //方式一：返回ResultBean的方式（ajax）
    //方式二：跳转到相关页面的方式（form表单）
    /**
     *跳转到相关页面的方式（form表单）
     */
    @RequestMapping("checkLogin")
    public String checkLogin(TUser user,
                             String referer,
                             HttpServletResponse response,
                             HttpServletRequest request){
        //验证  redis缓存  创建验证方法接口
        ResultBean resultBean = userService.checkLogin(user);
        if("200".equals(resultBean.getStatusCode())){
            //登录成功
            //1 、创建cookie(user:token--uuid)
            Cookie cookie = new Cookie("user:token",resultBean.getData().toString());
            //设置路径
            cookie.setPath("/");
            //表示只能通过后端的方式来访问到这个Cookie 避免客户端通过document.cookie 来得到cookie
            cookie.setHttpOnly(true);

            //这里设置cookie的域名为父域名，这样所有的子域名系统都可以获取到 解决同父域的跨域问题
            cookie.setDomain("qf.com");

            //2、 将cookie写到客户端
            response.addCookie(cookie);

            //返回一个视图  这里应返回到上一个页面 从哪来回哪去
            System.out.println("referer-->" + referer);
            if (referer!=null) {
                return  "redirest:http://www.qf.com:9091/index/home";
            }
            return "redirest"+referer;
            //return  "redirest:http://localhost:9091/index/home";
        }
        //登录失败 返回登录页
        return "index";
    }

    /**
     *返回ResultBean的方式（ajax）
     */
    @RequestMapping("checkLogin2")
    @ResponseBody
    public ResultBean checkLogin2(TUser user,HttpServletResponse response){
        //验证  redis缓存  创建验证方法接口
        ResultBean resultBean = userService.checkLogin(user);
        if("200".equals(resultBean.getStatusCode())){
            //登录成功
            //1 、创建cookie(user:token--uuid)
            Cookie cookie = new Cookie("user:token",resultBean.getData().toString());
            //设置路径
            cookie.setPath("/");
            //表示只能通过后端的方式来访问到这个Cookie 避免客户端通过document.cookie 来得到cookie
            cookie.setHttpOnly(true);
            //2、 将cookie写到客户端
            response.addCookie(cookie);

            //返回一个视图  这里应返回到上一个页面
            return  new ResultBean("200",resultBean.getData());
        }

        //登录失败 返回登录页
        return new ResultBean("500","账号密码错误");

    }


    /**
     * 提供一个登录状态检验接口
     *    方式一：redis缓存  重点：怎么解析cookie
     *    方式二：JWT令牌
     * 重点：通过springMVC设置注解@CrossOrigin 来解决跨域请求问题
     */
    @RequestMapping("checkIsLogin")
    @CrossOrigin(origins = "*",allowCredentials = "true")
    @ResponseBody
    public ResultBean checkIsLogin(HttpServletRequest request){
        /*方式一：redis  1、解析cookie 得到uuid*/
//        Cookie[] cookies = request.getCookies();
//        if (cookies!=null) {
//            //遍历cookie  找到user：token
//            for (Cookie cookie : cookies) {
//                if("user:token".equals(cookie.getName())){
//                    String uuid=cookie.getValue();
//                    //2、通过uuid验证状态 并返回一个判断依据
//                    return userService.checkIsLogin(uuid);
//                }
//            }
//        }
        //没有 "user:token"
        /*方式二：JWT  */

        return new ResultBean("404",null);
    }


    /**
     * 提供一个登录状态检验接口 ,通过Jsop来实现跨域请求问题
     */
    @RequestMapping("checkIsLoginJsonp")
    @ResponseBody
    public String checkIsLoginJsonp(@CookieValue(name="user:token",required = false) String uuid,
                                    String callback) throws JsonProcessingException {
        ResultBean resultBean =null;
        if (uuid!=null) {
            resultBean = userService.checkIsLogin(uuid);
        }else {
            resultBean = new ResultBean("404", null);
        }
        //将对象转化为json
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(resultBean);
        //回调客户端的方法

        return callback+"("+json+")";//deal(json)

    }

    /**
     * 提供一个登录状态检验接口   通过注解简化解析cookie
     */
    @RequestMapping("checkIsLogin2")
    @ResponseBody
    public ResultBean checkIsLogin2(@CookieValue(name="user:token",required = false) String uuid){
        if (uuid!=null) {
            //2、通过uuid验证状态 并返回一个判断依据
            return userService.checkIsLogin(uuid);
        }
        //没有 "user:token"
        return new ResultBean("404",null);
    }

    /**
     * 注销的接口
     *  采用redis时：  删除缓存中的uuid 和cookie 中的uuid    重点 删除cookie
     *  采用jwt时：  服务端没有存储凭证信息，只需要注销客户端的即可
     */
    @RequestMapping("logout")
    @ResponseBody
    public ResultBean logout(@CookieValue(name="user:token",required = false) String uuid,
                             HttpServletResponse response){
        if(uuid!=null){
            //删除cookie
            Cookie cookie = new Cookie("user:token", uuid);
            cookie.setPath("/");
            //这里设置cookie的域名为父域名，这样所有的子域名系统都可以获取到 解决同父域的跨域问题
            cookie.setDomain("qf.com");
            //设置有效期为0，表示失效
            cookie.setMaxAge(0);
            //把cookis写到客户端
            response.addCookie(cookie);
            //删除redis凭证 采用JWT时，不需要
            //return  userService.logout(uuid);
            return new ResultBean("200","注销成功");
        }
        return new ResultBean("404",null);
    }

}
