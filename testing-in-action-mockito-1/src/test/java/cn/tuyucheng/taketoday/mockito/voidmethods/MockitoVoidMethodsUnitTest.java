package cn.tuyucheng.taketoday.mockito.voidmethods;

import cn.tuyucheng.taketoday.mockito.MyList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MockitoVoidMethodsUnitTest {

    @Test
    @DisplayName("whenAddCalledVerified")
    void whenAddCalledVerified() {
        MyList listMock = mock(MyList.class);
        doNothing().when(listMock).add(isA(Integer.class), isA(String.class));
        listMock.add(0, "");
        verify(listMock, times(1)).add(0, "");
    }

    @Test
    @DisplayName("givenNull_addThrows")
    void givenNull_addThrows() {
        MyList listMock = mock(MyList.class);
        assertThrows(Exception.class, () -> doThrow().when(listMock).add(isA(Integer.class), isA(String.class)));
        listMock.add(0, null);
    }

    @Test
    @DisplayName("whenAddCalledValueCaptured")
    void whenAddCalledValueCaptured() {
        MyList listMock = mock(MyList.class);
        ArgumentCaptor<String> valueCapture = ArgumentCaptor.forClass(String.class);
        doNothing().when(listMock).add(any(Integer.class), valueCapture.capture());
        listMock.add(0, "captured");
        assertEquals("captured", valueCapture.getValue());
    }

    @Test
    @DisplayName("whenAddCalledAnswered")
    void whenAddCalledAnswered() {
        MyList listMock = mock(MyList.class);
        doAnswer(invocation -> {
            Object arg0 = invocation.getArgument(0);
            Object arg1 = invocation.getArgument(1);
            // do something with the arguments here
            assertEquals(3, arg0);
            assertEquals("answer me", arg1);
            return null;
        }).when(listMock).add(any(Integer.class), any(String.class));
        listMock.add(3, "answer me");
    }

    @Test
    @DisplayName("whenAddCalledRealMethodCalled")
    void whenAddCalledRealMethodCalled() {
        MyList listMock = mock(MyList.class);
        doCallRealMethod().when(listMock).add(any(Integer.class), any(String.class));
        listMock.add(1, "real");
        verify(listMock, times(1)).add(1, "real");
    }
}