package ua.com.juja.yeryery.manager;

import java.sql.SQLException;

public interface DatabaseManager {
    void connect(String database, String username, String password);

    String[] getTableNames();

    String[] getTableColumns(String tableName);

    void clear(String tableName);

    void create(String tableName, DataSet columns) throws SQLException;

    void drop(String tableName);

    boolean isConnected();

    void insert(String tableName, DataSet input) throws SQLException ;

    DataSet[] getDataContent(String tableName);
}