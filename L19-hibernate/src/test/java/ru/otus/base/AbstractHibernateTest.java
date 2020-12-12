package ru.otus.base;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.otus.core.model.Client;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;

import static ru.otus.DbServiceDemo.HIBERNATE_CFG_FILE;

public abstract class AbstractHibernateTest {
    protected static final String TEST_CLIENT_NAME = "Вася";
    protected static final String TEST_CLIENT_NEW_NAME = "НЕ Вася";
    protected static final String TEST_CLIENT_NEW_NAME2 = "Совершенно точно НЕ Вася";

    protected SessionFactory sessionFactory;
    private static TestContainersConfig.CustomPostgreSQLContainer CONTAINER;

    @BeforeAll
    public static void init() {
        CONTAINER = TestContainersConfig.CustomPostgreSQLContainer.getInstance();
        CONTAINER.start();
    }

    @AfterAll
    public static void shutdown() {
        CONTAINER.stop();
    }

    @BeforeEach
    public void setUp() {
        String dbUrl = System.getProperty("app.datasource.demo-db.jdbcUrl");
        String dbUserName = System.getProperty("app.datasource.demo-db.username");
        String dbPassword = System.getProperty("app.datasource.demo-db.password");

        var migrationsExecutor = new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword);
        migrationsExecutor.cleanDb();
        migrationsExecutor.executeMigrations();

        Configuration configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        configuration.setProperty("hibernate.connection.url", dbUrl);
        configuration.setProperty("hibernate.connection.username", dbUserName);
        configuration.setProperty("hibernate.connection.password", dbPassword);

        sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class);
    }

    @AfterEach
    void tearDown() {
        sessionFactory.close();
    }

    protected Client buildDefaultClient() {
        return new Client(0, TEST_CLIENT_NAME);
    }

    protected void saveClient(Client client) {
        try (Session session = sessionFactory.openSession()) {
            saveClient(session, client);
        }
    }

    protected void saveClient(Session session, Client client) {
        session.beginTransaction();
        session.save(client);
        session.getTransaction().commit();
    }

    protected Client loadClient(long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Client.class, id);
        }
    }

    protected EntityStatistics getUsageStatistics() {
        Statistics stats = sessionFactory.getStatistics();
        return stats.getEntityStatistics(Client.class.getName());
    }
}
