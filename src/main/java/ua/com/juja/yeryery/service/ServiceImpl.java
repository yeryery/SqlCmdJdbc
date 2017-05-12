package ua.com.juja.yeryery.service;

import ua.com.juja.yeryery.model.*;

import java.sql.SQLException;
import java.util.*;

public class ServiceImpl implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("display", "clear", "create", "delete", "drop", "insert", "update");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = new JdbcManager();
        manager.connect(databaseName, userName, password);
        return manager;
    }

    @Override
    public List<List<String>> constructTable(DatabaseManager manager, String tableName) {
        List<List<String>> result = new LinkedList<>();

        List<String> columns = new LinkedList<>(manager.getTableColumns(tableName));
        List<DataSet> tableData = manager.getDataContent(tableName);

        result.add(columns);
        for (DataSet dataSet : tableData) {
            List<String> row = new ArrayList<>(columns.size());
            result.add(row);
            for (String column : columns) {
                row.add(dataSet.get(column).toString());
            }
        }

        return result;

    }

    @Override
    public Set<String> listTables(DatabaseManager manager) {
        return manager.getTableNames();
    }

    @Override
    public void insert(DatabaseManager manager, String tableName, Map<String, String[]> parameters) throws SQLException {
        DataSet insertedRow = new DataSetImpl();
        Set<String> tableColumns = manager.getTableColumns(tableName);

        for (String name : parameters.keySet()) {
            if (tableColumns.contains(name)) {
                insertedRow.put(name, parameters.get(name)[0]);
            }
        }

        manager.insert(tableName, insertedRow);
    }

    @Override
    public Set<String> getColumnNames(DatabaseManager manager, String tableName) {
        return manager.getTableColumns(tableName);
    }

    @Override
    public void delete(DatabaseManager manager, String tableName, String column, String value) throws SQLException {
        DataEntry entry = new DataEntryImpl();
        entry.setEntry(column, value);
        manager.delete(tableName, entry);
    }

    @Override
    public void create(DatabaseManager manager, String tableName, String[] columnNames, String[] columnTypes,
                            String primaryKeyName, String primaryKeyType) throws SQLException {
        DataEntry primaryKey = new DataEntryImpl();
        primaryKey.setEntry(primaryKeyName, primaryKeyType);

        DataSet dataSet = new DataSetImpl();

        for (int i = 0; i < columnNames.length; i++) {
            dataSet.put(columnNames[i], columnTypes[i]);
        }

        manager.create(tableName, primaryKey, dataSet);
    }

    @Override
    public void drop(DatabaseManager manager, String tableName) {
        manager.drop(tableName);
    }

    @Override
    public void clear(DatabaseManager manager, String tableName) {
        manager.clear(tableName);
    }
}
