package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.services.ClientAuthService;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Collections;

public class LoginServlet extends HttpServlet {

    private static final String PARAM_LOGIN = "login";
    private static final String PARAM_PASSWORD = "password";
    private static final int MAX_INACTIVE_INTERVAL = 30;
    private static final String LOGIN_PAGE_TEMPLATE = "login.html";

    private final TemplateProcessor templateProcessor;
    private final ClientAuthService clientAuthService;

    public LoginServlet(TemplateProcessor templateProcessor, ClientAuthService clientAuthService) {
        this.templateProcessor = templateProcessor;
        this.clientAuthService = clientAuthService;
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("text/html");
        httpServletResponse.getWriter().println(templateProcessor.getPage(LOGIN_PAGE_TEMPLATE, Collections.emptyMap()));
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        String login = httpServletRequest.getParameter(PARAM_LOGIN);
        String passw = httpServletRequest.getParameter(PARAM_PASSWORD);

        if (clientAuthService.authenticate(login, passw)) {
            HttpSession session = httpServletRequest.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            httpServletResponse.sendRedirect("/admin");
        }
    }
}
