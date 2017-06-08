package ua.com.juja.yeryery.model;

import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    void connect(String database, String username, String password);

    Set<String> getTableNames();

    Set<String> getDatabases();

    Set<String> getTableColumns(String tableName);

    boolean isConnected();

    void clear(String tableName);

    void create(String tableName, DataEntry primaryKey, DataSet columns);

    void createDB(String dataBase);

    void drop(String tableName);

    void dropDB(String dataBaseName);

    void insert(String tableName, DataSet input);

    void update(String tableName, DataSet input, DataEntry entry);

    void delete(String tableName, DataEntry entry);

    List<DataSet> getDataContent(String tableName);
}
