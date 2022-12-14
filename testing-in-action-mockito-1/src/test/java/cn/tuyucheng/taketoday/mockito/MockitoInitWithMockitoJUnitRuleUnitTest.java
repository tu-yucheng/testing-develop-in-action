package cn.tuyucheng.taketoday.mockito;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class MockitoInitWithMockitoJUnitRuleUnitTest {

    @Rule
    public MockitoRule initRule = MockitoJUnit.rule();

    @Mock
    private List<String> mockedList;

    @Test
    public void whenUsingMockitoJunitRule_thenMocksInitialized() {
        when(mockedList.size()).thenReturn(4);
        assertThat(mockedList.size()).isEqualTo(4);
    }
}