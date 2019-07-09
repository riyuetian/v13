package com.qf.temptimer.jdk;

import java.util.Date;
import java.util.Timer;


/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2621:27
 * @description: TODO
 *
 * JDk 提供的定时任务
 */
public class Main {

    public static void main(String[] args) {
        //
        Timer timer = new Timer();
        MyTimerTask task = new MyTimerTask();
        System.out.println("main:" + new Date());

        /// delay 为long类型：从task  过delay毫秒执行一次
        //timer.schedule(task,1000);

        // delay为long,period为long：从task 起过delay毫秒以后，每隔period毫秒执行一次。
        timer.schedule(task,1000,1000);

        // time为Date类型：在指定时间执行一次。
        //timer.schedule(task, time);

        // firstTime为Date类型,period为long
        // 从firstTime时刻开始，每隔period毫秒执行一次。
        //timer.schedule(task, firstTime, period);

        //每个月1号8点 发工资  这种需求不完全固定的采用JDK定时器 很难完成 有局性性
        //可以采用spring自带的定时器  支持cron表达式



    }
}
