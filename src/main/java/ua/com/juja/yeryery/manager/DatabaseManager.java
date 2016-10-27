package ua.com.juja.yeryery.manager;

import java.sql.SQLException;
import java.util.Set;

public interface DatabaseManager {
    void connect(String database, String username, String password);

    Set<String> getTableNames();

    Set<String> getTableColumns(String tableName);

    void clear(String tableName);

    void create(String tableName, DataSet columns) throws SQLException;

    void drop(String tableName);

    boolean isConnected();

    void insert(String tableName, DataSet input) throws SQLException ;

    void update(String tableName, DataSet input, int id) throws SQLException;

    DataSet[] getDataContent(String tableName);
}
