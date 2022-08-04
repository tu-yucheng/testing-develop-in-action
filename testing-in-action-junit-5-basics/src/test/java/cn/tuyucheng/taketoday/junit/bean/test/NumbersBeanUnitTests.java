package cn.tuyucheng.taketoday.junit.bean.test;

import cn.tuyucheng.taketoday.junit5.bean.NumbersBean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NumbersBeanUnitTests {
    private final NumbersBean bean = new NumbersBean();

    @Test
    @DisplayName("givenEvenNumber_whenCheckingIsNumberEven_thenTure")
    void givenEvenNumber_whenCheckingIsNumberEven_thenTure() {
        boolean result = bean.isNumberEven(8);
        assertTrue(result);
    }

    @Test
    @DisplayName("givenOddNumber_whenCheckingIsNumberEven_thenFalse")
    void givenOddNumber_whenCheckingIsNumberEven_thenFalse() {
        boolean result = bean.isNumberEven(1);
        assertFalse(result);
    }

    @Test
    @DisplayName("givenLowerThanTenNumber_whenCheckingIsNumberEven_thenResultUnderTenMillis")
    void givenLowerThanTenNumber_whenCheckingIsNumberEven_thenResultUnderTenMillis() {
        assertTimeout(Duration.ofMillis(10), () -> bean.isNumberEven(3));
    }

    @Test
    @DisplayName("givenNull_whenCheckingIsNumberEven_thenNullPointerException")
    void givenNull_whenCheckingIsNumberEven_thenNullPointerException() {
        assertThrows(NullPointerException.class, () -> bean.isNumberEven(null));
    }
}