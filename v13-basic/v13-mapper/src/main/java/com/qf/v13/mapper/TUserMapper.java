package com.qf.v13.mapper;

import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TUser;

public interface TUserMapper extends IBaseDao<TUser>{

    TUser selectByUsername(String username);


}