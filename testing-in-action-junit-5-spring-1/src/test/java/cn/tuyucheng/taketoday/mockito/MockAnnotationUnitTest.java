package cn.tuyucheng.taketoday.mockito;

import cn.tuyucheng.taketoday.mockito.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MockAnnotationUnitTest {

    /*
       this annotation is a shorthand for the Mockito.mock() method,
       we need to enable Mockito annotations to use this annotation.
       we can do this either by using the MockitoExtension to run the test,
       or by calling the MockitoAnnotations.initMocks() method explicitly.
     */
    @Mock
    private UserRepository mockRepository;

    @Test
    @DisplayName("givenCountMethodMocked_whenCountInvoked_thenMockValueReturned")
    void givenCountMethodMocked_whenCountInvoked_thenMockValueReturned() {
        when(mockRepository.count()).thenReturn(123L);
        long userCount = mockRepository.count();
        assertEquals(123L, userCount);
        Mockito.verify(mockRepository).count();
    }

    @Test
    @DisplayName("givenCountMethodMocked_whenCountInvoked_thenMockedValueReturned")
    void givenCountMethodMocked_whenCountInvoked_thenMockedValueReturned() {
        UserRepository localMockRepository = Mockito.mock(UserRepository.class);
        when(localMockRepository.count()).thenReturn(111L);
        long userCount = localMockRepository.count();
        assertEquals(111L, userCount);
        verify(localMockRepository).count();
    }
}