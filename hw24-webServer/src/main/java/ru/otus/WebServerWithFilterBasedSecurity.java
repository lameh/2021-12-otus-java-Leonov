package ru.otus;

import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.ClientDao;
import ru.otus.core.repository.ClientDaoImpl;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.server.ClientWebServerWithFilterBasedSecurity;
import ru.otus.services.ClientAuthServiceImpl;
import ru.otus.services.TemplateProcessorImpl;

/**
 * Стартовая страница:
 * http://localhost:8080
 *
 * Стартовая страница логина:
 * http://localhost:8080/login
 *
 * Страница операций с клиентами:
 * http://localhost:8080/clients
 *
 * REST сервис:
 * http://localhost:8080/api/client/3
 */

public class WebServerWithFilterBasedSecurity {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

        var clientDao = initClientDao();
        var clientAuthService = new ClientAuthServiceImpl(clientDao);
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

        var webServer = new ClientWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, clientDao, gson, templateProcessor, clientAuthService);
        webServer.start();
        webServer.join();
    }

    private static ClientDao initClientDao() {

        var config = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = config.getProperty("hibernate.connection.url");
        var dbUserName = config.getProperty("hibernate.connection.username");
        var dbPassword = config.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
        var sessionFactory = HibernateUtils.buildSessionFactory(config, Client.class, Address.class, Phone.class);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);

        return new ClientDaoImpl(clientTemplate, transactionManager);
    }
}
