package com.example.demo.util;

import com.example.demo.event.LogEvent;
import com.lmax.disruptor.EventHandler;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LogEventHandler implements EventHandler<LogEvent> {
    private String name = "Default";

    @Override
    public void onEvent(LogEvent logEvent, long sequence, boolean endOfBatch) throws Exception {
        System.out.println(logEvent.getMessage() + "[name]=" + this.name);
    }
}
