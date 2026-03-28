package com.example.demo;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureTest {

    public static void main(String[] args) throws InterruptedException {
        // T1
        CompletableFuture<Void> futureT1 = CompletableFuture.runAsync(() -> {
            System.out.println("T1 is executing. Current time：" + new Date(System.currentTimeMillis()));
            // 模拟耗时操作
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        // T2
        CompletableFuture<Void> futureT2 = CompletableFuture.runAsync(() -> {
            System.out.println("T2 is executing. Current time：" + new Date(System.currentTimeMillis()));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        // 使用allOf()方法合并T1和T2的CompletableFuture，等待它们都完成
        CompletableFuture<Void> bothCompleted = CompletableFuture.allOf(futureT1, futureT2);
        // 当T1和T2都完成后，执行T3
        bothCompleted.thenRunAsync(() -> System.out.println("T3 is executing after T1 and T2 have completed.Current time：" + new Date(System.currentTimeMillis())));
        // 等待所有任务完成，验证效果
        Thread.sleep(3000);
    }
}
