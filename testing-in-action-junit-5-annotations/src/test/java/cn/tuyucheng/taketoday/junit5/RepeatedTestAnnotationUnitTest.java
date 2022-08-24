package cn.tuyucheng.taketoday.junit5;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepeatedTestAnnotationUnitTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepeatedTestAnnotationUnitTest.class);

    @BeforeEach
    void beforeEachTest() {
        LOGGER.info("Before Each Test");
    }

    @AfterEach
    void afterEachTest() {
        LOGGER.info("After Each Test");
        LOGGER.info("=======================");
    }

    @RepeatedTest(3)
    void repeatedTest(TestInfo testInfo) {
        LOGGER.info("Executing repeated test");
        assertEquals(2, Math.addExact(1, 1), "1 + 1 should equal 2");
    }

    @RepeatedTest(value = 3, name = RepeatedTest.LONG_DISPLAY_NAME)
    void repeatedTestWithLongName() {
        LOGGER.info("Executing repeated test with long name");
        assertEquals(2, Math.addExact(1, 1), "1 + 1 should equal 2");
    }

    @RepeatedTest(value = 3)
    void repeatedTestWithShortName() {
        LOGGER.info("Executing repeated test with short name");
        assertEquals(2, Math.addExact(1, 1), "1 + 1 should equal 2");
    }

    @RepeatedTest(value = 3, name = "Custom name {currentRepetition}/{totalRepetitions}")
    void repeatedTestWithCustomDisplayName() {
        assertEquals(2, Math.addExact(1, 1), "1 + 1 should equal 2");
    }

    @RepeatedTest(3)
    void repeatedTestWithRepetitionInfo(RepetitionInfo repetitionInfo) {
        LOGGER.info("Repetition # {}", repetitionInfo.getCurrentRepetition());
        assertEquals(3, repetitionInfo.getTotalRepetitions());
    }
}