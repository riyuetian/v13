package com.qf.v13itemweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13itemweb.config.CommonConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 完成系统根据id实现网站静态页面
 *      1、静态页面ftl
 *      2、控制层Controller
 *      3、引入相关依赖
 *             返回结果
 *             实体类
 *
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1914:47
 */
@Controller
@RequestMapping("item")
public class ItemController {

    @Reference
    private IProductService productService;

    @Autowired
    private Configuration configuration;

    //注入提共线程池 的对象
    @Autowired
    private ThreadPoolExecutor threadPool;

    /**
     * HttpClient
     */
    @RequestMapping("param")
    @ResponseBody
    public ResultBean param(String name,String password){
        System.out.println(name+"-->"+password);
        return new ResultBean("200","ok");
    }

    /**
     * 生成静态页面
     */
    @RequestMapping("createHTMLById/{id}")
    @ResponseBody
    public ResultBean createHTMLById(@PathVariable("id") Long id){
        //抽取方法快捷键 ctrl+alt+M
        return createHTML(id);
    }

    /**
     * 单线程的方法
     */
    private ResultBean createHTML(@PathVariable("id") Long id) {
        try {
            //1.获取模板
            Template template = configuration.getTemplate("item.ftl");
            //2、获取数据
            Map<String ,Object> data = new HashMap<>();
            data.put("product",productService.selectByPrimaryKey(id));
            //3、输出 先获取完整路径 在输出
                 //FileWriter fileWriter = new FileWriter("G:\\myJavaItems\\IDEA\\v13\\v13-web\\v13-item-web\\src\\main\\resources\\static\\item.html");
                 //这里我们已classPath的方式来保存路径  先获取到static的路径  这里生成的路径是运行环境中的
            String path = ResourceUtils.getURL("classpath:static").getPath();
                 //拼接带文件的名的最终路径G:\\myJavaItems\\IDEA\\v13\\v13-web\\v13-item-web\\src\\main\\resources\\static\\id.html
            String filePath = new StringBuilder(path).append(File.separator).append(id).append(".html").toString();
            System.out.println(filePath);
            FileWriter fileWriter = new FileWriter(filePath);
            //4、集结
            template.process(data,fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultBean("404","获取模板失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            return new ResultBean("404","生成静态页面失败");
        }
        return new ResultBean("200","生成静态页面成功");
    }

    /**
     * 采用多线程来提高  批量生成静态页面 的方法
     */
    @RequestMapping("batchCrateHTML")
    @ResponseBody
    public ResultBean batchCrateHTML(@RequestParam List<Long> ids) throws ExecutionException, InterruptedException {
        //这是单线程的写法  多核CPU实现   采用多线程可以进行很好的性能优化
        /*for (Long id : ids) {
            return  createHTML(id);
        }*/

        //线程池的基本要求
        //   1、初始化线程数
        //  2、最大线程数
        //  3、线程最大发呆时间
        //   4、等待队列的长度    长度太长会导致内存溢出（oom）
        //以下几个方法不做考虑
        //Executors.newSingleThreadExecutor();//队列太长，内存OOM
        //Executors.newFixedThreadPool(10);//队列太长，内存OOM
        //Executors.newCachedThreadPool();//创建线程数太多
        //单个线程的线程池：顺序执行一些任务，顺序执行效果
        //这里我们自定义的方式来创建
        //线程数怎么来确定好？可以根据用户硬件环境来考虑
        //查看当前硬件多少核

      /*  int cpus = Runtime.getRuntime().availableProcessors();
        System.out.println("我的硬件核数=" + cpus);
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                cpus, cpus * 2, 10, TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>(100));*/

      /*
         Future的应用场景
       在并发编程中，我们经常用到非阻塞的模型，在之前的多线程的三种实现中，
            不管是继承thread类还是实现runnable接口，都无法保证获取到之前的执行结果。
            通过实现Callback接口，并用Future可以来接收多线程的执行结果。
     Future表示一个可能还没有完成的异步任务的结果，针对这个结果可以添加Callback以便在任务执行成功或失败后作出相应的操作。
       */
        //创建一个集合 来得到返回的结果集
        List<Future<Long>> results = new ArrayList<>(ids.size());
        for (Long id : ids) {
            results.add(threadPool.submit(new CreateHTMLTesk(id)));
        }

        //查看每个线程的返回结果
        //将失败的添加到一个集合
        List<Long> errors = new ArrayList<>(ids.size());
        for (Future<Long> result : results) {
            Long id = result.get();
            if(id !=0){
                //谁失败了
                //先记录下来
                errors.add(id);
            }
        }
        //再将失败的做处理
        if(errors.size()>0){
            //记录到日志 create_time-->菜单
            //然后解决方式
            //1.手工模式： 直接调用接口实现
            //2\自动模式 ：定时任务（重试生成 ，不超过3次）
            return new ResultBean("500","批量生成静态页面失败！");
        }

        return new ResultBean("200","批量生成静态页面成功！");

        //考虑到生成失败  1谁失败了 2 怎么补救
        //这里可以将
        // public Boolean call() throws Exception 和{ List<Future<Boolean>> results = new ArrayList<>(ids.size());
        //返回结果改为Long  失败的为id   成功为0L
     }

    //采用内部类来调用多线程 选用Callable接口 call()方法
    private class CreateHTMLTesk implements Callable<Long>{
        private Long id;
        public CreateHTMLTesk(Long id) {
            this.id = id;
        }

        @Override
        public Long call() throws Exception {
            try {
                //1.获取模板
                Template template = configuration.getTemplate("item.ftl");
                //2、获取数据
                Map<String ,Object> data = new HashMap<>();
                data.put("product",productService.selectByPrimaryKey(id));
                //3、输出 先获取完整路径 在输出
                //FileWriter fileWriter = new FileWriter("G:\\myJavaItems\\IDEA\\v13\\v13-web\\v13-item-web\\src\\main\\resources\\static\\item.html");
                //这里我们已classPath的方式来保存路径  先获取到static的路径  这里生成的路径是运行环境中的
                String path = ResourceUtils.getURL("classpath:static").getPath();
                //拼接带文件的名的最终路径G:\\myJavaItems\\IDEA\\v13\\v13-web\\v13-item-web\\src\\main\\resources\\static\\id.html
                String filePath = new StringBuilder(path).append(File.separator).append(id).append(".html").toString();
                FileWriter fileWriter = new FileWriter(filePath);
                //4、集结
                template.process(data,fileWriter);
            } catch (IOException e) {
                e.printStackTrace();
                return id;
            } catch (TemplateException e) {
                e.printStackTrace();
                return id;
            }
            return 0L;
        }
    }
}

/**
 * 1.   Runnable接口的run()方法没有返回值，而Callable接口的call()方法可以有返回值。
   2.   Runnable接口的run()方法不可以声明抛出异常，而Callable接口的call()方法可以声明抛出异常。
 */
/*class CreateHTMLTesk2 implements Callable<String>{
    @Override
    public String call() throws Exception {
        return null;
    }
}*/

/**
 * 多线程的实现类
 */
/*class CreateHTMLTesk implements Runnable {
    private Long id;
    public CreateHTMLTesk(Long id) {
        this.id = id;
    }
    @Override
    public void run() {
        try {
            //1.获取模板
            Template template = configuration.getTemplate("item.ftl");
            //2、获取数据
            Map<String ,Object> data = new HashMap<>();
            data.put("product",productService.selectByPrimaryKey(id));
            //3、输出 先获取完整路径 在输出
            //FileWriter fileWriter = new FileWriter("G:\\myJavaItems\\IDEA\\v13\\v13-web\\v13-item-web\\src\\main\\resources\\static\\item.html");
            //这里我们已classPath的方式来保存路径  先获取到static的路径  这里生成的路径是运行环境中的
            String path = ResourceUtils.getURL("classpath:static").getPath();
            //拼接带文件的名的最终路径G:\\myJavaItems\\IDEA\\v13\\v13-web\\v13-item-web\\src\\main\\resources\\static\\id.html
            String filePath = new StringBuilder(path).append(File.separator).append(id).append(".html").toString();
            FileWriter fileWriter = new FileWriter("filePath");

            //4、集结
            template.process("data",fileWriter);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultBean("404","获取模板失败");
        } catch (TemplateException e) {
            e.printStackTrace();
            return new ResultBean("404","生成静态页面失败");
        }
        return new ResultBean("200","生成静态页面成功");
    }
}*/
