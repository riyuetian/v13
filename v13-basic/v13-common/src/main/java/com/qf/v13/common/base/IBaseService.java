package com.qf.v13.common.base;

import java.util.List;

/**
 * @description: TODO
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1219:35
 */
public interface IBaseService<T> {

    int deleteByPrimaryKey(Long id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKeyWithBLOBs(T record);

    int updateByPrimaryKey(T record);

    public List<T> list();
}
