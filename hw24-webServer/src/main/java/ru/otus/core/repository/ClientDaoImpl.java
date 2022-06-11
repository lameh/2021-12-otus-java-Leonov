package ru.otus.core.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.List;
import java.util.Optional;

public class ClientDaoImpl implements ClientDao {

    private static final Logger log = LoggerFactory.getLogger(ClientDaoImpl.class);

    private final DataTemplate<Client> clientDataTemplate;

    private final TransactionManager transactionManager;

    public ClientDaoImpl(DataTemplate<Client> clientDataTemplate, TransactionManager transactionManager) {
        this.clientDataTemplate = clientDataTemplate;
        this.transactionManager = transactionManager;
        saveInDb();
    }


    @Override
    public Optional<Client> findById(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOpt = clientDataTemplate.findById(session, id);
            log.info("Found client by id: {}", clientOpt);
            return clientOpt;
        });
    }

    @Override
    public List<Client> findByLogin(String login) {
        return transactionManager.doInTransaction(session ->
            clientDataTemplate.findByEntityField(session, "login", login)
        );
    }

    @Override
    public Client save(Client client) {
        return transactionManager.doInTransaction(session -> {
            var cloneClient = client.clone();
            if (client.getId() == null) {
                clientDataTemplate.insert(session, cloneClient);
                return cloneClient;
            }
            clientDataTemplate.update(session, cloneClient);
            log.info("Update client: {}", cloneClient);
            return cloneClient;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("Found list of clients: {}", clientList);
            return clientList;
        });
    }

    private void saveInDb() {
        save(new Client(null, "Thomas Shelby", "client1", "1111111",
                new Address(null, "street1"), List.of(new Phone(null, "9111111111"))));
        save(new Client(null, "Arthur Shelby", "client2", "1111111",
                new Address(null, "street2"), List.of(new Phone(null, "9222222222"),
                new Phone(null, "9222222223"))));
        save(new Client(null, "Freddie Thorne", "client3", "1111111",
                new Address(null, "street3"), List.of(new Phone(null, "9333333333"))));
        save(new Client(null, "Alfred Solomons", "client4", "1111111",
                new Address(null, "street4"), List.of(new Phone(null, "9444444444"))));
        save(new Client(null, "Luca Changretta", "client5", "1111111",
                new Address(null, "street5"), List.of(new Phone(null, "9555555555"))));
        save(new Client(null, "Grace Shelby", "client6", "1111111",
                new Address(null, "street6"), List.of(new Phone(null, "9666666666"))));
        save(new Client(null, "Elizabeth Shelby", "client7", "1111111",
                new Address(null, "street7"), List.of(new Phone(null, "9777777777"))));
    }
}
