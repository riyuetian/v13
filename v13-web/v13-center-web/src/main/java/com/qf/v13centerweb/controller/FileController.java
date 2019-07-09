package com.qf.v13centerweb.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13centerweb.pojo.WangedtitorResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/1519:00
 * @description: TODO
 */
@Controller
@RequestMapping("file")
public class FileController {

    //
    @Autowired
    private FastFileStorageClient client;


    //获得访问图片ip地址
    @Value("${image.server}")
    private String imageServer;
    /**
     * 上传文件的方法
     */
    @RequestMapping("upload")
    @ResponseBody
    public ResultBean upload(MultipartFile file){
        System.out.println(file);
        //获得上传的文件名
        String filename = file.getOriginalFilename(); //**.**
        //获得文件名后缀 .**
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        try {
            //上传文件
            StorePath storePath =
                    client.uploadFile(file.getInputStream(), file.getSize(), extName, null);
            //获得文件路径 这里需要拼接ip地址得到完整的路径
            String path = new StringBuilder(imageServer).append(storePath.getFullPath()).toString();
            System.out.println(path);
            return new ResultBean("200",path);//这里需要前端实现回调行数得到path
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(file);
        return new ResultBean("404","网络不佳，上传失败");
    }

    /**
     * 富文本框上传图片的问题
     */
    @RequestMapping("multiUpload")
    @ResponseBody
    public WangedtitorResultBean multiUpload(MultipartFile[] files){
        //给data赋一个空间  对于数组  集合 已知长度 就给定长度，可省资源
        String[] data = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            String filename = files[i].getOriginalFilename();
            String entName = filename.substring(filename.lastIndexOf(".") + 1);
            try {
                StorePath storePath = client.uploadFile(files[i].getInputStream(), files[i].getSize(), entName, null);
                String path = new StringBuilder(imageServer).append(storePath.getFullPath()).toString();
                data[i]=path;
            } catch (IOException e) {
                e.printStackTrace();
                return new WangedtitorResultBean("1",null);
            }
        }
        return new WangedtitorResultBean("0",data);
    }
}
