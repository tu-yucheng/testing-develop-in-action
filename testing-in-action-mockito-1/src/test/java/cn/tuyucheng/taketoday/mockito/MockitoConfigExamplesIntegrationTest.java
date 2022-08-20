package cn.tuyucheng.taketoday.mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class MockitoConfigExamplesIntegrationTest {

    @Test
    @DisplayName("whenMockReturnBehaviorIsConfigured_thenBehaviorIsVerified")
    void whenMockReturnBehaviorIsConfigured_thenBehaviorIsVerified() {
        MyList listMock = mock(MyList.class);
        when(listMock.add(anyString())).thenReturn(false);
        boolean added = listMock.add(randomAlphabetic(6));
        assertThat(added, is(false));
    }

    @Test
    @DisplayName("whenMockReturnBehaviorIsConfigured2_thenBehaviorIsVerified")
    void whenMockReturnBehaviorIsConfigured2_thenBehaviorIsVerified() {
        MyList listMock = mock(MyList.class);
        doReturn(false).when(listMock).add(anyString());
        boolean added = listMock.add(randomAlphabetic(6));
        assertThat(added, is(false));
    }

    @Test
    @DisplayName("givenMethodIsConfiguredToThrowException_whenCallingMethod_thenExceptionIsThrown")
    void givenMethodIsConfiguredToThrowException_whenCallingMethod_thenExceptionIsThrown() {
        MyList listMock = mock(MyList.class);
        when(listMock.add(anyString())).thenThrow(IllegalStateException.class);
        assertThrows(IllegalStateException.class, () -> listMock.add(randomAlphabetic(6)));
    }

    @Test
    @DisplayName("whenMethodHasNoReturnType_whenConfiguringBehaviorOfMethod_thenPossible")
    void whenMethodHasNoReturnType_whenConfiguringBehaviorOfMethod_thenPossible() {
        MyList listMock = mock(MyList.class);
        doThrow(NullPointerException.class).when(listMock).clear();
        assertThrows(NullPointerException.class, listMock::clear);
    }

    @Test
    @DisplayName("givenBehaviorIsConfiguredToThrowExceptionOnSecondCall_whenCallingTwice_thenExceptionIsThrown")
    void givenBehaviorIsConfiguredToThrowExceptionOnSecondCall_whenCallingTwice_thenExceptionIsThrown() {
        MyList listMock = mock(MyList.class);
        when(listMock.add(anyString())).thenReturn(false).thenThrow(IllegalStateException.class);
        boolean added = listMock.add(randomAlphabetic(6));
        assertThat(added, is(false));
        assertThrows(IllegalStateException.class, () -> listMock.add(randomAlphabetic(6)));
    }

    @Test
    @DisplayName("givenBehaviorIsConfiguredToThrowExceptionOnSecondCall_whenCallingOnlyOnce_thenNoExceptionIsThrown")
    void givenBehaviorIsConfiguredToThrowExceptionOnSecondCall_whenCallingOnlyOnce_thenNoExceptionIsThrown() {
        MyList listMock = mock(MyList.class);
        when(listMock.add(anyString())).thenReturn(false).thenThrow(IllegalStateException.class);
        listMock.add(randomAlphabetic(6));
    }

    @Test
    @DisplayName("givenSpy_whenConfiguringBehaviorOfSpy_thenCorrectlyConfigured")
    void givenSpy_whenConfiguringBehaviorOfSpy_thenCorrectlyConfigured() {
        final MyList instance = new MyList();
        final MyList spy = Mockito.spy(instance);
        doThrow(NullPointerException.class).when(spy).size();
        assertThrows(NullPointerException.class, spy::size);
    }

    @Test
    @DisplayName("whenMockMethodCallingIsConfiguredToCallTheRealMethod_thenRealMethodIsCalled")
    void whenMockMethodCallingIsConfiguredToCallTheRealMethod_thenRealMethodIsCalled() {
        MyList listMock = mock(MyList.class);
        when(listMock.size()).thenCallRealMethod();
        assertThat(listMock.size(), equalTo(1));
    }

    @Test
    @DisplayName("whenMockMethodCallIsConfiguredWithCustomAnswer_thenRealMethodIsCalled")
    void whenMockMethodCallIsConfiguredWithCustomAnswer_thenRealMethodIsCalled() {
        MyList listMock = mock(MyList.class);
        doAnswer(invocation -> "Always the same").when(listMock).get(anyInt());
        String element = listMock.get(1);
        assertThat(element, is(equalTo("Always the same")));
    }
}