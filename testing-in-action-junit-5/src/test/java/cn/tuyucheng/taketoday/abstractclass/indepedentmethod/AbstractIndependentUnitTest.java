package cn.tuyucheng.taketoday.abstractclass.indepedentmethod;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractIndependentUnitTest {

    @Test
    @DisplayName("givenNonAbstractMethod_whenConcreteImple_testCorrectBehaviour")
    void givenNonAbstractMethod_whenConcreteImple_testCorrectBehaviour() {
        ConcreteImpl conClass = new ConcreteImpl();
        String actual = conClass.defaultImpl();
        assertEquals("DEFAULT-1", actual);
    }

    @Test
    @DisplayName("givenNonAbstractMethod_whenMockitoMock_testCorrectBehaviour")
    void givenNonAbstractMethod_whenMockitoMock_testCorrectBehaviour() {
        AbstractIndependent absClass = Mockito.mock(AbstractIndependent.class, Mockito.CALLS_REAL_METHODS);
        assertEquals("DEFAULT-1", absClass.defaultImpl());
    }
}