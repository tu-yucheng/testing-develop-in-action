package cn.tuyucheng.taketoday.methodorders;

import org.junit.jupiter.api.AfterAll;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NameAscendingOrderOfExecutionUnitTest {
    private static final StringBuilder output = new StringBuilder();

    @AfterAll
    public static void assertOutput() {
        assertEquals(output.toString(), "abc");
    }

    @Test
    public void secondTest() {
        output.append("b");
    }

    @Test
    public void thirdTest() {
        output.append("c");
    }

    @Test
    public void firstTest() {
        output.append("a");
    }
}