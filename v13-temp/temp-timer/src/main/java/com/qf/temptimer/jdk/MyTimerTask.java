package com.qf.temptimer.jdk;

import java.util.Date;
import java.util.TimerTask;

/**
 * @author Mr_Ma
 * Version: 1.0  2019/6/2621:10
 * @description: TODO
 */
public class MyTimerTask extends TimerTask{

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+"--->"+new Date());
    }
}
