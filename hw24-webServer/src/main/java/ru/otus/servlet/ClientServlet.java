package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.core.repository.ClientDao;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;

public class ClientServlet extends HttpServlet {

    private static final String CLIENT_PAGE_TEMPLATE = "admin.html";
    private final ClientDao clientDao;
    private final TemplateProcessor templateProcessor;

    public ClientServlet(ClientDao clientDao, TemplateProcessor templateProcessor) {
        this.clientDao = clientDao;
        this.templateProcessor = templateProcessor;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENT_PAGE_TEMPLATE, Collections.emptyMap()));
    }
}
