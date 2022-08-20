package cn.tuyucheng.taketoday.mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class MockitoAnnotationsUninitializedUnitTest {

    @Mock
    List<String> mockedList;

    @Test
    @DisplayName("whenMockitoAnnotationsUninitialized_thenNPEThrown")
    void whenMockitoAnnotationsUninitialized_thenNPEThrown() {
        assertThrows(NullPointerException.class, () -> when(mockedList.size()).thenReturn(1));
    }
}