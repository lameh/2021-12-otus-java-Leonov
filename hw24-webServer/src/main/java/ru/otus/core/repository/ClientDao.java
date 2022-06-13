package ru.otus.core.repository;

import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDao {

    Optional<Client> findById(long id);

    List<Client> findByLogin(String login);

    Client save(Client client);

    List<Client> findAll();
}
