package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.core.repository.ClientDao;
import ru.otus.crm.model.Client;

import java.io.IOException;
import java.util.List;

public class AllClientsShowApiServlet extends HttpServlet {

    private final ClientDao clientDao;
    private final Gson gson;

    public AllClientsShowApiServlet(ClientDao clientDao, Gson gson) {
        this.clientDao = clientDao;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream servletOutputStream = response.getOutputStream();
        List<Client> clientList = clientDao.findAll();
        servletOutputStream.print(gson.toJson(clientList));
    }
}
