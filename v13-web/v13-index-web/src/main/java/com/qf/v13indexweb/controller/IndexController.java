package com.qf.v13indexweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IProductTypeService;
import com.qf.v13.entity.TProductType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 商品服务-5  控制类 首页前后端交互
 *
 *  在yml文件配置访问首页的端口9091 和 立即加载
 *  注入依赖
 *  dubbo的配置 和 启动类通过注解启动Dubbo
 *
 *  注解@Controller  标记是mvc中c   controller层
 *  注解@RequestMapping()  来映射 Request 请求与处理器
 *
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1811:00
 */
@Controller
@RequestMapping("index")
public class IndexController {

    //声明一个Iservice对象  在此之前我们需要注入service-ipa依赖  和alibaba-dubbo依赖
    //@Reference注入的是分布式中的远程服务对象，
    //@Resource和@Autowired注入的是本地spring容器中的对象。
    @Reference  //自动注入  实例化
    private IProductTypeService productTypeService;

    /**
     * 遍历集合的方法
     */
    @RequestMapping("home")
    public String showHome(Model model){
        //调用远程服务  获得商品类别集合
        List<TProductType> list = productTypeService.list();
        //保存数据  到页面去展示
        model.addAttribute("list",list);
        return "home";
    }

    /**
     * 采用ajax异步获取
     */
    @RequestMapping("list")
    @ResponseBody
    public List<TProductType> ajaxShowHome(){
        return productTypeService.list();
    }



}
