package cn.tuyucheng.taketoday.migration.junit5;

import cn.tuyucheng.taketoday.migration.junit5.extensions.TraceUnitExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(TraceUnitExtension.class)
public class RuleExampleUnitTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleExampleUnitTest.class);

    @Test
    public void whenTracingTests() {
        LOGGER.info("This is my test");
    }
}