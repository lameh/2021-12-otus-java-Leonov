package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.core.repository.ClientDao;
import ru.otus.crm.model.Client;

import java.io.IOException;
import java.util.stream.Collectors;

public class ClientCreateApiServlet extends HttpServlet {

    private final ClientDao clientDao;
    private final Gson gson;

    public ClientCreateApiServlet(ClientDao clientDao, Gson gson) {
        this.clientDao = clientDao;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
        var client = clientDao.save(extractClientFromRequest(httpServletRequest));
    }

    private Client extractClientFromRequest(HttpServletRequest httpServletRequest) throws IOException {
        String json = httpServletRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return gson.fromJson(json, Client.class);
    }
}
