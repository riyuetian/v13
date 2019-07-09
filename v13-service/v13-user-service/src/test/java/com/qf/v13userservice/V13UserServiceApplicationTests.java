package com.qf.v13userservice;

import com.qf.v13.api.IUserService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13UserServiceApplicationTests {

	@Autowired
	private IUserService userService;

	/**
	 * 登录系统的测试
	 */
	@Test
	public void contextLoads() {
		TUser user = new TUser();
		user.setUsername("fang");
		user.setPassword("99999");
		ResultBean resultBean = userService.checkLogin(user);
		System.out.println(resultBean.getStatusCode());
		System.out.println(resultBean.getData());
	}

	/**
	 * 登录状态的检测
	 */
	@Test
	public void checkIsLoginTest(){
		ResultBean resultBean = userService.checkIsLogin("66666666666");
		System.out.println(resultBean.getStatusCode());

		TUser user = (TUser) resultBean.getData();
		if (user!=null) {
			System.out.println(user.getUsername());
		}
	}

	/**
	 * 注销检测
	 */
	@Test
	public void logout(){
		ResultBean resultBean = userService.checkIsLogin("66666666666");
		System.out.println(resultBean.getStatusCode());

	}
}
