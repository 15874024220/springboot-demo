package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SpringbootDemoApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() throws InterruptedException {
        // 测试 Spring 上下文是否正常加载

        final ReentrantLock lock = new ReentrantLock();
        System.out.println("===========123===========");
        lock.lock();

        new Thread(() -> {
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
        }, "Test-1").start();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                lock.lock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }, "Test-2").start();


        new Thread(() -> {
            try {
                Thread.sleep(3000);
                lock.lock();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }, "Test-3").start();

        Thread.currentThread().sleep(3000);
        lock.unlock();

        System.out.println("===========123===========");
    }

    // 验证：所有单例Bean都在一级缓存
    @Test
    public void testAllSingletonsInFirstLevelCache() throws IllegalAccessException, NoSuchFieldException {
        ApplicationContext context = this.applicationContext;

        DefaultListableBeanFactory beanFactory =
                (DefaultListableBeanFactory) ((AbstractApplicationContext) context).getBeanFactory();

        // 获取一级缓存
        Field singletonObjectsField = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjectsField.setAccessible(true);
        Map<String, Object> singletonObjects =
                (Map<String, Object>) singletonObjectsField.get(beanFactory);

        System.out.println("一级缓存中的Bean数量：" + singletonObjects.size());
        singletonObjects.forEach((name, bean) -> {
            System.out.println(name + " → " + bean.getClass().getName());
        });

        // 输出示例：
        // userService → com.example.UserService
        // orderService → com.example.OrderService$$EnhancerBySpringCGLIB
        // dataSource → com.zaxxer.hikari.HikariDataSource
        // ... 所有单例Bean都在这里
    }
}