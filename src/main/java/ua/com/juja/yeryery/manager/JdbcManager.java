package ua.com.juja.yeryery.manager;

import ua.com.juja.yeryery.ConnectException;

import java.sql.*;
import java.util.*;

public class JdbcManager implements DatabaseManager {

    private Connection connection;

    @Override
    public void connect(String database, String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Please, add jdbc jar to lib!", e);
        }

        try {
            connection = DriverManager.getConnection(
                    String.format("jdbc:postgresql://127.0.0.1:5432/%s", database), username, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tableNames = new TreeSet<String>();
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            throw new ConnectException("You can`t use '%s' unless you are not connected");
        }
        return tableNames;
    }

    @Override
    public Set<String> getDatabases() {
        Set<String> list = new LinkedHashSet<>();
        String sql = "SELECT datname FROM pg_database WHERE datistemplate = false;";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (Exception e) {
            list = null;
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> columnNames = new LinkedHashSet<String>();
        String sql = String.format("SELECT * FROM information_schema.columns WHERE table_name='%s'", tableName);

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                columnNames.add(rs.getString("column_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return columnNames;
    }

    @Override
    public void clear(String tableName) {
        try (Statement st = connection.createStatement()) {
            String sql = "DELETE FROM " + tableName;
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(String tableName, DataSet dataSet) throws SQLException {
        try (Statement st = connection.createStatement()) {
            String dataTypes = getDataSetFormatted(dataSet);
            String sql = String.format("CREATE TABLE %s(ID INT PRIMARY KEY NOT NULL, %s)", tableName, dataTypes);
            st.executeUpdate(sql);
        }
    }

    @Override
    public void createDB(String dataBaseName) {
        try (Statement st = connection.createStatement()) {
            String sql = String.format("CREATE DATABASE %s", dataBaseName);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drop(String tableName) {
        try (Statement st = connection.createStatement()) {
            String sql = String.format("DROP TABLE %s", tableName);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropDB(String dataBaseName) {
        try (Statement st = connection.createStatement()) {
            String sql = String.format("DROP DATABASE %s", dataBaseName);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void insert(String tableName, DataSet input) throws SQLException {
        try (Statement st = connection.createStatement()) {
            String columnNames = getColumnNamesFormatted("%s,", input);
            String values = getValuesFormatted("'%s',", input);
            String sql = String.format("INSERT INTO %s (%s)VALUES (%s)", tableName, columnNames, values);
            st.executeUpdate(sql);
        }
    }

    @Override
    public void update(String tableName, DataSet newDataSet, DataEntry definingEntry) throws SQLException {
        String updatedColumns = getColumnNamesFormatted("%s=?,", newDataSet);
        String definingColumn = definingEntry.getColumn();
        Object definingValue = definingEntry.getValue();
        String sql = String.format("UPDATE %s SET %s WHERE %s= ?", tableName, updatedColumns, definingColumn);

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            int sqlIndex = 1;
            List<Object> newValues = newDataSet.getValues();
            for (Object newValue : newValues) {
                ps.setObject(sqlIndex, newValue);
                sqlIndex++;
            }
            ps.setObject(sqlIndex, definingValue);
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(String tableName, DataEntry definingEntry) {
        String definingColumn = definingEntry.getColumn();
        Object definingValue = definingEntry.getValue();
        try (Statement st = connection.createStatement()) {
            String sql = String.format("DELETE FROM %s WHERE %s = '%s'", tableName, definingColumn, definingValue);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<DataSet> getDataContent(String tableName) {
        List<DataSet> result = new LinkedList<DataSet>();
        String sql = String.format("SELECT * FROM %s", tableName);

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSetImpl();
                result.add(dataSet);
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private String getColumnNamesFormatted(String format, DataSet input) {
        String result = "";
        Set<String> columnNames = input.getColumnNames();
        for (String columnName : columnNames) {
            result += String.format(format, columnName);
        }
        return result.substring(0, result.length() - 1);
    }

    private String getValuesFormatted(String format, DataSet input) {
        String result = "";
        List<Object> values = input.getValues();
        for (Object value : values) {
            result += String.format(format, value);
        }
        return result.substring(0, result.length() - 1);
    }

    private String getDataSetFormatted(DataSet input) {
        String result = "";
        Set<String> columnNames = input.getColumnNames();

        for (String columnName : columnNames) {
            result += columnName + " ";
            result += input.get(columnName) + ",";
        }
        return result.substring(0, result.length() - 1);
    }
}
