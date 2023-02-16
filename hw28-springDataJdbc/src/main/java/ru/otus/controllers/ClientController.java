package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClient;

import java.util.List;

@Controller
public class ClientController {
    private final DbServiceClient clientService;

    public ClientController(DbServiceClient clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/"})
    public String clientsListView(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clientsList";
    }
}
