package ua.com.juja.yeryery.service;

import ua.com.juja.yeryery.model.DatabaseManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Service {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> constructTable(DatabaseManager manager, String tableName);

    Set<String> listTables(DatabaseManager manager);

    void insert(DatabaseManager manager, String tableName, Map<String, String[]> parameter) throws SQLException;

    Set<String> getColumnNames(DatabaseManager manager, String tableName);

    void delete(DatabaseManager manager, String tableName, String column, String value) throws SQLException;

    void create(DatabaseManager manager, String tableName, String[] columnNames, String[] columnTypes,
                    String primaryKeyName, String primaryKeyType) throws SQLException;

    void drop(DatabaseManager manager, String tableName);

    void clear(DatabaseManager manager, String tableName);
}
