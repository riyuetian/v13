package com.qf.tempspringbootredis.config;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 1、@controller 控制器（注入服务）
 2、@service 服务（注入dao）
 3、@repository dao（实现dao访问）
 4、@component （把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>）
 　 @Component @Service, @Controller, @Repository 注解的类，并把这些类纳入进spring容器中管理。

布隆过滤器工作流程
    1，当一个元素被加入集合时，通过K个散列函数，将这个元素映射成一个“位数组”中的K个点，并把他们都设置为1.
    2，检索时，我们只需要用同样的算法，并检查这些点是不是都是1，就可以知道集合中是否存在该元素。
        只有一个点是0，则该元素一定不存在，如果都是1，则可能存在，这里面会存在一定的误差
    3，布隆过滤器有一个误判率的概念 误判率越低，则数组越大，占用内存空间越大。
        所以，误判率的设置会影响到数组的大小及hash函数的个数 假设，根据误判率，我们生成一个10位的bit数组，及2个hash函数（f1,f2）.

 * @description: TODO  谷歌布隆过滤器
 *      1、引入相关依赖
 *
 *
 */
//@component （把普通pojo实例化到spring容器中，相当于配置文件中的 <bean id="" class=""/>）  <context:component-scan base-package=”com.*”>
@Component
public class GoogleBloomFilter {

    private BloomFilter<Long> bloomFilter;

    /**
     * 被@PostConstruct修饰的方法会在服务器加载Servle的时候运行，并且只会被服务器执行一次
     *  程序启动时  加载此方法
     */
    @PostConstruct
    public void initBloomFilter() {
        //布隆过滤器初始化操作

        //1.模拟获取到数据库数据
        List<Long> sourceList = new ArrayList<>();
        for (long i = 0; i < 100; i++) {
            sourceList.add(i);
        }

        //2、构建布隆过滤器 默认误差率为3%
        bloomFilter = BloomFilter.create(Funnels.longFunnel(),sourceList.size(),0.001);
        //3、将数据放入布隆器
        for (Long s : sourceList) {
            bloomFilter.put(s);
        }
    }

    public boolean isExists(Long id){
        return bloomFilter.mightContain(id);
    }

}