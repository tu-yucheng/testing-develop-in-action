package cn.tuyucheng.taketoday;

import cn.tuyucheng.taketoday.extensions.*;
import cn.tuyucheng.taketoday.helpers.Employee;
import cn.tuyucheng.taketoday.helpers.EmployeeJdbcDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({EnvironmentExtension.class, EmployeeDatabaseSetupExtension.class, EmployeeDaoParameterResolver.class})
@ExtendWith(LoggingExtension.class)
@ExtendWith(IgnoreFileNotFoundExceptionExtension.class)
public class EmployeesUnitTest {

    private EmployeeJdbcDao employeeDao;

    private Logger logger;

    public EmployeesUnitTest(EmployeeJdbcDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Test
    @DisplayName("whenAddEmployee_thenGetEmployee")
    public void whenAddEmployee_thenGetEmployee() throws SQLException {
        Employee emp = new Employee(1, "john");
        employeeDao.add(emp);
        assertEquals(1, employeeDao.findAll().size());
    }

    @Test
    @DisplayName("whenGetEmployees_thenEmptyList")
    public void whenGetEmployees_thenEmptyList() throws SQLException {
        assertEquals(0, employeeDao.findAll().size());
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}