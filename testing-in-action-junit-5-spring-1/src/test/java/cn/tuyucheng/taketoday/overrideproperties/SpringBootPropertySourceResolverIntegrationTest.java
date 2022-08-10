package cn.tuyucheng.taketoday.overrideproperties;

import cn.tuyucheng.taketoday.overrideproperties.resolver.PropertySourceResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableWebMvc
@SpringBootTest(properties = "example.firstProperty=annotation")
class SpringBootPropertySourceResolverIntegrationTest {
    @Autowired
    private PropertySourceResolver propertySourceResolver;

    @Test
    @DisplayName("shouldSpringbootTestAnnotation_overridePropertyValues")
    void shouldSpringbootTestAnnotation_overridePropertyValues() {
        final String firstProperty = propertySourceResolver.getFirstProperty();
        final String secondProperty = propertySourceResolver.getSecondProperty();
        assertEquals("annotation", firstProperty);
        assertEquals("file", secondProperty);
    }
}