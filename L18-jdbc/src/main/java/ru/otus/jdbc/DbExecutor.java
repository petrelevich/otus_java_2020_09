package ru.otus.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public interface DbExecutor<T> {

    long executeInsert(Connection connection, String sql, List<Object> params) throws SQLException;

    Optional<T> executeSelect(Connection connection, String sql, Object id, Function<ResultSet, T> rsHandler) throws SQLException;

    //для получения всех записей params должен быть пустым массивом
    //List<T> executeSelect(Connection connection, String sql, Object[] params, Function<ResultSet, T> rsHandler) throws SQLException;
}
