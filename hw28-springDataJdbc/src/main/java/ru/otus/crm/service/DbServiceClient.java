package ru.otus.crm.service;

import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface DbServiceClient {
    Client saveClient(Client client);

    Client getClient(String id);

    List<Client> findAll();

    Client findById(String id);

    Optional<Client> findByName(String name);
}
