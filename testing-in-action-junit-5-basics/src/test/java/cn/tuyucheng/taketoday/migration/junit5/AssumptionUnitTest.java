package cn.tuyucheng.taketoday.migration.junit5;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

public class AssumptionUnitTest {

    @Test
    @DisplayName("trueAssumptions")
    void trueAssumptions() {
        assumeTrue(5 > 1, () -> "5 is greater than 1");
        assertEquals(5 + 2, 7);
    }

    @Test
    @DisplayName("falseAssumptions")
    void falseAssumptions() {
        assumeFalse(5 < 1, () -> "5 is less than 1");
        assertEquals(5 + 2, 7);
    }

    @Test
    @DisplayName("assumptionThat")
    void assumptionThat() {
        String something = "Just a string";
        assumingThat(something.equals("Just a string"), () -> assertEquals(2 + 2, 4));
    }

    @Test
    @DisplayName("whenEnvironmentIsWeb_thenUrlsShouldStartWithHttp")
    public void whenEnvironmentIsWeb_thenUrlsShouldStartWithHttp() {
        assumingThat("WEB".equals(System.getenv("ENV")), Assertions::fail);
    }
}