package cn.tuyucheng.taketoday.mockito;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MockitoInjectIntoSpyUnitTest {
    @Mock
    Map<String, String> wordMap;
    @InjectMocks
    MyDictionary dic = new MyDictionary();
    MyDictionary spyDic;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spyDic = Mockito.spy(new MyDictionary(wordMap));
    }

    @Test
    @DisplayName("whenUseInjectMocksAnnotation_thenCorrect")
    void whenUseInjectMocksAnnotation_thenCorrect() {
        Mockito.when(wordMap.get("aWord")).thenReturn("aMeaning");
        assertEquals("aMeaning", spyDic.getMeaning("aWord"));
    }
}