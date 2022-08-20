package cn.tuyucheng.taketoday.mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockSettings;
import org.mockito.exceptions.verification.TooFewActualInvocations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class MockitoMockIntegrationTest {

    @Test
    @DisplayName("whenUsingSimpleMock_thenCorrect")
    void whenUsingSimpleMock_thenCorrect() {
        MyList mockedList = mock(MyList.class);
        when(mockedList.add(anyString())).thenReturn(false);
        boolean added = mockedList.add(randomAlphabetic(6));
        verify(mockedList).add(anyString());
        assertThat(added, is(false));
    }

    @Test
    @DisplayName("whenUsingMockWithName_thenCorrect")
    void whenUsingMockWithName_thenCorrect() {
        MyList listMock = mock(MyList.class, "myMock");
        when(listMock.add(anyString())).thenReturn(false);
        listMock.add(randomAlphabetic(6));
        assertThatThrownBy(() -> verify(listMock, times(2)).add(anyString()))
                .isInstanceOf(TooFewActualInvocations.class)
                .hasMessageContaining("myMock.add");
    }

    @Test
    @DisplayName("whenUsingMockWithAnswer_thenCorrect")
    void whenUsingMockWithAnswer_thenCorrect() {
        MyList listMock = mock(MyList.class, new CustomAnswer());
        boolean added = listMock.add(randomAlphabetic(6));
        verify(listMock).add(anyString());
        assertThat(added, is(false));
    }

    @Test
    @DisplayName("whenUsingMockWithSettings_thenCorrect")
    void whenUsingMockWithSettings_thenCorrect() {
        MockSettings customSettings = withSettings().defaultAnswer(new CustomAnswer());
        MyList listMock = mock(MyList.class, customSettings);
        boolean added = listMock.add(randomAlphabetic(6));
        verify(listMock).add(anyString());
        assertThat(added, is(false));
    }

    private static class CustomAnswer implements Answer<Boolean> {

        @Override
        public Boolean answer(InvocationOnMock invocation) {
            return false;
        }
    }
}