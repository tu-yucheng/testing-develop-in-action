package cn.tuyucheng.taketoday.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Counter {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Scheduled(fixedDelay = 5)
    public void scheduled() {
        this.counter.incrementAndGet();
    }

    public int getInvocationCount() {
        return this.counter.get();
    }
}