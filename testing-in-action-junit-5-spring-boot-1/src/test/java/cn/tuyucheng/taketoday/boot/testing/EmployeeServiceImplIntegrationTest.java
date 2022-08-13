package cn.tuyucheng.taketoday.boot.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
// @Import(EmployeeServiceImplIntegrationTest.EmployeeServiceImplTestContextConfiguration.class)
public class EmployeeServiceImplIntegrationTest {
    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setup() {
        Employee john = new Employee("john");
        john.setId(11L);

        Employee bob = new Employee("bob");
        Employee alex = new Employee("alex");
        List<Employee> allEmployees = Arrays.asList(john, bob, alex);

        Mockito.when(employeeRepository.findByName(john.getName())).thenReturn(john);
        Mockito.when(employeeRepository.findByName(alex.getName())).thenReturn(alex);
        Mockito.when(employeeRepository.findById(john.getId())).thenReturn(Optional.of(john));
        Mockito.when(employeeRepository.findByName("wrong_name")).thenReturn(null);
        Mockito.when(employeeRepository.findAll()).thenReturn(allEmployees);
        Mockito.when(employeeRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("whenValidName_thenEmployeeShouldBeFound")
    void whenValidName_thenEmployeeShouldBeFound() {
        String name = "alex";
        Employee foundEmployee = employeeService.getEmployeeByName(name);
        assertThat(foundEmployee).isNotNull();
        assertThat(foundEmployee.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("whenInValidName_thenEmployeeShouldNotFound")
    void whenInValidName_thenEmployeeShouldNotFound() {
        Employee fromDb = employeeService.getEmployeeByName("wrong_name");
        assertThat(fromDb).isNull();
        verifyFindByNameIsCalledOnce("wrong_name");
    }

    @Test
    void whenInValidName_thenEmployeeShouldNotBeFound() {
        Employee fromDb = employeeService.getEmployeeByName("wrong_name");
        assertThat(fromDb).isNull();
        verifyFindByNameIsCalledOnce("wrong_name");
    }

    @Test
    public void whenValidName_thenEmployeeShouldExist() {
        boolean doesEmployeeExists = employeeService.exists("john");
        assertThat(doesEmployeeExists).isEqualTo(true);
        verifyFindByNameIsCalledOnce("john");
    }

    @Test
    public void whenNonExistingName_thenEmployeeShouldNotExists() {
        boolean doseEmployeeExists = employeeService.exists("some_name");
        assertThat(doseEmployeeExists).isEqualTo(false);
        verifyFindByNameIsCalledOnce("some_name");
    }

    @Test
    public void whenValidId_thenEmployeeShouldBeFound() {
        Employee fromDb = employeeService.getEmployeeById(11L);
        assertThat(fromDb.getName()).isEqualTo("john");
        verifyFindByIdIsCalledOnce();
    }

    @Test
    public void whenInValidId_thenEmployeeShouldNotBeFound() {
        Employee fromDb = employeeService.getEmployeeById(-123L);
        verifyFindByIdIsCalledOnce();
        assertThat(fromDb).isNull();
    }

    @Test
    public void given3Employees_whenGetAll_thenReturn3Records() {
        Employee alex = new Employee("alex");
        Employee john = new Employee("john");
        Employee bob = new Employee("bob");

        List<Employee> allEmployees = employeeService.getAllEmployees();
        assertThat(allEmployees).hasSize(3).extracting(Employee::getName).contains(alex.getName(), john.getName(), bob.getName());
        verifyFindAllEmployeesIsCalledOnce();
    }

    private void verifyFindByNameIsCalledOnce(String name) {
        Mockito.verify(employeeRepository, VerificationModeFactory.times(1)).findByName(name);
        Mockito.reset(employeeRepository);
    }

    private void verifyFindByIdIsCalledOnce() {
        Mockito.verify(employeeRepository, VerificationModeFactory.times(1)).findById(Mockito.anyLong());
        Mockito.reset(employeeRepository);
    }

    private void verifyFindAllEmployeesIsCalledOnce() {
        Mockito.verify(employeeRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(employeeRepository);
    }

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public EmployeeService employeeService() {
            return new EmployeeServiceImpl();
        }
    }
}