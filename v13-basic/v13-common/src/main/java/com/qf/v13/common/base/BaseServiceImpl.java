package com.qf.v13.common.base;

import java.util.List;

/**
 *  -6  事务逻辑 实现类
 * 设置为抽象类
 *     原由：共公的实现类，谁来重写getBaseDao()方法
 * @author Mr_Ma
 * Version: 1.0  2019/6/1219:36
 * @description: TODO
 */
public abstract class BaseServiceImpl<T> implements IBaseService<T> {

    //声明一个抽象的方法 用于得到getBaseDao()
    //子类实现这个方法时。注入具体的mapper
    
    public abstract IBaseDao<T> getBaseDao();

    @Override
    public int deleteByPrimaryKey(Long id) {
        return getBaseDao().deleteByPrimaryKey(id);
    }

    @Override
    public int insert(T record) {
        return getBaseDao().insert(record);
    }

    @Override
    public int insertSelective(T record) {
        return getBaseDao().insertSelective(record);
    }

    @Override
    public T selectByPrimaryKey(Long id) {
        return getBaseDao().selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(T record) {
        return getBaseDao().updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(T record) {
        return getBaseDao().updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(T record) {
        return getBaseDao().updateByPrimaryKey(record);
    }

    @Override
    public List<T> list() {
        return getBaseDao().list();
    }
}
