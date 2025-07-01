package com.github.alsaghir.repository;

import com.github.alsaghir.model.Department;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

class DepartmentRepositoryITest {
    private Connection connection;
    private DepartmentRepository departmentRepository;

    @BeforeEach
    void setUp() throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        connection.createStatement()
                .execute("CREATE TABLE DEPARTMENT (ID BIGINT AUTO_INCREMENT PRIMARY KEY, NAME VARCHAR(255))");
        departmentRepository = new DepartmentRepository(connection);
    }

    @AfterEach
    void tearDown() throws SQLException {
        connection.createStatement().execute("DROP TABLE DEPARTMENT");
        connection.close();
    }

    @Test
    void testAddDepartment() throws SQLException {
        Department department = new Department(null, "HR");
        departmentRepository.add(department);

        Map<Long, Department> departments = departmentRepository.findAll();
        Assertions.assertEquals(1, departments.size());
        Department retrievedDepartment = departments.values().iterator().next();
        Assertions.assertEquals("HR", retrievedDepartment.name());
    }

    @Test
    void testFindById() throws SQLException {
        Department department = new Department(null, "Finance");
        departmentRepository.add(department);

        Department retrievedDepartment = departmentRepository.findById(1L);
        Assertions.assertNotNull(retrievedDepartment);
        Assertions.assertEquals("Finance", retrievedDepartment.name());
    }

    @Test
    void testUpdateDepartment() throws SQLException {
        Department department = new Department(null, "IT");
        departmentRepository.add(department);

        Department updatedDepartment = new Department(1L, "Information Technology");
        departmentRepository.update(updatedDepartment);

        Department retrievedDepartment = departmentRepository.findById(1L);
        Assertions.assertEquals("Information Technology", retrievedDepartment.name());
    }

    @Test
    void testDeleteDepartment() throws SQLException {
        Department department = new Department(null, "Marketing");
        departmentRepository.add(department);

        departmentRepository.delete(1L);

        Map<Long, Department> departments = departmentRepository.findAll();
        Assertions.assertTrue(departments.isEmpty());
    }
}
