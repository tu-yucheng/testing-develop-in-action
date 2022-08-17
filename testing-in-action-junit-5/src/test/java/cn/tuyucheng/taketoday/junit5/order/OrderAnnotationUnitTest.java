package cn.tuyucheng.taketoday.junit5.order;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(OrderAnnotation.class)
public class OrderAnnotationUnitTest {
    private static final StringBuilder output = new StringBuilder();

    @AfterAll
    public static void assertOutput() {
        assertEquals("abc", output.toString());
    }

    @Test
    @Order(1)
    public void firstTest() {
        output.append("a");
    }

    @Test
    @Order(2)
    public void secondTest() {
        output.append("b");
    }

    @Test
    @Order(3)
    public void thirdTest() {
        output.append("c");
    }
}