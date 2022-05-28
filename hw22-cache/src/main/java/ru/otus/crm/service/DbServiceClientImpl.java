package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = new MyCache<>(List.of(new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
                log.info("Cache action: {}, key: {}, value: {}", action, key, value);
            }
        }));
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                Long clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                cache.put(clientId.toString(), createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            cache.put(client.getId().toString(), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(Long id) {
        if (cache.get(String.valueOf(id)) != null) {
            return cache.get(String.valueOf(id));
        }
           return transactionRunner.doInTransaction(connection -> {
                        var clientOptional = dataTemplate.findById(connection, id);
                        clientOptional.ifPresent(client -> cache.put(client.getId().toString(), client));
                        return clientOptional;
                    });
    }

    public Optional<Client> getClientWithoutCache(long id) {
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }

    @Override
    public long cacheSize() {
        return cache.cacheSize();
    }
}
