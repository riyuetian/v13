package com.qf.v13productservice;

import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProductService;
import com.qf.v13.api.IProductTypeService;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductType;
import com.qf.v13.pojo.TProductVO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 *  -11  测试类  这里测试调同本地服务
 *
 * 这里会有5个常见错误
 * 	1  创建数据源失败   没有连接数据库  配置yml即可
 *	2  sevrise至少需要有一个 找不到service   需要在ProductServiceImpl类上加注解@service
 *	3  找不到mapper   需要在启动类添加注解指定mapper路径
 *  4  找不到要调用的方法。认为没有  原因是采用了分层   需要在mapper包下的 pom.xml引入 所有mapper
 *  5  数据库配置时区问题   ?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class V13ProductServiceApplicationTests {

	//声明一个service对象。调用方式测试
	@Autowired
	private IProductService productService;

	/*测试商品服务*/
	//申明service对象
	@Autowired
	private IProductTypeService productTypeService;


	/*//测试阿里巴巴连接池
	private DataSource dataSource;
	//测试阿里巴巴连接池
	@Test
	public  void textalibabaSQL() throws SQLException {
		System.out.println(dataSource.getConnection());
	}*/
	@Test
	public void contextLoads() {
		TProduct product = productService.selectByPrimaryKey(2L);
		System.out.println(product.getName());
	}

	/**
	 * 测试查询集合
	 */
	@Test
	public void testList(){
		List<TProduct> list = productService.list();
		Assert.assertEquals(list.size(),1);
	}

	/**
	 * 测试分页
	 */
	@Test
	public void testPageList(){
		PageInfo<TProduct> pageInfo = productService.page(1,2);
		System.out.println(pageInfo.getList().size());
	}

	/**
	 * 测试添加
	 */
	@Test
	public void testAdd(){
		TProduct product = new TProduct();
		product.setName("1");
		product.setPrice(999L);
		product.setSalePrice(888L);
		product.setSalePoint("11111");
		product.setImages("wu");
		product.setFlag(true);
		product.setTypeId(1L);
		TProductVO productVO = new TProductVO();
		productVO.setProduct(product);
		productVO.setProductDesc("1111111111111111");
		Long count = productService.save(productVO);
		System.out.println(count);
	}



	/**
	 * 商品服务  测试方法
	 */
	@Test
	public void testType(){
		List<TProductType> list = productTypeService.list();
		System.out.println(list.size());
	}





}
