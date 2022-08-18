package cn.tuyucheng.taketoday.extensions.testwatcher;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(TestResultLoggerExtension.class)
class TestWatcherAPIUnitTest {

    @Test
    @DisplayName("givenFalseIsTrue_whenTestAbortedThenCaptureResult")
    void givenFalseIsTrue_whenTestAbortedThenCaptureResult() {
        assumeTrue(false);
    }

    @Disabled
    @Test
    void givenTrueIsTrue_whenTestDisabledThenCaptureResult() {
        assertTrue(true);
    }

    @Test
    @DisplayName("givenTrueIsTrue_whenTestAbortedThenCaptureResult")
    void givenTrueIsTrue_whenTestAbortedThenCaptureResult() {
        assumeTrue(true);
    }

    @Disabled("This test is disabled")
    @Test
    void givenFailure_whenTestDisabledWithReason_ThenCaptureResult() {
        fail("Not yet implemented");
    }
}