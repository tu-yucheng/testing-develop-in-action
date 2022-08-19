package cn.tuyucheng.taketoday.testinstance;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AdditionUnitTest {
    private int sum = 1;

    @Test
    void addingTwoToSumReturnsThree() {
        sum += 2;
        Assertions.assertEquals(3, sum);
    }

    @Test
    void addingThreeToSumReturnsFour() {
        sum += 3;
        Assertions.assertEquals(4, sum);
    }
}