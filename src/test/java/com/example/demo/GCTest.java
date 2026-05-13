package com.example.demo;

import java.util.concurrent.locks.ReentrantLock;

public class GCTest {
    public static void main(String[] args) {
//        byte[] allocation1, allocation2;
//        allocation1 = new byte[309000*1024];

        try {
            threadTest();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 该方法造成死锁：父任务等待子任务执行完成，而子任务等待父任务释放线程池资源，这也就造成了 "死锁" 。
     * @throws InterruptedException
     */
    public static void threadTest() throws InterruptedException {
        // 测试 Spring 上下文是否正常加载

        final ReentrantLock lock = new ReentrantLock();
        System.out.println("===========123===========");
        System.out.println(Thread.currentThread().getName());
        lock.lock();
        lock.lock();

        Thread t1 =  new Thread(() -> {
            try {
                Thread.sleep(1000);
                lock.lock();
                System.out.println(Thread.currentThread().getName());
                System.out.println("Test".indexOf(Thread.currentThread().getName()));
                System.out.println(Thread.currentThread().getName().contains("Test"));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }, "Test-1");
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                lock.lock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }, "Test-2");
        t2.start();


        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(3000);
                lock.lock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }, "Test-3");
        t3.start();


        t1.join();
        t2.join();
        t3.join();
        System.out.println("========所有线程执行完成========");

        lock.unlock();
        lock.unlock();

    }
}