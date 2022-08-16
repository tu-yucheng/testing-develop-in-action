package cn.tuyucheng.taketoday;

import cn.tuyucheng.taketoday.extensions.EmployeeDaoParameterResolver;
import cn.tuyucheng.taketoday.extensions.EmployeeDatabaseSetupExtension;
import cn.tuyucheng.taketoday.extensions.EnvironmentExtension;
import cn.tuyucheng.taketoday.helpers.Employee;
import cn.tuyucheng.taketoday.helpers.EmployeeJdbcDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({EnvironmentExtension.class, EmployeeDaoParameterResolver.class})
public class ProgrammaticEmployeesUnitTest {

    @RegisterExtension
    static EmployeeDatabaseSetupExtension DB = new EmployeeDatabaseSetupExtension("jdbc:h2:mem:AnotherDb;DB_CLOSE_DELAY=-1", "org.h2.Driver", "sa", "");
    private EmployeeJdbcDao employeeDao;

    public ProgrammaticEmployeesUnitTest(EmployeeJdbcDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Test
    public void whenAddEmployee_thenGetEmployee() throws SQLException {
        Employee emp = new Employee(1, "john");
        employeeDao.add(emp);
        assertEquals(1, employeeDao.findAll().size());
    }

    @Test
    public void whenGetEmployees_thenEmptyList() throws SQLException {
        assertEquals(0, employeeDao.findAll().size());
    }
}