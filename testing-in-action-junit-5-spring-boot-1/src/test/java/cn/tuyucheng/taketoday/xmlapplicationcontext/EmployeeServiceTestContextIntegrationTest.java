package cn.tuyucheng.taketoday.xmlapplicationcontext;

import cn.tuyucheng.taketoday.xmlapplicationcontext.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(locations = "/test-context.xml")
class EmployeeServiceTestContextIntegrationTest {
    @Autowired
    @Qualifier("employeeServiceTestImpl")
    private EmployeeService employeeServiceTest;

    @Test
    @DisplayName("whenContextLoads_thenServiceTestIsNotNull")
    void whenContextLoads_thenServiceTestIsNotNull() {
        assertThat(employeeServiceTest).isNotNull();
    }
}