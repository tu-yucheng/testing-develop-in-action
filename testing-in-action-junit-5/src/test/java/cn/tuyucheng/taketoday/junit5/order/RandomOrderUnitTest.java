package cn.tuyucheng.taketoday.junit5.order;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.Random.class)
public class RandomOrderUnitTest {

    private static final StringBuilder output = new StringBuilder();

    @AfterAll
    public static void assertOutput() {
        assertEquals(output.toString(), "ACB");
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
    public void myCTest() {
        output.append("C");
    }
}