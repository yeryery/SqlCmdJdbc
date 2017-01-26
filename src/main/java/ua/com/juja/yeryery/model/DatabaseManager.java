package ua.com.juja.yeryery.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    void connect(String database, String username, String password);

    Set<String> getTableNames();

    Set<String> getDatabases();

    Set<String> getTableColumns(String tableName);

    boolean isConnected();

    void clear(String tableName);

    void create(String tableName, DataEntry primaryKey, DataSet columns) throws SQLException;

    void createDB(String dataBase);

    void drop(String tableName);

    void dropDB(String dataBaseName);

    void insert(String tableName, DataSet input) throws SQLException;

    void update(String tableName, DataSet input, DataEntry entry) throws SQLException;

    void delete(String tableName, DataEntry entry);

    List<DataSet> getDataContent(String tableName);
}
