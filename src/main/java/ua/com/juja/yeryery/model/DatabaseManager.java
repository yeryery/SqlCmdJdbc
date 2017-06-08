package ua.com.juja.yeryery.model;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface DatabaseManager {
    void connect(String database, String username, String password) throws SQLException;

    Set<String> getTableNames() throws SQLException;

    Set<String> getDatabases() throws SQLException;

    Set<String> getTableColumns(String tableName) throws SQLException;

    boolean isConnected();

    void clear(String tableName) throws SQLException;

    void create(String tableName, DataEntry primaryKey, DataSet columns) throws SQLException;

    void createDB(String dataBase) throws SQLException;

    void drop(String tableName) throws SQLException;

    void dropDB(String dataBaseName) throws SQLException;

    void insert(String tableName, DataSet input) throws SQLException;

    void update(String tableName, DataSet input, DataEntry entry) throws SQLException;

    void delete(String tableName, DataEntry entry) throws SQLException;

    List<DataSet> getDataContent(String tableName) throws SQLException;
}
