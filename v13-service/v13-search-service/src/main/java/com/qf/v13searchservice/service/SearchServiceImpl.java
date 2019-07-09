package com.qf.v13searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.v13.api.ISearchService;
import com.qf.v13.common.pojo.ResultBean;
import com.qf.v13.entity.TProduct;
import com.qf.v13.mapper.TProductMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 搜索服务同步数据-2  实现类
 *      需要引入 service -api 接口依赖
 *      引入dubbo依赖 --> 设置dubbo配置信息-->启动类注解启动dubbo
 *      映入mapper依赖-->启动类注解扫描mapper
 *      注解@Service  dubbo的service

 * @author Mr_Ma
 * Version: 1.0  2019/6/1819:04
 */
@Service
public class SearchServiceImpl implements ISearchService{

    //声明productMapper 对象 调用方法
    @Autowired
    private TProductMapper productMapper;

    //跟新索引库
    @Autowired
    private SolrClient solrClient;

    //如何抽出全量和增量的公共代码
    /**
     *重写 全量同步
     */
    @Override
    public ResultBean synAllData() {
        //1\获取数据库的数据
        List<TProduct> list = productMapper.list();
        //2、数据导入到索引库中  document对象添加
        for (TProduct product : list) {
            //创建solrInputDocument对象
            SolrInputDocument document = new SolrInputDocument();
            //赋值数据
            document.setField("id",product.getId());
            document.setField("product_name",product.getName());
            document.setField("product_price",product.getPrice());
            document.setField("product_images",product.getImages());
            document.setField("product_sale_point",product.getSalePoint());
            //添加语句
            try {
                solrClient.add(document);
            } catch (SolrServerException  | IOException e) {
                e.printStackTrace();
                return new ResultBean("404","数据添加到索引库失败");
            }
        }
        //提交
        try {
            solrClient.commit();
        } catch (SolrServerException |IOException e) {
            e.printStackTrace();
            return new ResultBean("404","数据提交到索引库失败");
        }
        return new ResultBean("200","数据同步到索引库成功");
    }

    /**
     * 重写 增量同步
     */
    @Override
    public ResultBean synDataById(Long id) {
        //根据Id获取数据
        TProduct product = productMapper.selectByPrimaryKey(id);
        //构建documnet对象
        SolrInputDocument document = new SolrInputDocument();
        //给document对象赋值
        document.setField("id",product.getId());
        document.setField("product_name",product.getName());
        document.setField("product_price",product.getPrice());
        document.setField("product_images",product.getImages());
        document.setField("product_sale_point",product.getSalePoint());
        //添加到索引库语句
        try {
            solrClient.add(document);
        } catch (SolrServerException  | IOException e) {
            e.printStackTrace();
            return new ResultBean("404","数据添加到索引库失败");
        }
        //提交
        try {
            solrClient.commit();
        } catch (SolrServerException |IOException e) {
            e.printStackTrace();
            return new ResultBean("404","数据提交到索引库失败");
        }
        return new ResultBean("200","数据同步到索引库成功");
    }

    /**
     *搜索服务-2  实现关键字搜索
     *StringUtils.isAnyEmpty(keywords)?????
     */
    @Override
    public ResultBean queryByKeywords(String keywords) {
        /*组装条件查询*/
        //创建对象
        SolrQuery solrQuery = new SolrQuery();
        //1、做判断 是否为null
        if(StringUtils.isAnyEmpty(keywords)){
            //是null 查询所有
            solrQuery.setQuery("*:*");
        }else{
            solrQuery.setQuery("product_keywords:"+keywords);
        }

        /*  新增高亮的效果
        * */
        //开启高亮
        solrQuery.setHighlight(true);
        //添加需要高亮的字段
        solrQuery.addHighlightField("product_name");
        //设置高亮效果
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        //实现分页 在这里做分页的设置
       /* solrQuery.setStart((pageIndex-1)*pageSize);
        solrQuery.setRows(pageSize);*/

        /* 执行查询 */
        try {
            QueryResponse queryResponse = solrClient.query(solrQuery);
            //获取到查询的结果集
            SolrDocumentList documents = queryResponse.getResults();
            //转化为List集合  先创建一个集合对象 ，并指定长度
            List<TProduct> list = new ArrayList<>(documents.size());

            //获取高亮信息
            Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
            //外层map  Stirng id的值  map.put<'1' ,'1的高亮信息'>
            //内层map  String 高亮的字段   List<String> 高亮效果

            //遍历results集合
            for (SolrDocument document : documents) {
                //将doucment转化为product对象
                TProduct product = new TProduct();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
               // product.setName(document.getFieldValue("product_name").toString());
                product.setImages(document.getFieldValue("product_images").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setSalePoint(document.getFieldValue("product_sale_point").toString());

                //单独为高亮做配置product_name
                //获取到记录的高亮信息
                Map<String, List<String>> idHigh = highlighting.get(document.getFieldValue("id"));
                //获取到商品名称对应的高亮信息
                List<String> productNameHigh = idHigh.get("product_name");
                //判断是否有字段对应高亮
                if(productNameHigh==null||productNameHigh.isEmpty()){
                    //没有高亮效果
                    product.setName(document.getFieldValue("product_name").toString());
                }else{
                    product.setName(productNameHigh.get(0));
                }

                list.add(product);
            }
            return new ResultBean("200",list);
        } catch (SolrServerException |IOException  e) {
            e.printStackTrace();
            return  new ResultBean("404","执行查询失败");
        }
    }
}
