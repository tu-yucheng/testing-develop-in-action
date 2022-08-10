package cn.tuyucheng.taketoday.overrideproperties;

import cn.tuyucheng.taketoday.overrideproperties.resolver.PropertySourceResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableWebMvc
@SpringBootTest
class TestResourcePropertySourceResolverIntegrationTest {
    @Autowired
    private PropertySourceResolver propertySourceResolver;

    @Test
    @DisplayName("shouldTestResourceFile_overridePropertyValues")
    void shouldTestResourceFile_overridePropertyValues() {
        final String firstProperty = propertySourceResolver.getFirstProperty();
        final String secondProperty = propertySourceResolver.getFirstProperty();
        assertEquals("file", firstProperty);
        assertEquals("file", secondProperty);
    }
}