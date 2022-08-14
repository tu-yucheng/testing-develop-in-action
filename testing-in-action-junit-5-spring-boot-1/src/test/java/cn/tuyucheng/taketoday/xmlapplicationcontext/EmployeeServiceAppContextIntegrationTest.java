package cn.tuyucheng.taketoday.xmlapplicationcontext;

import cn.tuyucheng.taketoday.xmlapplicationcontext.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = XmlBeanApplication.class)
// @ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/application-context.xml")
// @ContextConfiguration(locations = "classpath:WEB-INF/application-context.xml")
class EmployeeServiceAppContextIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    @DisplayName("whenContextLoads_thenServiceISNotNull")
    void whenContextLoads_thenServiceISNotNull() {
        assertThat(employeeService).isNotNull();
    }
}