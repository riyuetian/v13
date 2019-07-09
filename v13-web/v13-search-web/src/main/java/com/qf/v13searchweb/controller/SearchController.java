package com.qf.v13searchweb.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.v13.api.ISearchService;
import com.qf.v13.common.pojo.ResultBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 搜索工程  控制类-4
 *      先配置好依赖和配置文件
 *      导入静态资源
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1821:03
 */
@Controller
@RequestMapping("search")
public class SearchController {

    //
    @Reference
    private ISearchService searchService;
    /**
     * 搜索服务的方法
     * 这里传的是ResultBean  所以ResultBea要序列化
     */
    @RequestMapping("queryByKeywords")
    public String queryByKeywords(String keywords, Model model){
        //通过搜索得到数据
        ResultBean result = searchService.queryByKeywords(keywords);
        //保存数据 在前端显示
        model.addAttribute("result",result);
        return "list";
    }

}

