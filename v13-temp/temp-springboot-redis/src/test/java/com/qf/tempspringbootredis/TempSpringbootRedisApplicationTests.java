package com.qf.tempspringbootredis;

import com.qf.tempspringbootredis.config.GoogleBloomFilter;
import com.qf.tempspringbootredis.entity.Home;
import com.qf.tempspringbootredis.entity.ProductType;
import com.qf.tempspringbootredis.entity.Student;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * springboot 整合redis
 * 	注意依赖 新建initializr  选择Access+Driver
 *   1  yml 文件
 *
 *  注解@Autowried  与 注解@Resource的区别
 *  	@Resource 的作用相当于@Autowired，只不过@Autowired按byType自动注入，
 *  	而@Resource默认按 byName自动注入罢了。@Resource有两个属性是比较重要的，
 *  		分是name和type，Spring将@Resource注解的name属性解析为bean的名字，而type属性则解析为bean的类型。
 *  		所以如果使用name属性，则使用byName的自动注入策略，而使用type属性时则使用byType自动注入策略。
 *  		如果既不指定name也不指定type属性，这时将通过反射机制使用byName自动注入策略。
　　		@Resource 装配顺序
	　　1. 如果同时指定了name和type，则从Spring上下文中找到唯一匹配的bean进行装配，找不到则抛出异常
	　　2. 如果指定了name，则从上下文中查找名称（id）匹配的bean进行装配，找不到则抛出异常
	　　3. 如果指定了type，则从上下文中找到类型匹配的唯一bean进行装配，找不到或者找到多个，都会抛出异常
	　　4. 如果既没有指定name，又没有指定type，则自动按照byName方式进行装配；如果没有匹配，则回退为一个原始类型进行匹配，如果匹配则自动装配；
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TempSpringbootRedisApplicationTests {

	//声明一个redisTemplate 注入实例化
	//@Autowired
	//private RedisTemplate redisTemplate;

	//当序列化由公共类提供后 这里注解需要改变
	@Resource(name = "redisTemplate1")
	private RedisTemplate<String ,Object> redisTemplate;

	@Resource(name = "redisTemplate2")
	private RedisTemplate<String ,Object> redisTemplate2;

	@Autowired
	private GoogleBloomFilter googleBloomFilter;

	@Test
	public void contextLoads() {
		//修改序列化方法 一  方法二调用公共类类的对象即可 更具有全局性 扩展性
		redisTemplate.setKeySerializer(new StringRedisSerializer());

		redisTemplate.opsForValue().set("home","nangua");
		System.out.println(redisTemplate.opsForValue().get("home"));//nangua

	}


	/**
	 * 但需要获得对象时 需要采用默认的序列化方式 不然会报错
	 */
	@Test
	public void studetTest() {
		//修改序列化方法 一  方法二调用公共类类的对象即可 更具有全局性 扩展性
		redisTemplate2.setKeySerializer(new StringRedisSerializer());

		redisTemplate2.opsForValue().set("student1",new Student("fangLOVE"));
		System.out.println(redisTemplate2.opsForValue().get("student1"));//Student{name='fangLOVE'}
	}


	/**
	 * 采用redis缓存来优化系统性能  减少与数据库的交互
	 *   我们可以将数据保存在redis中用于提供查询   改变key的序列化方式
	 *
	 *  分布式锁:
	 *
	 */
	@Test
	public void cacheTest(){
		//1、先看缓存 没有再看数据库 ，看完数据库，同时在将数据保存到缓存
		String key = "product:typesSSS";
		List<ProductType> types = (List<ProductType>) redisTemplate2.opsForValue().get(key);
		if(types==null){
			//只有获得锁的线程，才能去查询数据库
			//采用redis的分布式锁来实现

			//解决无锁情况： value值随机
			String uuid = UUID.randomUUID().toString();

			//setIfAbsent(k,v)如果键不存在则新增,存在则不改变已经有的值。
		//	Boolean lock = redisTemplate2.opsForValue().setIfAbsent("lock", uuid);

			//注意 原子性操作  先增lock 与设置有效期 应该视为不可分裂，避免因为中间出错 导致先增了没有设置有效期 而形成死锁
			//原子性 这里采用  LUR脚本   来实现
			Boolean lock = redisTemplate2.opsForValue().setIfAbsent("lock", uuid,60,TimeUnit.SECONDS);

//			System.out.println("lock:"+lock);
//			Boolean lock1 = redisTemplate2.opsForValue().setIfAbsent("lock", uuid);
//			System.out.println("lock1:"+lock1);
			if (lock) {
				//给锁设置时间  避免造成死锁  时间不能大 线程死锁是指由于两个或者多个线程互相持有对方所需要的资源，导致这些线程处于等待状态，无法前往执行
		//		redisTemplate2.expire("lock",6,TimeUnit.SECONDS);

				//获取到了锁
				//缓存中没有数据 去数据库查找 查找返回结果放入缓存
				types = new ArrayList<>();
				types.add(new ProductType(1,"海贼王"));
				types.add(new ProductType(2,"火影忍者"));

				//保存到redis
				redisTemplate2.opsForValue().set(key,types);
				System.out.println("添加到缓存");

				//获得当前锁的value
				String  currentUuid = (String) redisTemplate2.opsForValue().get("lock");
				//判断是否还是原先自己的那一把锁  这里可能还没有执行过来 ，锁就过期了。
				if (currentUuid.equals(uuid)) {
					//释放锁
					redisTemplate2.delete("lock");
				}
			}else{
				//没有获取到锁 等待一段时间在尝试获取锁
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				cacheTest();
			}
		}else {
			for (ProductType type : types) {
				System.out.println(type.getId()+"-->"+type.getName());
			}
			System.out.println("从缓存中获取到数据");
		}
	}


	/**
	 * 多线程异步处理
	 * 	存在的问题:
	 * 	1、缓存跟新策略：
	 * 		a、查询时，先从缓存中获取，获取不到，则从数据库获取，并将查询结果保存到缓存
	 		b、更新时，先更新数据库的数据，更新成功后，删除缓存的数据
	 	2、缓存穿透：
	 		问题：通过故意访问不存在的数据，从而实现了缓存穿透攻击，导致造成很多的请求打到DB数据库上，最终将DB整挂。
	 		解决：
	 			方案一：保存null对象到缓存   占用空间内存大，可能会导致缓存雪崩
	 			方案二：采用集合保存已经存在的对象，如果集合查不到该key，则说明数据库也不存在，不需要走数据库，直接返回即可。占用空间内存大，效率低
	 			方案三：布隆过滤器
	 				a:Google布隆过滤器
	 				b:redis布隆过滤器
	 	3、缓存雪崩
	 		问题：
	 		解决：
	 */
	@Test
	public void multiThreadText() throws InterruptedException {
		/*1、创建线程池 4中对象 但我们采用底层 */
//		1、newSingleThreadExecutor 创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行。
//		2、newFixedThreadPool 创建一个定长线程池，可控制线程最大并发数，超出的线程会在队列中等待。
//		3、newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
//		4、newScheduledThreadPool 创建一个定长线程池，支持定时及周期性任务执行。
//		FixedThreadPool 和 SingleThreadPool，允许的请求队列长度为 Integer.MAX_VALUE，可能会堆积大量的请求，从而导致 OOM。
//		CachedThreadPool 和 ScheduledThreadPool，允许的创建线程数量为 Integer.MAX_VALUE，可能会创建大量的线程，从而导致 OOM
//		所以我们采用底层ThreadPoolExecutor 方式去创建  即为自定义方式
		ThreadPoolExecutor pool = new ThreadPoolExecutor(
				10,20,100,
				TimeUnit.MILLISECONDS,new LinkedBlockingDeque<>(100));

		//2、批量提交任务给线程池执行
		for (int i = 0; i <100 ; i++) {
			pool.submit(new Runnable() {
				@Override
				public void run() {
					cacheTest();
				}
			});
			
		}

		Thread.sleep(100000);
	}


	/**
	 * lua语言测试
	 */
	@Test
	public void lockScriptTest(){
		//1.创建一个可以执行lua脚本的执行对象
		DefaultRedisScript<Boolean> lockScript = new DefaultRedisScript<>();
		//2.获取要执行的脚本
		lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("set.lua")));
		//3.设置返回类型
		lockScript.setResultType(Boolean.class);
//		//4.封装参数
//		List<String> keyList = new ArrayList<>();
//		keyList.add("lock");
//		String uuid = UUID.randomUUID().toString();
//		keyList.add(uuid);
		//5.执行脚本
		Boolean result = redisTemplate.execute(lockScript, null);
//		System.out.println(result);
//
//		Long expire = redisTemplate.getExpire("lock");
		System.out.println(result);
	}
	/**
	 * lua语言测试
	 */
	@Test
	public void lockScriptTest2(){
		//1.创建一个可以执行lua脚本的执行对象
		DefaultRedisScript<Boolean> lockScript = new DefaultRedisScript<>();
		//2.获取要执行的脚本
		lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lock.lua")));
		//3.设置返回类型
		lockScript.setResultType(Boolean.class);

		//4、设置参数
		List<String> params =new ArrayList<>();
		params.add("lock");
		params.add(UUID.randomUUID().toString());
		params.add("60");
		//5.执行脚本
		Boolean result = redisTemplate.execute(lockScript, params);
		//6判断
		if (result) {
			Long expire = redisTemplate.getExpire("lock");
			System.out.println(expire);
		}else {
			System.out.println(result);
		}
	}


	/**
	 * 缓存穿透攻击 方案一 ：查不到 保存为空对象
	 * 		但100000个进程查询  就容易导致内存撑爆
	 *     解决：采用布隆过滤器
	 */
	@Test
	public void cachePenetrateTest(){
		String key ="1";
		Home home = (Home) redisTemplate2.opsForValue().get(key);
		if (home==null) {
			//分布式锁
			System.out.println("查询数据库");

			//数据库根本就不存在id为1的记录
			//没有东西可以放入缓存中

			//缓存保留空对象
			redisTemplate2.opsForValue().set(key,new Home());
			//设置有效期
			redisTemplate2.expire(key,60,TimeUnit.SECONDS);
		}else {
			//缓存中查询
			System.out.println("缓存获取不到该对象。。");
			System.out.println(home);
		}
	}

	/**
	 * 缓存穿透攻击 方案二:谷歌布隆过滤器的测试
	 */
	@Test
	public void testGoogleBloomFilterTest(){
		int count = 0;
		for (long i = 1000; i < 4000; i++) {
			boolean exists = googleBloomFilter.isExists(i);
			if(exists){
				count++;
			}
		}
		System.out.println(count);
	}

	/**
	 * 缓存穿透攻击 方案三:redis布隆过滤器的测试
	 * 	1、先redis安装配置好 布隆过滤器
	 * 	2、编写lua小脚本 添加add.lua 查选exists.lua
	 *  3、调用脚本执行程序
	 */
	/**
	 * 添加
	 */
	@Test
	public void bloomAddTest(){
		//1.创建一个可以执行lua脚本的执行对象
		DefaultRedisScript<Boolean> lockScript = new DefaultRedisScript<>();
		//2.获取要执行的脚本
		lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("add.lua")));
		//3.设置返回类型
		lockScript.setResultType(Boolean.class);
		//4.封装参数
		List<String> keyList = new ArrayList<>();
		keyList.add("myBloomFilter2");
		keyList.add("java");
		//5.执行脚本
		Boolean result = (Boolean) redisTemplate.execute(lockScript, keyList);
		System.out.println(result);
	}

	/**
	 * 查选
	 */
	@Test
	public void bloomExistsTest(){
		//1.创建一个可以执行lua脚本的执行对象
		DefaultRedisScript<Boolean> lockScript = new DefaultRedisScript<>();
		//2.获取要执行的脚本
		lockScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("exists.lua")));
		//3.设置返回类型
		lockScript.setResultType(Boolean.class);
		//4.封装参数
		List<String> keyList = new ArrayList<>();
		keyList.add("myBloomFilter2");
		keyList.add("javaee");
		//5.执行脚本
		Boolean result = (Boolean) redisTemplate.execute(lockScript, keyList);
		System.out.println(result);
	}

}
