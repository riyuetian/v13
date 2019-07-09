package com.qf.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.IProductTypeService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProductType;
import com.qf.v13.mapper.TProductTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商品服务-2  实现类  继承BaseserviceImpl  实现IProductTypeService接口 然后补全mapping中的CRUD实现
 *
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1719:03
 */
@Service//商品服务 注解  为什么要加注解？？
public class ProductTypeServiceImpl extends BaseServiceImpl<TProductType> implements IProductTypeService{

    @Autowired
    private TProductTypeMapper productTypeMapper;

    @Override
    public IBaseDao<TProductType> getBaseDao() {
        return productTypeMapper;
    }


}
