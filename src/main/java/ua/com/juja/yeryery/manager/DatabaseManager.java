package ua.com.juja.yeryery.manager;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    void connect(String database, String username, String password);

    Set<String> getTableNames();

    Set<String> getTableColumns(String tableName);

    void clear(String tableName) throws SQLException;

    void create(String tableName, DataSet columns) throws SQLException;

    void drop(String tableName) throws SQLException;

    boolean isConnected();

    void insert(String tableName, DataSet input) throws SQLException;

    void update(String tableName, DataSet input, String columnName, Object value) throws SQLException;

    void delete(String tableName, String columnName, Object value) throws SQLException;

    List<DataSet> getDataContent(String tableName);
}
