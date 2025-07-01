package com.github.alsaghir.config;

import com.github.alsaghir.controller.ContentServlet;
import com.github.alsaghir.controller.DepartmentServlet;
import com.github.alsaghir.controller.EmployeeServlet;
import com.github.alsaghir.controller.IndexServlet;
import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ListenerInfo;
import jakarta.servlet.ServletException;

/**
 * Represents the main entry point for the servlet container management. This class is responsible
 * for initializing the servlet container and deploying the application.
 */
public class ServletContainerManager {

  public void initializeServlet() throws ServletException {
    // @formatter:off
    DeploymentInfo servletBuilder =
        Servlets.deployment()
            .setClassLoader(ServletContainerManager.class.getClassLoader())
            .setDeploymentName("DepartmentEmployeeApp.war")
            .setResourceManager(new ClassPathResourceManager(ServletContainerManager.class.getClassLoader()))
            .setContextPath("/")
            .addServlets(Servlets.servlet("home", IndexServlet.class).addMapping("/home"))
            .addServlets(Servlets.servlet("content", ContentServlet.class).addMapping("/content"))
            .addServlets(Servlets.servlet("departments", DepartmentServlet.class).addMapping("/departments").addMapping("/departments/*"))
            .addServlets(Servlets.servlet("employees", EmployeeServlet.class).addMapping("/employees").addMapping("/employees/*"))
    // @formatter:on

                // Very important: add the LifecycleManger listener to handle application lifecycle
                // events
                .addListener(new ListenerInfo(LifecycleManger.class));

    DeploymentManager manager = Servlets.defaultContainer().addDeployment(servletBuilder);
    manager.deploy();

    HttpHandler servletHandler = manager.start();
    PathHandler pathHandler =
            Handlers.path()
                    .addExactPath("/", Handlers.redirect("/home")) // Redirect root to /home
                    // All other paths go to the servlet handler for other servlets mappings
                    .addPrefixPath("/", servletHandler);

    Undertow server =
            Undertow.builder().addHttpListener(8080, "0.0.0.0").setHandler(pathHandler).build();
    server.start();
  }
}
