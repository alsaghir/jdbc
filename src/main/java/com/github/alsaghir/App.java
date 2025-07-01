package com.github.alsaghir;


import com.github.alsaghir.config.ServletContainerManager;
import jakarta.servlet.ServletException;

public class App {
    public static void main(String[] args) throws ServletException {
        // Initialize the servlet container manager
        // Main entry point for the application
        ServletContainerManager servletContainerManager = new ServletContainerManager();
        servletContainerManager.initializeServlet();
    }
}


