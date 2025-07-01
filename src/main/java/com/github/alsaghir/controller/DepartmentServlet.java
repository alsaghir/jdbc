package com.github.alsaghir.controller;

import com.github.alsaghir.config.LifecycleManger;
import com.github.alsaghir.model.Department;
import com.github.alsaghir.repository.DepartmentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DepartmentServlet extends ParentServlet {

  public static final String ACTION = "action";
  public static final String NAME = "name";
  public static final String ID = "id";
  public static final String EDIT_DEPARTMENT_HTML = "edit_department.html";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    WebContext context = getWebContext(request, response);
    Connection conn = (Connection) request.getServletContext().getAttribute(LifecycleManger.DB_CONNECTION_ATTR);
    DepartmentRepository departmentRepository = new DepartmentRepository(conn);

    String action = request.getParameter(ACTION);

    try {
      if (Action.ADD.name().equalsIgnoreCase(action)) {
        getEngine().process(EDIT_DEPARTMENT_HTML, context, response.getWriter());
      } else if (Action.DELETE.name().equalsIgnoreCase(action)) {
        Long departmentId = Long.parseLong(request.getParameter(ID));
        try {
          departmentRepository.delete(departmentId);
        } catch (SQLException e) {
          throw new IllegalStateException("Cannot delete department with employees");
        }
        redirectToHome(response);
      } else if (Action.EDIT.name().equalsIgnoreCase(action)) {
        Department departmentToEdit = departmentRepository.findById(Long.parseLong(request.getParameter(ID)));
        context.setVariable("department", departmentToEdit);
        getEngine().process(EDIT_DEPARTMENT_HTML, context, response.getWriter());
      } else {
        redirectToHome(response);
      }
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String action = request.getParameter(ACTION);
    Connection conn = (Connection) request.getServletContext().getAttribute(LifecycleManger.DB_CONNECTION_ATTR);
    DepartmentRepository departmentRepository = new DepartmentRepository(conn);

    try {
      String name = request.getParameter(NAME);

      if (Action.ADD.name().equalsIgnoreCase(action)) {
        departmentRepository.add(new Department(null, name));
      } else if (Action.EDIT.name().equalsIgnoreCase(action)) {
        Long id = Long.parseLong(request.getParameter("id"));
        departmentRepository.update(new Department(id, name));
      }
      redirectToHome(response);
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }
}
