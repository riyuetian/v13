package com.qf.v13.api;

import com.qf.v13.common.base.IBaseService;
import com.qf.v13.common.pojo.ResultBean;

/**
 * 搜索服务同步数据 -1   定义service 接口 下一步实现
 *      不需要继承
 *      包下需引入统一返回类型ResultBean的依赖
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1818:42
 */
public interface ISearchService {

    /**
     * 全量同步  适合初始化工作
     */
    public ResultBean synAllData();

    /**
     * 增量同步
     */
    public ResultBean synDataById(Long id);

    /**
     * 收缩服务查选-1  提供让关键字搜索的方法  然后实现
     */
    public ResultBean queryByKeywords(String keywords);


}