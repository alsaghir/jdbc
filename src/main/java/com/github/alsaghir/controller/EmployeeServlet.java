package com.github.alsaghir.controller;

import com.github.alsaghir.config.LifecycleManger;
import com.github.alsaghir.model.Employee;
import com.github.alsaghir.repository.DepartmentRepository;
import com.github.alsaghir.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class EmployeeServlet extends ParentServlet {

    public static final String ACTION = "action";
    public static final String ID = "id";
    public static final String EDIT_EMPLOYEE_HTML = "edit_employee.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = getWebContext(request, response);
        Connection conn = (Connection) request.getServletContext().getAttribute(LifecycleManger.DB_CONNECTION_ATTR);
        EmployeeRepository employeeRepository = new EmployeeRepository(conn);
        DepartmentRepository departmentRepository = new DepartmentRepository(conn);

        String action = request.getParameter(ACTION);

        try {
            if (Action.ADD.name().equalsIgnoreCase(action)) {
                context.setVariable("departments", departmentRepository.findAll());
                getEngine().process(EDIT_EMPLOYEE_HTML, context, response.getWriter());
            } else if (Action.DELETE.name().equalsIgnoreCase(action)) {
                Long id = Long.parseLong(request.getParameter(ID));
                employeeRepository.delete(id);
                redirectToHome(response);
            } else if (Action.EDIT.name().equalsIgnoreCase(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                context.setVariable("employee", employeeRepository.findById(id));
                context.setVariable("departments", departmentRepository.findAll());
                getEngine().process(EDIT_EMPLOYEE_HTML, context, response.getWriter());
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
        EmployeeRepository employeeRepository = new EmployeeRepository(conn);

        try {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            Long departmentId = Long.parseLong(request.getParameter("departmentId"));

            if (Action.ADD.name().equalsIgnoreCase(action)) {
                employeeRepository.add(new Employee(null, firstName, lastName, departmentId));
            } else if (Action.EDIT.name().equalsIgnoreCase(action)) {
                Long id = Long.parseLong(request.getParameter("id"));
                employeeRepository.update(new Employee(id, firstName, lastName, departmentId));
            }
            redirectToHome(response);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }



}
