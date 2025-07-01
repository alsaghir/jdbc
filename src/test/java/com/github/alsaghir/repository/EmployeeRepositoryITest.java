package com.github.alsaghir.repository;

import com.github.alsaghir.model.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

class EmployeeRepositoryITest {
    private Connection connection;
    private EmployeeRepository employeeRepository;

    @BeforeEach
     void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        connection.createStatement()
                .execute("CREATE TABLE EMPLOYEE (ID BIGINT AUTO_INCREMENT PRIMARY KEY, FIRST_NAME VARCHAR(255), LAST_NAME VARCHAR(255), D_ID BIGINT)");
        employeeRepository = new EmployeeRepository(connection);
    }

    @AfterEach
     void tearDown() throws SQLException {
        connection.createStatement().execute("DROP TABLE EMPLOYEE");
        connection.close();
    }

    @Test
     void testAddEmployee() throws SQLException {
        Employee employee = new Employee(null, "John", "Doe", 1L);
        employeeRepository.add(employee);

        Map<Long, Employee> employees = employeeRepository.findAll();
        Assertions.assertEquals(1, employees.size());
        Employee retrievedEmployee = employees.values().iterator().next();
        Assertions.assertEquals("John", retrievedEmployee.firstName());
        Assertions.assertEquals("Doe", retrievedEmployee.lastName());
        Assertions.assertEquals(1L, retrievedEmployee.DepartmentId());
    }

    @Test
    void testFindById() throws SQLException {
        Employee employee = new Employee(null, "Jane", "Doe", 2L);
        employeeRepository.add(employee);

        Employee retrievedEmployee = employeeRepository.findById(1L);
        Assertions.assertNotNull(retrievedEmployee);
        Assertions.assertEquals("Jane", retrievedEmployee.firstName());
        Assertions.assertEquals("Doe", retrievedEmployee.lastName());
        Assertions.assertEquals(2L, retrievedEmployee.DepartmentId());
    }

    @Test
    void testUpdateEmployee() throws SQLException {
        Employee employee = new Employee(null, "Alice", "Smith", 3L);
        employeeRepository.add(employee);

        Employee updatedEmployee = new Employee(1L, "Alice", "Johnson", 3L);
        employeeRepository.update(updatedEmployee);

        Employee retrievedEmployee = employeeRepository.findById(1L);
        Assertions.assertEquals("Alice", retrievedEmployee.firstName());
        Assertions.assertEquals("Johnson", retrievedEmployee.lastName());
    }

    @Test
    void testDeleteEmployee() throws SQLException {
        Employee employee = new Employee(null, "Bob", "Brown", 4L);
        employeeRepository.add(employee);

        employeeRepository.delete(1L);

        Map<Long, Employee> employees = employeeRepository.findAll();
        Assertions.assertTrue(employees.isEmpty());
    }
}