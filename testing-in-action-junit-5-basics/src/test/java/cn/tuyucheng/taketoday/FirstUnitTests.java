package cn.tuyucheng.taketoday;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FirstUnitTests {

    @Test
    @DisplayName("lambdaExpressions")
    void lambdaExpressions() {
        List<Integer> numbers = Arrays.asList(1, 2, 3);
        assertTrue(numbers.stream().mapToInt(x -> x).sum() > 5, "Sum should be greater than 5");
    }

    @Test
    @DisplayName("groupAssertions")
    @Disabled("test to show MultipleFailureError")
    void groupAssertions() {
        int[] numbers = {0, 1, 2, 3, 4};
        assertAll("number",
                () -> assertEquals(1, numbers[0]),
                () -> assertEquals(3, numbers[3]),
                () -> assertEquals(1, numbers[4])
        );
    }
}