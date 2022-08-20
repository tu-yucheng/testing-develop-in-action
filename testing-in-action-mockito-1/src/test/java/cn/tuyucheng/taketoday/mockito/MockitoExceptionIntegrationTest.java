package cn.tuyucheng.taketoday.mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MockitoExceptionIntegrationTest {

    @Test
    @DisplayName("whenConfigNonVoidReturnMethodToThrowEx_thenExIsThrown")
    void whenConfigNonVoidReturnMethodToThrowEx_thenExIsThrown() {
        MyDictionary dictMock = mock(MyDictionary.class);
        when(dictMock.getMeaning(anyString())).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> dictMock.getMeaning("word"));
    }

    @Test
    @DisplayName("whenConfigVoidReturnMethodThrowEx_thenExIsThrown")
    void whenConfigVoidReturnMethodThrowEx_thenExIsThrown() {
        MyDictionary dictMock = mock(MyDictionary.class);
        doThrow(IllegalStateException.class).when(dictMock).add(anyString(), anyString());
        assertThrows(IllegalStateException.class, () -> dictMock.add("word", "meaning"));
    }

    @Test
    @DisplayName("whenConfigNonVoidReturnMethodToThrowExWithNewExObj_thenExIsThrown")
    void whenConfigNonVoidReturnMethodToThrowExWithNewExObj_thenExIsThrown() {
        MyDictionary dictMock = mock(MyDictionary.class);
        when(dictMock.getMeaning(anyString())).thenThrow(new NullPointerException("Error occurred"));
        assertThrows(NullPointerException.class, () -> dictMock.getMeaning("word"));
    }

    @Test
    @DisplayName("whenConfigVoidReturnMethodToThrowExWithNewExObj_thenExIsThrown")
    void whenConfigVoidReturnMethodToThrowExWithNewExObj_thenExIsThrown() {
        MyDictionary dictMock = mock(MyDictionary.class);
        doThrow(new IllegalStateException("Error occurred")).when(dictMock).add(anyString(), anyString());
        assertThrows(IllegalStateException.class, () -> dictMock.add("word", "meaning"));
    }

    @Test
    @DisplayName("whenConfigVoidReturnMethodToThrowEx_thenExIsThrown")
    void whenConfigVoidReturnMethodToThrowEx_thenExIsThrown() {
        MyDictionary dict = new MyDictionary();
        MyDictionary spy = spy(dict);
        when(spy.getMeaning(anyString())).thenThrow(NullPointerException.class);
        assertThrows(NullPointerException.class, () -> spy.getMeaning("word"));
    }
}