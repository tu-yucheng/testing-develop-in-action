package cn.tuyucheng.taketoday.autoconfig.exclude;

import cn.tuyucheng.taketoday.boot.Application;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExcludeAutoConfig2IntegrationTest {
    @LocalServerPort
    private int port;

    @Test
    @DisplayName("givenSecurityConfigExcluded_whenAccessHome_thenNoAuthenticationRequired")
    void givenSecurityConfigExcluded_whenAccessHome_thenNoAuthenticationRequired() {
        int statusCode = RestAssured.get("http://localhost:" + port).getStatusCode();
        assertEquals(HttpStatus.OK.value(), statusCode);
    }
}