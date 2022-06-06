package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {

        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная
        HwCache<String, Client> cache = new MyCache<>();

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, cache);

        List<Long> clientIds = new ArrayList<>();

        for (int i = 0; i < 30000; i++) {
            clientIds.add(dbServiceClient.saveClient(new Client("client" + i)).getId());
            /*if (i % 100 == 0) {
                System.out.println("Save clients -> Cache size = " + dbServiceClient.cacheSize());
            }*/
        }

        long startTime = System.currentTimeMillis();

        for (long id : clientIds) {
            dbServiceClient.getClient(id);
//            dbServiceClient.getClientWithoutCache(id);
            /*if (id % 100 == 0) {
                System.out.println("Get clients -> Cache size = " + dbServiceClient.cacheSize());
            }*/
        }
        log.info("With cache usage, msec: {}", System.currentTimeMillis() - startTime);
//        log.info("Without cache usage, msec: {}", System.currentTimeMillis() - startTime);
    }
    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
