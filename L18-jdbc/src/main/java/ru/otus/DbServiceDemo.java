package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.model.Client;
import ru.otus.core.service.DbServiceClientImpl;
import ru.otus.demo.DataSourceDemo;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.ClientDaoJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.Optional;



/**
 * @author sergey
 * created on 03.02.19.
 */
// этот класс не должен быть в домашней работе
public class DbServiceDemo {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

    public static void main(String[] args) {
        var dataSource = new DataSourceDemo();
        flywayMigrations(dataSource);

        var sessionManager = new SessionManagerJdbc(dataSource);
        DbExecutorImpl<Client> dbExecutor = new DbExecutorImpl<>();
        var clientDao = new ClientDaoJdbc(sessionManager, dbExecutor);

        var dbServiceClient = new DbServiceClientImpl(clientDao);
        var id = dbServiceClient.saveClient(new Client(0, "dbServiceClient"));
        Optional<Client> clientOpt = dbServiceClient.getClient(id);

        clientOpt.ifPresentOrElse(
                client -> logger.info("created client, name:{}", client.getName()),
                () -> logger.info("client was not created")
        );
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
