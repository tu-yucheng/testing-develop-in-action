package cn.tuyucheng.taketoday.junit5.order;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(CustomOrder.class)
public class CustomOrderUnitTest {
    private static final StringBuilder output = new StringBuilder();

    @AfterAll
    public static void assertOutput() {
        assertEquals(output.toString(), "AaB");
    }

    @Test
    public void myATest() {
        output.append("A");
    }

    @Test
    public void myBTest() {
        output.append("B");
    }

    @Test
    public void myaTest() {
        output.append("a");
    }
}