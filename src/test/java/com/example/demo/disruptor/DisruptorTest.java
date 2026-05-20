package com.example.demo.disruptor;

import com.example.demo.event.LogEvent;
import com.example.demo.util.LogEventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.demo.util.factory.LogEventFactory.getLogEventDisruptor;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class DisruptorTest {

    @Test
    public void runDisruptor() {
        //获取 Disruptor 对象
        Disruptor<LogEvent> disruptor = getLogEventDisruptor();
        //绑定处理事件的Handler对象
        disruptor.handleEventsWith(new LogEventHandler());
        disruptor.handleEventsWith(new LogEventHandler("name1"), new LogEventHandler("name2")).handleEventsWith(new LogEventHandler("name3"));
        //启动 Disruptor
        disruptor.start();
        //获取保存事件的环形数组（RingBuffer）
        RingBuffer<LogEvent> ringBuffer = disruptor.getRingBuffer();
        //发布 10w 个事件
        for (int i = 1; i <= 5; i++) {
            // 通过调用 RingBuffer 的 next() 方法获取下一个空闲事件槽的序号
            long sequence = ringBuffer.next();
            try {
                LogEvent logEvent = ringBuffer.get(sequence);
                // 初始化 Event，对其赋值
                logEvent.setMessage("这是第%d条日志消息".formatted(i));
            } finally {
                // 发布事件
                ringBuffer.publish(sequence);
            }
        }
        System.out.println(ringBuffer);
        // 关闭 Disruptor
        disruptor.shutdown();
    }
}
