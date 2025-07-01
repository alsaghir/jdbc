package com.github.alsaghir.controller;

import com.github.alsaghir.config.LifecycleManger;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;


public abstract class ParentServlet extends HttpServlet {

    private transient TemplateEngine engine;
    private transient JakartaServletWebApplication application;

    public TemplateEngine getEngine() {
        return engine;
    }

    public JakartaServletWebApplication getApplication() {
        return application;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        var context = config.getServletContext();
        this.engine = (TemplateEngine) context.getAttribute(LifecycleManger.TEMPLATE_ENGINE_ATTR);
        this.application = (JakartaServletWebApplication) context.getAttribute(LifecycleManger.APPLICATION_ATTR);
    }

    protected WebContext getWebContext(HttpServletRequest request, HttpServletResponse response) {
        return new WebContext(getApplication().buildExchange(request, response));
    }

    protected static void redirectToHome(HttpServletResponse response) throws IOException {
        response.sendRedirect("/content");
    }

}