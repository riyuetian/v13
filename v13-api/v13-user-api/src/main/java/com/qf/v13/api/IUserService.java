package com.qf.v13.api;


import com.qf.v13.common.base.IBaseService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2520:18
 * @description: TODO
 */
public interface IUserService extends IBaseService<TUser> {


    /**
     *登陆检验接口
     */
    public ResultBean checkLogin(TUser user);


    /**
     *登录状态的认证接口,通过在cookie中的到uuid来检测
     */
    public ResultBean checkIsLogin(String uuid);

    /**
     * 注销接口  通过保存在客户端的凭证 cookie中得到的uuid实现
     */
    public ResultBean logout(String uuid);
}
