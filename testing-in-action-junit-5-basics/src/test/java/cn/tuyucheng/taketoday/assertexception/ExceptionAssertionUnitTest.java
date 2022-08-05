package cn.tuyucheng.taketoday.assertexception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExceptionAssertionUnitTest {

    @Test
    @DisplayName("whenExceptionThrown_thenAssertionSuccessed")
    void whenExceptionThrown_thenAssertionSuccessed() {
        Exception exception = assertThrows(NumberFormatException.class, () -> Integer.parseInt("1a"));
        String expectedMessage = "For input string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("whenDerivedExceptionThrown_thenAssertionSuccessed")
    void whenDerivedExceptionThrown_thenAssertionSuccessed() {
        Exception exception = assertThrows(RuntimeException.class, () -> Integer.parseInt("1a"));
        String expectedMessage = "For input string";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}