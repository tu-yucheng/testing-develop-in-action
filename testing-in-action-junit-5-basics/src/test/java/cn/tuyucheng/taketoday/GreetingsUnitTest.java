package cn.tuyucheng.taketoday;

import cn.tuyucheng.taketoday.junit5.Greetings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @RunWith(JUnitPlatform.class)
public class GreetingsUnitTest {

    @Test
    @DisplayName("whenCallingSayHello_thenReturnHello")
    void whenCallingSayHello_thenReturnHello() {
        assertEquals("Hello", Greetings.sayHello());
    }
}