package cn.tuyucheng.taketoday.scheduled;

import cn.tuyucheng.taketoday.config.ScheduledConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(ScheduledConfig.class) // start the application context and our beans in the testing environment
public class ScheduledIntegrationTest {
    @Autowired
    private Counter counter;

    @Test
    @DisplayName("givenSleepBy100ms_whenGetInvocationCount_thenIsGreaterThanZero")
    public void givenSleepBy100Ms_whenGetInvocationCount_thenIsGreaterThanZero() throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(100L);
        assertThat(counter.getInvocationCount()).isGreaterThan(0);
    }
}