package cn.tuyucheng.taketoday.mockito;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MockFinalsIntegrationTest {

    @Test
    @DisplayName("whenMockFinalClassMockWorks")
    void whenMockFinalClassMockWorks() {
        FinalList finalList = new FinalList();
        FinalList mock = mock(FinalList.class);
        when(mock.size()).thenReturn(2);
        assertNotEquals(mock.size(), finalList.size());
    }

    @Test
    @DisplayName("whenMockFinalMethodMockWorks")
    void whenMockFinalMethodMockWorks() {
        MyList myList = new MyList();
        MyList mockList = mock(MyList.class);
        when(mockList.finalMethod()).thenReturn(1);
        assertNotEquals(mockList.finalMethod(), myList.finalMethod());
    }
}