package com.qf.miaosha.controller;

import com.qf.miaosha.entity.TProduct;
import com.qf.miaosha.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/818:59
 * @description: TODO
 */
@Controller
@RequestMapping("product")
public class ProductController {

    @Autowired
    private IProductService productService;

    @RequestMapping("getById")
    public String getById(Long id , Model model){
        TProduct product = productService.getById(id);
        model.addAttribute("product",product);
        return "item";
    }

    @RequestMapping("sale")
    @ResponseBody
    public String sale(Long id){
        Boolean result = productService.sale(id);
        if(result){
            //秒杀成功
            return "success";
        }
        return "faild";

    }
}
