package com.qf.v13productservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.v13.api.IProductService;
import com.qf.v13.common.base.BaseServiceImpl;
import com.qf.v13.common.base.IBaseDao;
import com.qf.v13.entity.TProduct;
import com.qf.v13.entity.TProductDesc;
import com.qf.v13.mapper.TProductDescMapper;
import com.qf.v13.mapper.TProductMapper;
import com.qf.v13.pojo.TProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 *   -10  实现层
 *  继承BaseServiceImpl   实现接口IProductService  需要依赖
 *
 * @author Mr_Ma
 * Version: 1.0  2019/6/1220:22
 * @description: TODO
 */
//@Service    //加入注解，不然会报找不到bain的异常 测试的service
@Service  //发布商品服务  需要用的是dubbo类中的注解service
public class ProductServiceImpl extends BaseServiceImpl<TProduct> implements IProductService{


    //声明productMapper对象 并注解实例化
    @Autowired
    private TProductMapper productMapper;

    //声明productDeacMapper对象
    @Autowired
    private TProductDescMapper productDescMapper;

    /**
     * 重写getBaseDao 注入mapper 对象
     * 那么在base用到的方法实质就是mapper里面的了
     * @return
     */
    @Override
    public IBaseDao<TProduct> getBaseDao() {
        return productMapper;
    }

    /**
     *分页1-3  重写分页方法  同时需要配置分页的设置 新建一个commonConfig类
     */
    @Override
    public PageInfo<TProduct> page(Integer pageIndex, Integer pageSize) {
        //设置分页参数 起始位 和 每页条数
        PageHelper.startPage(pageIndex,pageSize);
        //遍历数据库 得到相应集合
        List<TProduct> list = list();
        //构建一个分页对象
        PageInfo<TProduct> pageInfo = new PageInfo<TProduct>(list, 2);
        return pageInfo;
    }

    /**
     * 添加商品3
     * 注解添加事务管理
     *  spring boot 会自动配置一个 DataSourceTransactionManager，
     *  我们只需在方法（或者类）加上 @Transactional 注解，就自动纳入 Spring 的事务管理了
     */
    @Override
    @Transactional
    public Long save(TProductVO productVO) {
        //1、保持商品的基本信息
        TProduct product = productVO.getProduct();
        product.setFlag(true);//??
        //在添加的时候同时主键回填 在mapper中添加
        int count = productMapper.insert(product);
        System.out.println(count);
        //2、保存商品的描述信息 根据当前商品的ID来添加
        String productDesc = productVO.getProductDesc();
        TProductDesc desc = new TProductDesc();
        desc.setProductDesc(productDesc);
        desc.setProductId(product.getId());//这里选择product.getId()就需要先主键回填
        productDescMapper.insert(desc);
        //3、返回id
        return product.getId();
    }

    /**
     * 删除单个1
     * 因为我们需要的是逻辑删除  所有需要重写方法
     * 返回值为受影响的行数
     *
     * 一：逻辑删除
         逻辑删除的本质是修改操作，所谓的逻辑删除其实并不是真正的删除，
         而是在表中将对应的是否删除标识（is_delete）或者说是状态字段（status）做修改操作。
         比如0是未删除，1是删除。在逻辑上数据是被删除的，但数据本身依然存在库中。
         对应的SQL语句：update 表名 set is_delete = 1 where id = 1；语句表示，在该表中将id为1的信息进行逻辑删除，
         那么客户端进行查询id为1的信息，服务器就不会提供信息。倘若想继续为客户端提供该信息，可将 is_delete 更改为 0 。
     * 二：物理删除
         物理删除就是真正的从数据库中做删除操作了。
         对应的SQL语句：delete from 表名 where 条件；执行该语句，即为将数据库中该信息进行彻底删除，无法恢复。
     */
    @Override
    public int deleteByPrimaryKey(Long id) {
        //创建对象
        TProduct product =new TProduct();
        product.setId(id);
        product.setFlag(false);
        int count = productMapper.updateByPrimaryKeySelective(product);
        return count;
    }

    /**
     *批量删除
     */
    @Override
    public Long batchDel(List<Long> ids) {
        //方式一  与数据库的交互次数过多，效率低
       /* for (Long id:ids){
            productMapper.deleteByPrimaryKey(id);
        }*/
       //方式二  update t_product set flag=0 where id in(1,2,3)

        return productMapper.batchUpdateFlag(ids);
    }
}
