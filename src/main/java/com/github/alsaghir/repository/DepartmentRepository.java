package com.github.alsaghir.repository;

import com.github.alsaghir.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DepartmentRepository {

  private final Connection connection;

  public DepartmentRepository(Connection connection) {
    this.connection = connection;
  }

  public Map<Long, Department> findAll() throws SQLException {
    PreparedStatement statement = connection.prepareStatement("SELECT * FROM DEPARTMENT");
    ResultSet result = statement.executeQuery();
    Map<Long, Department> departments = new HashMap<>();
    while (result.next()) {
      Department department = new Department(result.getLong(1), result.getString(2));
      departments.put(result.getLong(1), department);
    }
    statement.close();
    return departments;
  }

  public Department findById(Long id) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("SELECT * FROM DEPARTMENT WHERE ID = ?");
    statement.setLong(1, id);
    ResultSet result = statement.executeQuery();
    Department department = null;
    if (result.next()) {
      department = new Department(result.getLong(1), result.getString(2));
    }
    statement.close();
    return department;
  }

  public void update(Department department) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("UPDATE DEPARTMENT SET NAME = ? WHERE ID = ?");
    statement.setString(1, department.name());
    statement.setLong(2, department.id());
    statement.executeUpdate();
    statement.close();
  }

  public void delete(Long id) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("DELETE FROM DEPARTMENT WHERE ID = ?");
    statement.setLong(1, id);
    statement.executeUpdate();
    statement.close();
  }

  public void add(Department department) throws SQLException {
    PreparedStatement statement = connection.prepareStatement("INSERT INTO DEPARTMENT (NAME) VALUES (?)");
    statement.setString(1, department.name());
    statement.executeUpdate();
    statement.close();
  }

}
