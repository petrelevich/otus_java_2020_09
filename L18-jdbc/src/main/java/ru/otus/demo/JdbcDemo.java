package ru.otus.demo;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * @author sergey
 * created on 01.10.18.
 */
// этот класс не должен быть в домашней работе
public class JdbcDemo {
    private static final Logger logger = LoggerFactory.getLogger(JdbcDemo.class);

    public static void main(String[] args) throws SQLException {
        var dataSource = new DataSourceDemo();
        flywayMigrations(dataSource);
        JdbcDemo demo = new JdbcDemo();
        try (var connection = getConnection(dataSource)) {
            int id = 1;
            demo.insertRecord(connection, id);
            demo.selectRecord(connection, id);
        }
    }

    private static Connection getConnection(DataSource dataSource) throws SQLException {
        var connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    private void insertRecord(Connection connection, int id) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("insert into test(id, name) values (?, ?)")) {
            Savepoint savePoint = connection.setSavepoint("savePointName");
            pst.setInt(1, id);
            pst.setString(2, "NameValue");
            try {
                int rowCount = pst.executeUpdate(); //Блокирующий вызов
                connection.commit();
                logger.info("inserted rowCount: {}", rowCount);
            } catch (SQLException ex) {
                connection.rollback(savePoint);
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private void selectRecord(Connection connection, int id) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("select name from test where id  = ?")) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    var name = rs.getString("name");
                    logger.info("name:{}", name);
                }
            }
        }
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
