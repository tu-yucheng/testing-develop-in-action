package cn.tuyucheng.taketoday;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ExceptionUnitTests {

    @Test
    @DisplayName("showThrowException")
    void showThrowException() {
        Throwable exception = assertThrows(UnsupportedOperationException.class, () -> {
            throw new UnsupportedOperationException("Not supported");
        });
        assertEquals("Not supported", exception.getMessage());
    }

    @Test
    @DisplayName("assertThrowsNullPointerException")
    void assertThrowsNullPointerException() {
        String str = null;
        assertThrows(NullPointerException.class, () -> str.length());
    }

    @Test
    @DisplayName("whenModifyMapDuringIteration_thenThrownException")
    void whenModifyMapDuringIteration_thenThrownException() {
        Map<Integer, String> map = new HashMap<>();
        map.put(1, "One");
        map.put(2, "Two");
        Executable executable = () -> map.forEach((key, value) -> map.remove(1));
        assertThrows(ConcurrentModificationException.class, executable);
    }
}