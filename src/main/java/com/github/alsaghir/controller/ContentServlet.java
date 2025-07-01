package com.github.alsaghir.controller;

import com.github.alsaghir.config.LifecycleManger;
import com.github.alsaghir.repository.DepartmentRepository;
import com.github.alsaghir.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class ContentServlet extends ParentServlet {

  private static final Logger log = LoggerFactory.getLogger(ContentServlet.class);

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.info("info doGet");
    log.trace("trace doGet");
    log.warn("warn doGet");
    log.debug("debug doGet");
    WebContext context = getWebContext(request, response);
    Connection conn = (Connection) request.getServletContext().getAttribute(LifecycleManger.DB_CONNECTION_ATTR);
    EmployeeRepository employeeRepository = new EmployeeRepository(conn);
    DepartmentRepository departmentRepository = new DepartmentRepository(conn);
    try {
      context.setVariable("departments", departmentRepository.findAll());
      context.setVariable("employees", employeeRepository.findAll());
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }

    getEngine().process("content.html", context, response.getWriter());
  }

}