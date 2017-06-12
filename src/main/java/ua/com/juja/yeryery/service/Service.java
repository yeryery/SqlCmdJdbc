package ua.com.juja.yeryery.service;

import ua.com.juja.yeryery.model.DatabaseManager;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Service {

    List<String> getCommands();

    DatabaseManager connect(String databaseName, String userName, String password) throws ServiceException;

    List<List<String>> constructTable(DatabaseManager manager, String tableName) throws ServiceException;

    Set<String> listTables(DatabaseManager manager) throws ServiceException;

    void insert(DatabaseManager manager, String tableName, Map<String, String[]> parameter) throws ServiceException;

    Set<String> getColumnNames(DatabaseManager manager, String tableName) throws ServiceException;

    void delete(DatabaseManager manager, String tableName, String column, String value) throws ServiceException;

    void create(DatabaseManager manager, String tableName, String[] columnNames, String[] columnTypes,
                    String primaryKeyName, String primaryKeyType) throws ServiceException;

    void drop(DatabaseManager manager, String tableName) throws ServiceException;

    void clear(DatabaseManager manager, String tableName) throws ServiceException;

    void update(DatabaseManager manager, String tableName, Map<String, String[]> parameters, String definingColumn,
                String definingValue) throws ServiceException;

    Set<String> getColumns(DatabaseManager manager, String tableName) throws ServiceException;
}
