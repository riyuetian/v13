package com.qf.v13searchservice;

import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import com.qf.v13searchservice.service.SearchServiceImpl;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class V13SearchServiceApplicationTests {

	@Test
	public void contextLoads() {
	}

	//声明一个solrClient对象
	@Autowired
	private SolrClient solrClient;

	//同步数据库到索引库
	@Autowired
	private SearchServiceImpl searchService;

	/**
	 * 搜索查询的测试
	 */
	@Test
	public void queryByKeywordsTest(){
		ResultBean<List<TProduct>> resultBean = searchService.queryByKeywords("华荣耀");
		List<TProduct> products = resultBean.getData();
		for (TProduct product : products) {
			System.out.println(product.getId() + "->" + product.getName());
		}
	}

	/**
	 *同步数据库到索引库的测试
	 */
	@Test
	public void synAllData(){
		ResultBean resultBean = searchService.synAllData();
		System.out.println(resultBean.getStatusCode());
	}

	/**
	 * 测试添加的方法
	 */
	@Test
	public void addTest() throws IOException, SolrServerException {
		//sole 是以 document为基本单位
		SolrInputDocument document = new SolrInputDocument();
		//必须添加id 需要唯一标识 不然回报缺少id错误
		// Document is missing mandatory uniqueKey field: id
		//没有这个id就是新增 有这个id就是覆盖 修改
		document.setField("id",8);
		document.setField("product_name","大台式");
		document.setField("product_price","9");
		document.setField("product_images","无");
		document.setField("product_sale_point","就是大");
		//2 保存document到solr索引库中
		//solrClient.add(document);

		solrClient.add("collection2",document);

		//3 提交
		//solrClient.commit();
		solrClient.commit("collrction2");

		System.out.println("保存成功！");
	}

	/**
	 * 做一个查询的测试
	 */
	@Test
	public void queryTest() throws IOException, SolrServerException {
		//组装查询条件  先创建一个对象
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("product_name:华为旗舰大龙虾");//查询的结果为两条
		//执行查询
		QueryResponse response = solrClient.query(solrQuery);
		//得到查询的集合
		SolrDocumentList solrDocuments = response.getResults();
		//遍历集合
		for (SolrDocument document : solrDocuments) {
			System.out.println("id:"+document.get("id")
					+"\tname:"+document.get("product_name"));
		}
	}

	/**
	 * 删除的测试 根据id 唯一标识
	 */
	@Test
	public void deleteTest() throws IOException, SolrServerException {
		//删除语句
		solrClient.deleteById("1");
		//提交
		solrClient.commit();
	}

	/**
	 * 条件删除
	 * 不是精确删除  而是关键词删除  关键词
	 */
	@Test
	public void delQueryTest() throws IOException, SolrServerException {
		solrClient.deleteByQuery("product_name:大");
		solrClient.commit();
	}

	/**
	 * 清空所有
	 */
	@Test
	public void delAllTest() throws IOException, SolrServerException {
		solrClient.deleteByQuery("*:*");
		solrClient.commit();
	}

}
