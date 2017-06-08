package ua.com.juja.yeryery.service;

import ua.com.juja.yeryery.model.*;

import java.sql.SQLException;
import java.util.*;

public abstract class ServiceImpl implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("display", "clear", "create", "delete", "drop", "insert", "update");
    }

    protected abstract DatabaseManager createManager();

    @Override
    public DatabaseManager connect(String databaseName, String userName, String password) throws ServiceException {
        DatabaseManager manager = createManager();
        //TODO не видит bean JdbcManager в AppContext
        try {
            manager.connect(databaseName, userName, password);
        } catch (SQLException e) {
            throw new ServiceException("Connection error", e);
        }

        return manager;
    }

    @Override
    public List<List<String>> constructTable(DatabaseManager manager, String tableName) {
        List<List<String>> result = new LinkedList<>();
        List<String> columns = new LinkedList<>();
        List<DataSet> tableData = new LinkedList<>();

        try {
            columns = new LinkedList<>(manager.getTableColumns(tableName));
            tableData = manager.getDataContent(tableName);
        } catch (SQLException e) {
            throw new ServiceException("Construct table error", e);
        }

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
    public Set<String> listTables(DatabaseManager manager) throws ServiceException {
        try {
            return manager.getTableNames();
        } catch (SQLException e) {
            throw new ServiceException("List tables error", e);
        }
    }

    @Override
    public void insert(DatabaseManager manager, String tableName, Map<String, String[]> parameters) throws ServiceException {
        DataSet insertedRow = new DataSetImpl();

        for (String name : parameters.keySet()) {
            insertedRow.put(name, parameters.get(name)[0]);
        }

        try {
            manager.insert(tableName, insertedRow);
        } catch (SQLException e) {
            throw new ServiceException("Insert entry error", e);
        }
    }

    @Override
    public Set<String> getColumnNames(DatabaseManager manager, String tableName) throws ServiceException {
        try {
            return manager.getTableColumns(tableName);
        } catch (SQLException e) {
            throw new ServiceException("Get columnnames error", e);
        }
    }

    @Override
    public void delete(DatabaseManager manager, String tableName, String column, String value) throws ServiceException {
        DataEntry entry = new DataEntryImpl();
        entry.setEntry(column, value);

        try {
            manager.delete(tableName, entry);
        } catch (SQLException e) {
            throw new ServiceException("Delete entry error", e);
        }
    }

    @Override
    public void create(DatabaseManager manager, String tableName, String[] columnNames, String[] columnTypes,
                       String primaryKeyName, String primaryKeyType) throws ServiceException {
        DataEntry primaryKey = new DataEntryImpl();
        primaryKey.setEntry(primaryKeyName, primaryKeyType);

        DataSet dataSet = new DataSetImpl();

        for (int i = 0; i < columnNames.length; i++) {
            dataSet.put(columnNames[i], columnTypes[i]);
        }

        try {
            manager.create(tableName, primaryKey, dataSet);
        } catch (SQLException e) {
            throw new ServiceException("Create table error", e);
        }
    }

    @Override
    public void drop(DatabaseManager manager, String tableName) throws ServiceException {
        try {
            manager.drop(tableName);
        } catch (SQLException e) {
            throw new ServiceException("Drop table error", e);
        }
    }

    @Override
    public void clear(DatabaseManager manager, String tableName) throws ServiceException {
        try {
            manager.clear(tableName);
        } catch (SQLException e) {
            throw new ServiceException("Clear table error", e);
        }
    }

    @Override
    public void update(DatabaseManager manager, String tableName, Map<String, String[]> parameters,
                       String definingColumn, String definingValue) throws ServiceException {

        DataSet updatedRow = new DataSetImpl();

        for (String name : parameters.keySet()) {
            String newValue = parameters.get(name)[0];

            if (!newValue.equals("")) {
                updatedRow.put(name, defineType(newValue));
            }
        }

        DataEntry definingEntry = new DataEntryImpl();
        definingEntry.setEntry(definingColumn, defineType(definingValue));

        try {
            manager.update(tableName, updatedRow, definingEntry);
        } catch (SQLException e) {
            throw new ServiceException("Update table error", e);
        }
    }

    private Object defineType(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return value;
        }
    }

    @Override
    public Set<String> getColumns(DatabaseManager manager, String tableName) throws ServiceException {
        try {
            return manager.getTableColumns(tableName);
        } catch (SQLException e) {
            throw new ServiceException("Get columns error", e);
        }
    }
}
