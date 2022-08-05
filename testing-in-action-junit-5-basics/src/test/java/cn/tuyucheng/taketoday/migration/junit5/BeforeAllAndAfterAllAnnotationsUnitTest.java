package cn.tuyucheng.taketoday.migration.junit5;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeforeAllAndAfterAllAnnotationsUnitTest {
    private static final Logger LOG = LoggerFactory.getLogger(BeforeAllAndAfterAllAnnotationsUnitTest.class);

    @BeforeAll
    public static void setup() {
        LOG.info("startup - creating DB connection");
    }

    @AfterAll
    public static void tearDown() {
        LOG.info("teardown - closing DB connection");
    }

    @Test
    @DisplayName("simpleTest")
    void simpleTest() {
        LOG.info("simple test");
    }

    @Test
    @DisplayName("anotherSimpleTest")
    void anotherSimpleTest() {
        LOG.info("another simple test");
    }
}