package com.qf.v13.api;

import com.github.pagehelper.PageInfo;
import com.qf.v13.common.base.IBaseService;
import com.qf.v13.entity.TProduct;
import com.qf.v13.pojo.TProductVO;

import java.util.List;

/**
 *  -8   开发商品服务的api及实现
 *  继承IBaseService   就需要引入common依赖
 * @author Mr_Ma
 * Version: 1.0  2019/6/1219:56
 * @description: TODO
 */
public interface IProductService extends IBaseService<TProduct>{
    //用来单独单独扩展特殊的方法

    /**
     * 分页1-2  添加分页的方法
     */
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize);

    /**
     * 添加的方法2   传TProductVO 对象  返回新增的id
     */
    public Long save(TProductVO productVO);

    /**
     *批量删除1 添加批量删除接口 id集合
     */
    public Long batchDel(List<Long> ids);


}
