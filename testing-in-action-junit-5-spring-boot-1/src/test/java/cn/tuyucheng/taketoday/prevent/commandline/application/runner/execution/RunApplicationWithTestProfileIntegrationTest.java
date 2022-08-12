package cn.tuyucheng.taketoday.prevent.commandline.application.runner.execution;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest // bootstrap entire container
class RunApplicationWithTestProfileIntegrationTest {
    @Autowired
    private ApplicationContext context;

    @Test
    @DisplayName("whenContextLoads_thenRunnersAreNotLoaded")
    void whenContextLoads_thenRunnersAreNotLoaded() {
        assertNotNull(context.getBean(TaskService.class));
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(CommandLineTaskExecutor.class),
                "CommandLineRunner should not be loaded during this integration test");
        assertThrows(NoSuchBeanDefinitionException.class, () -> context.getBean(ApplicationRunnerTaskExecutor.class),
                "ApplicationRunner should not be loaded during this integration test");
    }
}