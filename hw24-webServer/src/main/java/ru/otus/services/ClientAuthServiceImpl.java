package ru.otus.services;

import ru.otus.core.repository.ClientDao;

public class ClientAuthServiceImpl implements ClientAuthService {

    private final ClientDao clientDao;

    public ClientAuthServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public boolean authenticate(String login, String password) {
        return clientDao.findByLogin(login).stream().anyMatch(client -> client.getPassword().equals(password));
    }
}
