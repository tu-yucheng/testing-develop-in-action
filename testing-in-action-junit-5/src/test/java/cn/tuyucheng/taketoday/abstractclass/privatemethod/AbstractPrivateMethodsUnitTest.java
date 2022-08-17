package cn.tuyucheng.taketoday.abstractclass.privatemethod;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Providing custom values for private methods using powermock
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractPrivateMethods.class)
public class AbstractPrivateMethodsUnitTest {

    @Test
    @DisplayName("givenNonAbstractMethodAndCallPrivateMethod_whenMockPrivateMethod_thenVerifyBehaviour")
    public void givenNonAbstractMethodAndCallPrivateMethod_whenMockPrivateMethod_thenVerifyBehaviour() throws Exception {
        AbstractPrivateMethods mockClass = PowerMockito.mock(AbstractPrivateMethods.class);
        PowerMockito.doCallRealMethod().when(mockClass).defaultImpl();
        String dateTime = LocalDateTime.now().toString();
        PowerMockito.doReturn(dateTime).when(mockClass, "getCurrentDateTime");
        String actual = mockClass.defaultImpl();
        assertEquals(dateTime + "DEFAULT-1", actual);
    }
}