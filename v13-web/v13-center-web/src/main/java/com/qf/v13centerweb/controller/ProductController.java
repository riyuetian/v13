package com.qf.v13centerweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProductService;

import com.qf.v13.api.ISearchService;
import com.qf.v13.common.constant.RabbitMQConstant;
import com.qf.v13.common.utils.HttpClientUtils;
import com.qf.v13.entity.TProduct;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.pojo.TProductVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *调用商品类5  前后端的交互控制类
 * 这里要注意的是service发布商品服务（8080） 不能和调用商品服务的端口一样 调用可设置9090
 * @author Mr_Ma
 * Version: 1.0  2019/6/139:30
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Reference  //这里的注解要注意 采用alibaba  dubbode的注解
    private IProductService productService;


    //同步数据调用  声明searchServic对象
    @Reference
    private ISearchService searchService;

    //系统间的同步改为异步  声明rabbitTemplate对象
    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     *根据ID查询商品
     */
    @RequestMapping("get/{id}")
    @ResponseBody
    public TProduct getById(@PathVariable("id") Long id){
        return productService.selectByPrimaryKey(id);
    }

    /**
     * 列表展示
     */
    @RequestMapping("list")
    public String list(Model model){
        //声明一个集合
        List<TProduct> list = productService.list();
        System.out.println("******************"+list.size());
        //保存数据
        model.addAttribute("list",list);
        //跳转到页面展示
        return "product/list";
    }

    /**
     * 分页展示1-5
     * @PathVariable:
     *      来获得请求url中的动态参数的，并且支持动态url访问，可以从url中直接提取参数而不需要采用?q=q&d=d的形式
     */
    @RequestMapping("page/{pageIndex}/{pageSize}")
    public String page(@PathVariable("pageIndex") Integer pageIndex,
                       @PathVariable("pageSize") Integer pageSize,
                       Model model){
        //1调用service的分页方法 得到一个分页对象
        PageInfo<TProduct> page = productService.page(pageIndex, pageSize);
        //保存数据
        model.addAttribute("page",page);
        //转发
        return "product/list";
    }

    /**
     * 添加商品 5
     * 注解@PostMapping  便是提交方式是Post 提交
     * @getmapping   get提交
     */
   /* @GetMapping
    @RequestMapping(method = RequestMethod.GET)*/
   @PostMapping("add")
    public String  add(TProductVO productVO){
       //添加
       Long id = productService.save(productVO);
       //重定向到分页页面
       //order by update_time desc

       //这里添加完成后 通知搜索系统需要数据跟新
       //跟新索引库 调用方法
       //这里不适用做全量同步 耗资源 后期同步策略应该为是为增量同步  在ISearchService中新增方法
       //searchService.synAllData(); 全量
       //searchService.synDataById(id);//增量
       //再生成相应的静态页面 http://localhost:9093/item/createHTMLById/"+id
       //象浏览器一样调用静态页面的接口
       HttpClientUtils.doGet("http://localhost:9093/item/createHTMLById/"+id);

       //这里添加到索引库 和 生成静态页面 都是不同的服务 应用程序
       //可以将上述的同步改为异步
       // 发送消息给交换机  发送给谁 ，是要做什么，哪些参数
       rabbitTemplate.convertAndSend(RabbitMQConstant.CENTER_PRODUCT_EXCHANGE,"product.add",id);

       return "redirect:/product/page/1/1";
    }

    /**删除单个商品 逻辑删除
     *
     * 需要注意采用ajax 返回数据类型的统一规范  json
     *
     * 对于非视图的返回类型一般都统一抽象为一个类 来管理
     *
     * @ResponseBody 表示该方法的返回结果直接写入 HTTP response body 中
     *  比如异步获取 json 数据，加上 @ResponseBody 后，会直接返回 json 数据
     */
    @PostMapping("delById/{id}")
    @ResponseBody
    public ResultBean delById(@PathVariable("id") Long id){
        //System.out.println(111);
        int count = productService.deleteByPrimaryKey(id);
        if(count>0){
            //删除成功后同时 删除索引库里相信的信息  删除静态页面

            return new ResultBean("200","删除成功");
        }
        return new ResultBean("404","删除失败");
    }

    /**
     * 批量删除4
     * @RequestParam
     *      用于将指定的请求参数赋值给方法中的形参  可以接收json格式的数据，并将其转换成对应的数据类型。
     *
     *      用来处理Content-Type: 为 application/x-www-form-urlencoded编码的内容。提交方式为get或post。
     *      （Http协议中，如果不指定Content-Type，则默认传递的参数就是application/x-www-form-urlencoded类型）
     *      RequestParam实质是将Request.getParameter() 中的Key-Value参数Map利用Spring的转化机制ConversionService配置，转化成参数接收对象或字段。
     *       get方式中query String的值，和post方式中body data的值都会被Servlet接受到并转化到Request.getParameter()参数集中，所以@RequestParam可以获取的到。
     */
    @PostMapping("batchDel")
    @ResponseBody
    public ResultBean batchDel(@RequestParam List<Long> ids){
        Long count = productService.batchDel(ids);
        if(count>0){
            //删除成功后同时 删除索引库里相信的信息  删除静态页面

            return new ResultBean("200","删除成功");
        }
        return new ResultBean("404","删除失败");
    }
}
