package cn.tuyucheng.taketoday.junit.tags.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class EmployeeDAO {
    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("employee");
    }

    public int getCountOfEmployees() {
        return jdbcTemplate.queryForObject("select count(*) from employee", Integer.class);
    }

    public List<Employee> getAllEmployees() {
        return jdbcTemplate.query("select * from employee", new EmployeeRowMapper());
    }

    public int addEmplyee(final int id) {
        return jdbcTemplate.update("insert into employee values (?, ?, ?, ?)", id, "Bill", "Gates", "USA");
    }

    public int addEmplyeeUsingSimpelJdbcInsert(final Employee employee) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", employee.getId());
        parameters.put("first_name", employee.getFirstName());
        parameters.put("last_name", employee.getLastName());
        parameters.put("address", employee.getAddress());
        return simpleJdbcInsert.execute(parameters);
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}