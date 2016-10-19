package ua.com.juja.yeryery.manager;

public interface DatabaseManager {
    void connect(String database, String username, String password);

    String[] getTableNames();

    String[] getTableColumns(String tableName);

    void clear(String tableName);

    void create(String tableName, DataSet columns);

    void drop(String tableName);

    boolean isConnected();

    void insert(String tableName, DataSet input);

    DataSet[] getDataContent(String tableName);
}
