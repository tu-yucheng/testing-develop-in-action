package cn.tuyucheng.taketoday.autoconfig.exclude;

import cn.tuyucheng.taketoday.boot.Application;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExcludeAutoConfig3IntegrationTest {
    @LocalServerPort
    private int port;

    @Test
    @DisplayName("givenSecurityConfigExcluded_whenAccessHome_thenNoAuthenticationRequired")
    void givenSecurityConfigExcluded_whenAccessHome_thenNoAuthenticationRequired() {
        int statusCode = RestAssured.get("http://localhost:" + port).getStatusCode();
        assertEquals(HttpStatus.OK.value(), statusCode);
    }
}