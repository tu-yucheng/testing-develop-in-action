package cn.tuyucheng.taketoday.junit5.order;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultOrderUnitTest {

    private static final StringBuilder output = new StringBuilder();

    @AfterAll
    public static void assertOutput() {
        assertEquals(output.toString(), "ABC");
    }

    @Test
    @DisplayName("Test A")
    public void myATest() {
        output.append("A");
    }

    @Test
    @DisplayName("Test B")
    public void myBTest() {
        output.append("B");
    }

    @Test
    @DisplayName("Test C")
    public void myCTest() {
        output.append("C");
    }
}