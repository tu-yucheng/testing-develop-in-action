package cn.tuyucheng.taketoday.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

//@RunWith(MockitoJUnitRunner.class)
// @ExtendWith(MockitoExtension.class)
class MockitoAnnotationIntegrationTest {
    @Mock
    List<String> mockedList;

    @Spy
    List<String> spiedList = new ArrayList<>();

    @Captor
    ArgumentCaptor<String> argCaptor;

    @Mock
    Map<String, String> wordMap;

    @InjectMocks
    MyDictionary dic = new MyDictionary();

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("whenNotUseMockAnnotation_thenCorrect")
    void whenNotUseMockAnnotation_thenCorrect() {
        List<String> listMock = mock(ArrayList.class);
        listMock.add("one");
        verify(listMock).add("one");
        assertEquals(0, listMock.size());

        when(listMock.size()).thenReturn(100);
        assertEquals(100, listMock.size());
    }

    @Test
    @DisplayName("whenUsingMockAnnotatino_thenMockIsInjected")
    void whenUsingMockAnnotatino_thenMockIsInjected() {
        mockedList.add("one");
        verify(mockedList).add("one");
        assertEquals(0, mockedList.size());
        when(mockedList.size()).thenReturn(100);
        assertEquals(100, mockedList.size());
    }

    @Test
    @DisplayName("whenNotUseSpyAnnotation_thenCorrect")
    void whenNotUseSpyAnnotation_thenCorrect() {
        ArrayList<Object> spyList = spy(new ArrayList<>());
        spyList.add("one");
        spyList.add("two");
        verify(spyList).add("one");
        verify(spyList).add("two");
        assertEquals(2, spyList.size());
        doReturn(100).when(spyList).size();
        assertEquals(100, spyList.size());
    }

    @Test
    @DisplayName("whenUseSpyAnnotation_thenSpyInjectedCorrect")
    void whenUseSpyAnnotation_thenSpyInjectedCorrect() {
        spiedList.add("one");
        spiedList.add("two");
        verify(spiedList).add("one");
        verify(spiedList).add("two");

        assertEquals(2, spiedList.size());
        doReturn(100).when(spiedList).size();
        assertEquals(100, spiedList.size());
    }

    @Test
    @DisplayName("whenNotUseCaptorAnnotation_thenCorrect")
    void whenNotUseCaptorAnnotation_thenCorrect() {
        List<String> mockList = mock(List.class);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        mockList.add("one");
        verify(mockList).add(argumentCaptor.capture());
        assertEquals("one", argumentCaptor.getValue());
    }


    @Test
    @DisplayName("whenUseCaptorAnnotation_thenTheSame")
    void whenUseCaptorAnnotation_thenTheSame() {
        mockedList.add("one");
        verify(mockedList).add(argCaptor.capture());
        assertEquals("one", argCaptor.getValue());
    }

    @Test
    @DisplayName("whenUseInjectMocksAnnotation_thenCorrect")
    void whenUseInjectMocksAnnotation_thenCorrect() {
        when(wordMap.get("aWord")).thenReturn("aMeaning");
        assertEquals("aMeaning", dic.getMeaning("aWord"));
    }
}