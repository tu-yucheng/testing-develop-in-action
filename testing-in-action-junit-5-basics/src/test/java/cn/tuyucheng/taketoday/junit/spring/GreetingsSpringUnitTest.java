package cn.tuyucheng.taketoday.junit.spring;

import cn.tuyucheng.taketoday.junit5.Greetings;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
// @RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringTestConfiguration.class})
public class GreetingsSpringUnitTest {

    @Test
    public void whenCallingSayHello_thenReturnHello() {
        assertEquals("Hello", Greetings.sayHello());
    }
}