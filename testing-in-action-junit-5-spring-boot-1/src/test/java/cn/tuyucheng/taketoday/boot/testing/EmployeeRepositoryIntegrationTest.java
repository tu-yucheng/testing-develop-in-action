package cn.tuyucheng.taketoday.boot.testing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class EmployeeRepositoryIntegrationTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    @DisplayName("whenFindByName_thenReturnEmployee")
    void whenFindByName_thenReturnEmployee() {
        Employee alex = new Employee("alex");
        entityManager.persistAndFlush(alex);
        Employee found = employeeRepository.findByName(alex.getName());
        assertThat(found.getName()).isEqualTo(alex.getName());
    }

    @Test
    @DisplayName("whenInvalidName_thenReturnNull")
    public void whenInvalidName_thenReturnNull() {
        Employee fromDb = employeeRepository.findByName("doesNotExists");
        assertThat(fromDb).isNull();
    }

    @Test
    @DisplayName("whenFindById_thenReturnEmployee")
    public void whenFindById_thenReturnEmployee() {
        Employee employee = new Employee("test");
        entityManager.persist(employee);
        Employee fromDb = employeeRepository.findById(employee.getId()).orElseGet(Employee::new);
        assertThat(fromDb.getName()).isEqualTo(employee.getName());
    }

    @Test
    @DisplayName("whenInvalidId_thenReturnNull")
    public void whenInvalidId_thenReturnNull() {
        Employee fromDb = employeeRepository.findById(-11L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    @DisplayName("givenSetOfEmployees_whenFindAll_thenReturnAllEmployees")
    public void givenSetOfEmployees_whenFindAll_thenReturnAllEmployees() {
        Employee alex = new Employee("alex");
        Employee ron = new Employee("ron");
        Employee bob = new Employee("bob");

        entityManager.persist(alex);
        entityManager.persist(ron);
        entityManager.persist(bob);
        entityManager.flush();
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(3).extracting(Employee::getName).containsOnly(alex.getName(), bob.getName(), ron.getName());
    }
}