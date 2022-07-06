package com.lxs.legou.seckill.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MultiThreadingCreateOrder {

    /**
     * 多线程测试
     */
    @Async
    public void createOrder() {
        try {
            System.out.println("准备执⾏....");
            Thread.sleep(20000);
            System.out.println("开始执⾏....");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

