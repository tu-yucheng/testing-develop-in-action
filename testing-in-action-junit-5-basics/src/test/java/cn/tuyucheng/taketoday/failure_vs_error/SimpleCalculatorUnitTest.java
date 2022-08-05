package cn.tuyucheng.taketoday.failure_vs_error;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimpleCalculatorUnitTest {

    @Test
    @DisplayName("whenDivideByValidNumber_thenAssertCorrectResult")
    void whenDivideByValidNumber_thenAssertCorrectResult() {
        double result = SimpleCalculator.divideNumbers(6, 3);
        assertEquals(2, result);
    }

    @Test
    @Disabled("test is expected to fail, disabled so that CI build still goes through")
    @DisplayName("whenDivideNumbers_thenExpectWrongResult")
    void whenDivideNumbers_thenExpectWrongResult() {
        double result = SimpleCalculator.divideNumbers(6, 3);
        assertEquals(15, result);
    }

    @Test
    @Disabled("test is expected to raise an error, disabled so that CI still goes through")
    @DisplayName("whenDivideByZero_thenThrowsException")
    void whenDivideByZero_thenThrowsException() {
        SimpleCalculator.divideNumbers(10, 0);
    }

    @Test
    @DisplayName("whenDivideByZero_thenAssertException")
    void whenDivideByZero_thenAssertException() {
        assertThrows(ArithmeticException.class, () -> SimpleCalculator.divideNumbers(10, 0));
    }
}