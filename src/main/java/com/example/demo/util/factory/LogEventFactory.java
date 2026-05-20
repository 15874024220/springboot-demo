package com.example.demo.util.factory;

import com.example.demo.event.LogEvent;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class LogEventFactory implements EventFactory<LogEvent> {
    @Override
    public LogEvent newInstance() {
        return new LogEvent();
    }

    public static Disruptor<LogEvent> getLogEventDisruptor() {
        // 创建 LogEvent 的工厂
        LogEventFactory logEventFactory = new LogEventFactory();
        // Disruptor 的 RingBuffer 缓存大小
        int bufferSize = 1024 * 1024;
        // 生产者的线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            final AtomicInteger threadNum = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "LogEventThread" + " [#" + threadNum.incrementAndGet() + "]");
            }
        };
        //实例化 Disruptor
        return new Disruptor<>(
                logEventFactory,
                bufferSize,
                threadFactory,
                // 单生产者
                ProducerType.SINGLE,
                // 阻塞等待策略
                new BlockingWaitStrategy());
    }
}
