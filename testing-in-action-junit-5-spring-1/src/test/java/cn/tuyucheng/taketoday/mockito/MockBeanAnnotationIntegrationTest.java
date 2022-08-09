package cn.tuyucheng.taketoday.mockito;

import cn.tuyucheng.taketoday.mockito.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class MockBeanAnnotationIntegrationTest {
    @MockBean  // add mock objects to the spring application context
    UserRepository mockRepository;

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("givenCountMethodMocked_whenCountInvoked_thenMockValueReturned")
    public void givenCountMethodMocked_whenCountInvoked_thenMockValueReturned() {
        Mockito.when(mockRepository.count()).thenReturn(123L);
        UserRepository userRepositoryFromContext = context.getBean(UserRepository.class);
        long userCount = userRepositoryFromContext.count();
        assertEquals(123L, userCount);
        Mockito.verify(mockRepository).count();
    }
}