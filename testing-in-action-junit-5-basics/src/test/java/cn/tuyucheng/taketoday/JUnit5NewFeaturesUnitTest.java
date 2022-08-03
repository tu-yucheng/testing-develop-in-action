package cn.tuyucheng.taketoday;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JUnit5NewFeaturesUnitTest {
    private static final Logger log = LoggerFactory.getLogger(JUnit5NewFeaturesUnitTest.class);

    @BeforeAll
    static void setup() {
        log.info("@BeforeAll - executes once before all test method in this class");
    }

    @AfterAll
    static void done() {
        log.info("@AfterAll - executes after all test method");
    }

    @BeforeEach
    void init() {
        log.info("@BeforeEach - executes before each test method in this class");
    }

    @Test
    @DisplayName("Single test successful")
    void Single_test_successful() {
        log.info("success");
    }

    @Test
    @Disabled("Not implemented yet.")
    void testShowSomething() {

    }

    @AfterEach
    void tearDown() {
        log.info("@AfterEach - executes after each test method");
    }
}