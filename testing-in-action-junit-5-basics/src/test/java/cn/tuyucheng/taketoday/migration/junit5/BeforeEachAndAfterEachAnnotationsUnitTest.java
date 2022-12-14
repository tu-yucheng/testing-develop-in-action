package cn.tuyucheng.taketoday.migration.junit5;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BeforeEachAndAfterEachAnnotationsUnitTest {
    private static final Logger LOG = LoggerFactory.getLogger(BeforeEachAndAfterEachAnnotationsUnitTest.class);
    private List<String> list;

    @BeforeEach
    public void init() {
        LOG.info("startup");
        list = new ArrayList<>(Arrays.asList("test1", "test2"));
    }

    @AfterEach
    public void teardown() {
        LOG.info("teardown");
        list.clear();
    }

    @Test
    public void whenCheckingListSize_ThenSizeEqualsToInit() {
        LOG.info("executing test");
        assertEquals(2, list.size());
        list.add("another test");
    }

    @Test
    public void whenCheckingListSizeAgain_ThenSizeEqualsToInit() {
        LOG.info("executing another test");
        assertEquals(2, list.size());
        list.add("yet another test");
    }
}