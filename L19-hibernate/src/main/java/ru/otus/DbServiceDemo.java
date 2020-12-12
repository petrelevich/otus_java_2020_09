package ru.otus;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.Client;
import ru.otus.core.service.DBServiceClient;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.ClientDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Optional;

public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {
        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String dbUserName = configuration.getProperty("hibernate.connection.username");
        String dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        // Все главное см в тестах
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);


        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        ClientDao userDao = new ClientDaoHibernate(sessionManager);
        DBServiceClient dbServiceClient = new DbServiceClientImpl(userDao);


        long id = dbServiceClient.saveClient(new Client(0, "Вася"));
        Optional<Client> mayBeCreatedClient = dbServiceClient.getClient(id);
        mayBeCreatedClient.ifPresentOrElse((client) -> outputClient("Created client", client),
                () -> logger.info("Client not found"));

        id = dbServiceClient.saveClient(new Client(1L, "А! Нет. Это же совсем не Вася"));
        Optional<Client> mayBeUpdatedClient = dbServiceClient.getClient(id);
        mayBeUpdatedClient.ifPresentOrElse((client) -> outputClient("Updated client", client),
                () -> logger.info("Client not found"));
    }

    private static void outputClient(String header, Client client) {
        logger.info("-----------------------------------------------------------");
        logger.info(header);
        logger.info("client:{}", client);
    }
}
