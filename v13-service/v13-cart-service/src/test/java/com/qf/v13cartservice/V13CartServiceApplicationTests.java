package com.qf.v13cartservice;

import com.qf.v13.api.ICartService;
import com.qf.v13.pojo.CartItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13CartServiceApplicationTests {

	@Autowired
	private ICartService cartService;

	private RedisTemplate<String ,Object> redisTemplate;

	@Test
	public void getTset() {
		String uuid="";
		//查单个
		CartItem cartItem = (CartItem) redisTemplate.opsForHash().get("user:cart:" + uuid, 1);
		//查所有 返回的是Map集合
		Map<Object, Object> entries = redisTemplate.opsForHash().entries("user:cart:" + uuid);
		Set<Map.Entry<Object,Object>> entries1 = entries.entrySet();
		for (Map.Entry<Object, Object> entry : entries1) {
			System.out.println(entry.getKey() + "--->" + entry.getValue());
		}

	}

}
