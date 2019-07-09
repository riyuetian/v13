package com.qf.zk;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/7/417:16
 * @description: TODO
 */
public class MyWatcher implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        System.out.println("监听的节点发生了变化....");
        System.out.println("发生变化的节点:" + event.getPath());
        System.out.println("发生了什么变化：" + event.getType());


    }
}
