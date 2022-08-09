package cn.tuyucheng.taketoday.reflectiontestutils;

import cn.tuyucheng.taketoday.reflectiontestutils.repository.Employee;
import cn.tuyucheng.taketoday.reflectiontestutils.repository.EmployeeService;
import cn.tuyucheng.taketoday.reflectiontestutils.repository.HRService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReflectionTestUtilsUnitTest {

    @Test
    @DisplayName("whenNonPublicField_thenReflectionTestUtilsSetField")
    void whenNonPublicField_thenReflectionTestUtilsSetField() {
        Employee employee = new Employee();
        ReflectionTestUtils.setField(employee, "id", 1);
        assertEquals(1, employee.getId());
    }

    @Test
    @DisplayName("whenNonPublicMethod_thenReflectionTestUtilsInvokeMethod")
    public void whenNonPublicMethod_thenReflectionTestUtilsInvokeMethod() {
        Employee employee = new Employee();
        ReflectionTestUtils.setField(employee, "id", 1);
        employee.setName("Smith, John");
        assertEquals("id: 1; name: Smith, John", ReflectionTestUtils.invokeMethod(employee, "employeeToString"));
    }

    @Test
    @DisplayName("whenInjectingMockOfDependency_thenReflectionTestUtilsSetField")
    public void whenInjectingMockOfDependency_thenReflectionTestUtilsSetField() {
        Employee employee = new Employee();
        ReflectionTestUtils.setField(employee, "id", 1);
        employee.setName("Smith, John");
        HRService hrService = Mockito.mock(HRService.class);
        Mockito.when(hrService.getEmployeeStatus(employee.getId())).thenReturn("Active");

        EmployeeService employeeService = new EmployeeService();

        // Inject mock into the private field
        ReflectionTestUtils.setField(employeeService, "hrService", hrService);
        assertEquals("Employee " + employee.getId() + " status: Active", employeeService.findEmployeeStatus(employee.getId()));
    }
}