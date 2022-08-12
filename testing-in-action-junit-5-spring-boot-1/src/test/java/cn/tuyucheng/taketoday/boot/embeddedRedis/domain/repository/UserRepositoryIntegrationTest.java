package cn.tuyucheng.taketoday.boot.embeddedRedis.domain.repository;

import cn.tuyucheng.taketoday.boot.embeddedRedis.TestRedisConfiguration;
import cn.tuyucheng.taketoday.boot.embeddedRedis.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestRedisConfiguration.class)
class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    @DisplayName("shouldSaveUser_toRedis")
    void shouldSaveUser_toRedis() {
        final UUID id = UUID.randomUUID();
        final User user = new User(id, "name");
        final User saved = userRepository.save(user);
        Arrays.stream(applicationContext.getBeanDefinitionNames()).forEach(System.out::println);
        assertNotNull(saved);
    }
}