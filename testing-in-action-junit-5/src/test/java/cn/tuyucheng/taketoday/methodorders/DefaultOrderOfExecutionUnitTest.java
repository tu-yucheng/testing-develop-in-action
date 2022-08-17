package cn.tuyucheng.taketoday.methodorders;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FixMethodOrder(MethodSorters.DEFAULT)
public class DefaultOrderOfExecutionUnitTest {
    private static final StringBuilder output = new StringBuilder();

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

    @AfterAll
    public static void assertOutput() {
        assertEquals("cab", output.toString());
    }
}