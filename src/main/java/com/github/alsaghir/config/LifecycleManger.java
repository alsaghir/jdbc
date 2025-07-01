package com.github.alsaghir.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lifecycle event listener for the Jakarta Servlet web application. Events such as application
 * startup and shutdown are handled here. Database connections and Thymeleaf template engine are
 * initialized here.
 */
public class LifecycleManger implements ServletContextListener {

  public static final String TEMPLATE_ENGINE_ATTR = "com.github.alsaghir.TemplateEngineInstance";
  public static final String APPLICATION_ATTR =
      "com.github.alsaghir.JakartaServletWebApplicationInstance";
  public static final String DB_CONNECTION_ATTR = "com.github.alsaghir.ConnectionInstance";

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContextListener.super.contextInitialized(sce);
    initThymeleaf(sce);
    try {
      initDbConnection(sce);
    } catch (SQLException
        | ClassNotFoundException e) { // Checked exceptions cannot be added to the method signature
      // Instead, we handle them here and throw a runtime exception that will be caught by the
      // servlet container
      throw new IllegalStateException(e);
    }
  }

  /**
   * Initializes the database connection and sets it as a servlet context attribute.
   *
   * @param sce the ServletContextEvent containing the servlet context
   * @throws SQLException if a database access error occurs
   * @throws ClassNotFoundException if the H2 database driver class is not found
   */
  private void initDbConnection(ServletContextEvent sce)
      throws SQLException, ClassNotFoundException {
    Class.forName("org.h2.Driver");
    Connection connection =
        DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/~/test", "u", "p");
    sce.getServletContext().setAttribute(DB_CONNECTION_ATTR, connection);
  }

  /**
   * Initializes the Thymeleaf template engine and sets it as a servlet context attribute.
   *
   * @param sce the ServletContextEvent containing the servlet context
   */
  private static void initThymeleaf(ServletContextEvent sce) {
    JakartaServletWebApplication application =
        JakartaServletWebApplication.buildApplication(sce.getServletContext());
    ITemplateEngine templateEngine = buildTemplateEngine(application);
    sce.getServletContext().setAttribute(TEMPLATE_ENGINE_ATTR, templateEngine);
    sce.getServletContext().setAttribute(APPLICATION_ATTR, application);
  }

  private static ITemplateEngine buildTemplateEngine(final IWebApplication application) {

    // Templates will be resolved as application (ServletContext) resources
    final WebApplicationTemplateResolver templateResolver =
        new WebApplicationTemplateResolver(application);

    // HTML is the default mode, but we will set it anyway for better understanding of code
    templateResolver.setTemplateMode(TemplateMode.HTML);

    // This will convert "home" to "/WEB-INF/templates/home.html"
    templateResolver.setPrefix("/WEB-INF/templates/");
    templateResolver.setSuffix(".html");

    // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by
    // LRU
    templateResolver.setCacheTTLMs(3600000L);

    // Cache is set to true by default. Set to false if you want templates to
    // be automatically updated when modified.
    templateResolver.setCacheable(false);

    final TemplateEngine templateEngine = new TemplateEngine();
    templateEngine.setTemplateResolver(templateResolver);

    return templateEngine;
  }
}
