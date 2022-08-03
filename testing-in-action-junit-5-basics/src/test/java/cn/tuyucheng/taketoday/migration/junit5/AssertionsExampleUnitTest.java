package cn.tuyucheng.taketoday.migration.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionsExampleUnitTest {

    @Test
    @Disabled
    public void shouldFailBecauseTheNumbersAreNotEqual() {
        assertEquals(2, 3, "Numbers are not equal!");
    }

    @Test
    @Disabled
    public void shouldFailBecauseTheNumbersAreNotEqual_lazyEvaluation() {
        assertTrue(2 == 3, () -> "Numbers " + 2 + " and " + 3 + " are not equal!");
    }

    @Test
    @Disabled
    public void shouldFailBecauseItsNotTrue_overloading() {
        fail("It's not true!");
    }

    @Test
    @DisplayName("shouldAssertAllTheGroup")
    public void shouldAssertAllTheGroup() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Assertions.assertAll("List is not incremental",
                () -> assertEquals(list.get(0).intValue(), 1),
                () -> assertEquals(list.get(1).intValue(), 2),
                () -> assertEquals(list.get(2).intValue(), 3)
        );
    }
}