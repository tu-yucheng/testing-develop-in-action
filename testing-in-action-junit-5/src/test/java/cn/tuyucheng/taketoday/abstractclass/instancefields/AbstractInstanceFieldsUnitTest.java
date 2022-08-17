package cn.tuyucheng.taketoday.abstractclass.instancefields;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractInstanceFieldsUnitTest {

    @Test
    void givenProtectedInstanceField_whenMockClassCountGt5_thenTestNonAbstractMethod() {
        // mock
        AbstractInstanceFields instClass = Mockito.mock(AbstractInstanceFields.class);
        Mockito.doCallRealMethod()
                .when(instClass)
                .testFunc();

        // set counter greater than 5
        instClass.count = 7;

        // compare the result
        assertEquals("Overflow", instClass.testFunc());
    }

    @Test
    @DisplayName("givenNonAbstractMethodAndPrivateField_whenPowerMockitoAndActiveFieldTrue_thenCorrectBehaviour")
    void givenNonAbstractMethodAndPrivateField_whenPowerMockitoAndActiveFieldTrue_thenCorrectBehaviour() {

        AbstractInstanceFields instClass = PowerMockito.mock(AbstractInstanceFields.class);
        PowerMockito.doCallRealMethod()
                .when(instClass)
                .testFunc();
        Whitebox.setInternalState(instClass, "active", true);

        // compare the expected result with actual
        assertEquals("Added", instClass.testFunc());
    }
}