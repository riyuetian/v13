package com.qf.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/411:47
 * @description: TODO
 */
public class ZkTest {

    /**
     * 连接
     * @throws IOException
     */
    @Test
    public void connectionTest() throws IOException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.199.129:2181",100,null);
        System.out.println(zooKeeper);
    }

    /**
     * 创建一个新的节点  会判断是否已有该节点，有就会报错
     * @throws IOException
     */
    @Test
    public void createNodeTest() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.199.129:2181",100,null);

        String result = zooKeeper.create("/myfang", "999".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println(result);
    }

    /**
     *  获取节点的状态信息
     */
    @Test
    public void getStatTest() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.199.129:2181",100,null);
        Stat stat = zooKeeper.exists("/myfang", false);
        System.out.println(stat.getCzxid());
        System.out.println(stat.getCtime());
        System.out.println(stat.getDataLength());
    }

    /**
     *  获取节点数据内容
     * @throws IOException
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public  void getDataTest() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.199.129:2181",100,null);
        byte[] data = zooKeeper.getData("/myfang", false, null);
        System.out.println(new String(data));

    }

    /**
     * 修改节点数据  注意点：必须要有这个节点
     */
    @Test
    public void setDataTest() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.199.129:2181",100,null);
        //version需与节点数据中的 Dataversion 相同
        Stat stat = zooKeeper.setData("/myfang", "5201314".getBytes(), 2);
        System.out.println(stat.toString());
        System.out.println(stat.getCzxid());
    }

    /**
     *  删除节点
     * @throws IOException
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void delTEst() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.199.129:2181",100,null);
        //version 需为-1
        zooKeeper.delete("/myfang",-1);
    }


    /**
     * exists 监控测试  一次性触发
     *   被监视的Znode创建、删除或数据更新时被触发。
     */
    @Test
    public void existsWatchTest() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.199.129:2181",100,null);
        zooKeeper.exists("/newFang", new MyWatcher());

        Thread.sleep(900000);
    }

    /**
     * getData监控
     *  在被监视的Znode删除或数据更新时被触发。在被创建时不能被触发，因为只有Znode一定存在，getData操作才会成功。
     */
    @Test
    public void getDataWatchTest() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.199.129:2181",100,null);
        zooKeeper.getData("/newFang", new MyWatcher(),null);

        Thread.sleep(900000);
    }


    /**
     *getChildren
     *  在被监视的Znode的子节点创建或删除，或是这个Znode自身被删除时被触发。
     *  可以通过查看watch事件类型来区分是Znode，还是他的子节点被删除：
     *      NodeDelete表示Znode被删除，NodeDeletedChanged表示子节点被删除。
     */
    @Test
    public void getChildrenWatchTest() throws IOException, KeeperException, InterruptedException {
        ZooKeeper zooKeeper = new ZooKeeper("192.168.199.129:2181",100,null);
        zooKeeper.getChildren("/newFang", new MyWatcher());

        Thread.sleep(900000);
    }

}
