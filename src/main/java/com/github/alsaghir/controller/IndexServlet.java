package com.github.alsaghir.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.WebContext;

import java.io.IOException;

public class IndexServlet extends ParentServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext context = getWebContext(request, response);
        getEngine().process("home.html", context, response.getWriter());
    }

}