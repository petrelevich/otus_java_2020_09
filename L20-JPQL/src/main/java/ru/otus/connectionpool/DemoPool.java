package ru.otus.connectionpool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
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
 * created on 08.10.18.
 */
public class DemoPool {
    private static final Logger logger = LoggerFactory.getLogger(DemoPool.class);
    private static final String URL = "jdbc:h2:mem:testDB;DB_CLOSE_DELAY=-1";
    private DataSource dataSourcePool;

    public static void main(String[] args) throws SQLException {
        var demoPool = new DemoPool();
        demoPool.createTable();
        demoPool.insertRecords();
        demoPool.useConnectionPool();
    }

    private DemoPool() {
        createConnectionPool();
    }

    private void createConnectionPool() {
        var config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setConnectionTimeout(3000); //ms
        config.setIdleTimeout(60000); //ms
        config.setMaxLifetime(600000);//ms
        config.setAutoCommit(false);
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(10);
        config.setPoolName("DemoHiPool");
        config.setRegisterMbeans(true);

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        dataSourcePool = new HikariDataSource(config);
    }

    private void createTable() throws SQLException {
        try (var connection = dataSourcePool.getConnection();
             PreparedStatement pst = connection.prepareStatement("create table test(id int, name varchar(50))")) {
            pst.executeUpdate();
        }
    }

    private void insertRecords() throws SQLException {
        try (var connection = dataSourcePool.getConnection();
             var pst = connection.prepareStatement("insert into test(id, name) values (?, ?)")) {
            var savePoint = connection.setSavepoint("savePointName");
            try {
                int rowCount = 0;
                for (int idx = 0; idx < 100; idx++) {
                    pst.setInt(1, idx);
                    pst.setString(2, "NameValue_" + idx);
                    rowCount += pst.executeUpdate();
                }
                connection.commit();
                logger.info("inserted rowCount: {}", rowCount);
            } catch (SQLException ex) {
                connection.rollback(savePoint);
                logger.error(ex.getMessage(), ex);
            }
        }
    }

    private void useConnectionPool() throws SQLException {
        try (var connection = dataSourcePool.getConnection();
             var pst = connection.prepareStatement("select count(*) as counter from test")) {
            try (var rs = pst.executeQuery()) {
                if (rs.next()) {
                    logger.info("counter: {}", rs.getString("counter"));
                }
            }
            connection.rollback();
        }
    }
}
