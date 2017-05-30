package ua.com.juja.yeryery.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.yeryery.model.*;

import java.sql.SQLException;
import java.util.*;

@Component
public class ServiceImpl implements Service {

    @Autowired
    private DatabaseManagerFactory factory;

    @Override
    public List<String> commandsList() {
        return Arrays.asList("display", "clear", "create", "delete", "drop", "insert", "update");
    }

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) {
        DatabaseManager manager = factory.createDatabaseManager();
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
                Object value = dataSet.get(column);
                if (value != null) {
                    row.add(value.toString());
                } else {
                    row.add("");
                }
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

        for (String name : parameters.keySet()) {
            insertedRow.put(name, parameters.get(name)[0]);
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

    @Override
    public void update(DatabaseManager manager, String tableName, Map<String, String[]> parameters,
                       String definingColumn, String definingValue) throws SQLException {

        DataSet updatedRow = new DataSetImpl();

        for (String name : parameters.keySet()) {
            String newValue = parameters.get(name)[0];

            if (!newValue.equals("")) {
                updatedRow.put(name, defineType(newValue));
            }
        }

        DataEntry definingEntry = new DataEntryImpl();
        definingEntry.setEntry(definingColumn, defineType(definingValue));

        manager.update(tableName, updatedRow, definingEntry);
    }

    private Object defineType(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    @Override
    public Set<String> getColumns(DatabaseManager manager, String tableName) {
        return manager.getTableColumns(tableName);
    }
}
