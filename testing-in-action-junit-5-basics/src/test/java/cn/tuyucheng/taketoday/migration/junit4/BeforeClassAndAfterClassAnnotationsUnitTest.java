package cn.tuyucheng.taketoday.migration.junit4;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BeforeClassAndAfterClassAnnotationsUnitTest {
    private static final Logger LOG = LoggerFactory.getLogger(BeforeClassAndAfterClassAnnotationsUnitTest.class);
    private static List<String> list;

    @BeforeClass
    public static void setup() {
        LOG.info("setup");
        list = new ArrayList<>(Arrays.asList("test1", "test2"));
    }

    @AfterClass
    public static void tearDown() {
        LOG.info("teardown");
    }

    @Test
    public void whenCheckingListSize_thenSizeEqualsToSetup() {
        LOG.info("executing test");
        assertEquals(2, list.size());
        list.add("another test");
    }

    @Test
    public void whenCheckingListSize_thenSizeEqualsToSetupSizePlusOne() {
        LOG.info("executing another test");
        assertEquals(3, list.size());
    }
}