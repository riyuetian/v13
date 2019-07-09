package com.qf.v13itemweb;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ItemWebApplicationTests {

	//声明静态页面的对象
	@Autowired
	private Configuration configuration;

	/**
	 * 测试生成静态页面
	 */

	@Test
	public void createHTMLTest() throws IOException, TemplateException {
		//1\创建模板
		Template template = configuration.getTemplate("hello.ftl");
		//2、获取数据
		HashMap<String, Object> data = new HashMap<>();
		data.put("username","南瓜呀南瓜");
		//3、输出
		/*创建对象 并赋上数据*/
		Friends friends = new Friends(1,"盘古",new Date());
		data.put("friends",friends);

		/*输出集合*/
		List<Friends> list = new ArrayList<>();
		list.add( new Friends(1,"盘古",new Date()));
		list.add( new Friends(2,"后裔",new Date()));
		list.add( new Friends(3,"女蜗",new Date()));
		data.put("list",list);

		/*if判断*/
		data.put("money",20000);

		/*空值问题*/
		data.put("msg",null);

		FileWriter out = new FileWriter("G:\\myJavaItems\\IDEA\\v13\\v13-web\\v13-item-web\\src\\main\\resources\\static\\hello.html");
		//4、集结
		template.process(data,out);

		System.out.println("生成静态页面成功");
	}

}
