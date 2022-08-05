package cn.tuyucheng.taketoday.migration.junit5;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;

@Tag("junit5")
@Tag("annotations")
@RunWith(JUnitPlatform.class)
public class AnnotationTestExampleUnitTest {

    @Test
    @DisplayName("shouldRaiseAnException")
    void shouldRaiseAnException() {
        assertThrows(Exception.class, () -> {
            throw new Exception("This is my expected exception");
        });
    }

    @Test
    @Disabled
    public void shouldFailBecauseTimeout() {
        assertTimeout(Duration.ofMillis(1), () -> Thread.sleep(10));
    }
}