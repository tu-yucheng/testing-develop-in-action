package cn.tuyucheng.taketoday.abstractclass.abstractmethod;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractMethodCallingUnitTest {
    private AbstractMethodCalling abstractMethodCalling;

    @BeforeEach
    public void setup() {
        abstractMethodCalling = Mockito.mock(AbstractMethodCalling.class);
    }

    @Test
    @DisplayName("givenDefaultImpl_whenMockAbstractFunc_thenExpectedBehaviour")
    void givenDefaultImpl_whenMockAbstractFunc_thenExpectedBehaviour() {
        Mockito.when(abstractMethodCalling.abstractFunc()).thenReturn("Abstract");
        Mockito.doCallRealMethod().when(abstractMethodCalling).defaultImpl();
        // validate result by mock abstractFunc's behaviour
        assertEquals("Abstract Default", abstractMethodCalling.defaultImpl());

        // check the value with null response from abstract method
        Mockito.doReturn(null).when(abstractMethodCalling).abstractFunc();
        assertEquals("Default", abstractMethodCalling.defaultImpl());
    }
}