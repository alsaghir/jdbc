package com.github.alsaghir.repository;

import com.github.alsaghir.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EmployeeRepository {

  private final Connection connection;

  public EmployeeRepository(Connection connection) {
    this.connection = connection;
  }

  public Map<Long, Employee> findAll() throws SQLException {
    PreparedStatement statement = connection.prepareStatement("SELECT * FROM EMPLOYEE");
    ResultSet result = statement.executeQuery();
    Map<Long, Employee> employees = new HashMap<>();
    while (result.next()) {
      Employee emp = new Employee(result.getLong(1), result.getString(2), result.getString(3), result.getLong(4));
      employees.put(result.getLong(1), emp);
    }
    statement.close();
    return employees;
  }

  public Employee findById(Long id) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("SELECT * FROM EMPLOYEE WHERE ID = ?");
    statement.setLong(1, id);
    ResultSet result = statement.executeQuery();
    Employee emp = null;
    if (result.next()) {
      emp = new Employee(result.getLong(1), result.getString(2), result.getString(3), result.getLong(4));
    }
    statement.close();
    return emp;
  }

  public void update(Employee employee) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("UPDATE EMPLOYEE SET FIRST_NAME = ?, LAST_NAME = ?, D_ID = ? WHERE ID = ?");
    statement.setString(1, employee.firstName());
    statement.setString(2, employee.lastName());
    statement.setLong(3, employee.DepartmentId());
    statement.setLong(4, employee.id());
    statement.executeUpdate();
    statement.close();
  }

  public void delete(Long id) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("DELETE FROM EMPLOYEE WHERE ID = ?");
    statement.setLong(1, id);
    statement.executeUpdate();
    statement.close();
  }

  public void add(Employee employee) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("INSERT INTO EMPLOYEE (FIRST_NAME, LAST_NAME, D_ID) VALUES (?, ?, ?)");
    statement.setString(1, employee.firstName());
    statement.setString(2, employee.lastName());
    statement.setLong(3, employee.DepartmentId());
    statement.executeUpdate();
    statement.close();
  }

}
