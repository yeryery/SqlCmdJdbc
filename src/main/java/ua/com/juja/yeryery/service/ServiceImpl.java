package ua.com.juja.yeryery.service;

import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DataSetImpl;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.model.JdbcManager;

import java.sql.SQLException;
import java.util.*;

public class ServiceImpl implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect", "display", "clear", "create", "delete", "drop", "insert",
                "tables", "update");
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
    public void insert(DatabaseManager manager, String tableName, Map<String, String[]> parameter) throws SQLException {
        DataSet insertedRow = new DataSetImpl();
        Set<String> tableColumns = manager.getTableColumns(tableName);
        for (String name : parameter.keySet()) {
            if (tableColumns.contains(name)) {
                insertedRow.put(name, parameter.get(name)[0]);
            }
        }

        manager.insert(tableName, insertedRow);
    }

    @Override
    public Set<String> getColumnNames(DatabaseManager manager, String tableName) {
        return manager.getTableColumns(tableName);
    }
}
