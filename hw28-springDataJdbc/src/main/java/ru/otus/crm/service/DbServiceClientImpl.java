package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.Client;
import ru.otus.crm.repository.ClientRepository;
import ru.otus.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServiceClientImpl implements DbServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private final TransactionManager transactionManager;
    private final ClientRepository clientRepository;

    public DbServiceClientImpl(TransactionManager transactionManager, ClientRepository clientRepository) {
        this.transactionManager = transactionManager;
        this.clientRepository = clientRepository;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(() -> {
            Client savedClient = clientRepository.save(client);
            log.info("Client saved {}", savedClient);
            return savedClient;
        });
    }

    @Override
    public Client getClient(String id) {
        Optional<Client> clientOptional = clientRepository.findById(id);
        log.info("Client -> {}", clientOptional);
        return clientOptional.orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Override
    public List<Client> findAll() {
        var clientList = new ArrayList<Client>();
        clientRepository.findAll().forEach(clientList::add);
        log.info("List of clients {}", clientList);
        return clientList;
    }

    public Client findById(String id) {
        Optional<Client> client = clientRepository.findById(id);
        log.info("Client by Id {}", client);
        return client.get();
    }

    public Optional<Client> findByName(String name) {
        var clientOpt = clientRepository.findByName(name);
        log.info("Client by name {}", clientOpt);
        return clientOpt;
    }
}
