package ua.com.juja.yeryery.model;

import java.sql.SQLException;
import java.util.*;

public class DummyManager implements DatabaseManager {

    private Map<String, List<DataSet>> tables = new LinkedHashMap<>();

    @Override
    public List<DataSet> getDataContent(String tableName) {
        return get(tableName);
    }

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public Set<String> getDatabases() throws SQLException {
        return null;
    }

    @Override
    public void connect(String database, String userName, String password) {
        // do nothing
    }

    @Override
    public void clear(String tableName) {
        get(tableName).clear();
    }

    @Override
    public void create(String tableName, DataEntry primaryKey, DataSet columns) throws SQLException {
        get(tableName);
    }

    @Override
    public void createDB(String dataBase) throws SQLException {
        // do nothing
    }

    @Override
    public void drop(String tableName) throws SQLException {
        tables.remove(tableName);
    }

    @Override
    public void dropDB(String dataBaseName) throws SQLException {
        // do nothing
    }

    @Override
    public void insert(String tableName, DataSet input) throws SQLException {
        if (tables.containsKey(tableName)) {
            List<DataSet> rows = tables.get(tableName);
            rows.add(input);
        }
    }

    @Override
    public void update(String tableName, DataSet input, DataEntry entry) throws SQLException {
        for (DataSet dataSet : getDataContent(tableName)) {
            String definingColumn = entry.getColumn();
            if (dataSet.get(definingColumn).equals(entry.getValue())) {
                for (String column : input.getColumnNames()) {
                    dataSet.put(column, input.get(column));
                }
            }
        }
    }

    @Override
    public void delete(String tableName, DataEntry entry) throws SQLException {
        if (tables.containsKey(tableName)) {
            List<DataSet> rows = tables.get(tableName);

            for (DataSet row : rows) {
                for (String column : row.getColumnNames()) {
                    if (column.equals(entry.getColumn()) && row.get(column).equals(entry.getValue())) {
                        rows.remove(row);
                    }
                }
            }
        }
    }

    private List<DataSet> get(String tableName) {
        if (!tables.containsKey(tableName)) {
            tables.put(tableName, new LinkedList<DataSet>());
        }
        return tables.get(tableName);
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        return new LinkedHashSet<String>(Arrays.asList("name", "password", "id"));
    }

    @Override
    public boolean isConnected() {
        return true;
    }
}

