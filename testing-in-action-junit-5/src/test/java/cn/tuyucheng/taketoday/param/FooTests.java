package cn.tuyucheng.taketoday.param;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(FooParameterResolver.class)
class FooTests {

    @Test
    void testIt(Foo fooInstance) {
        assertNotNull(fooInstance);
    }
}