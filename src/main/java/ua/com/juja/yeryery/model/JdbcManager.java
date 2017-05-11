package ua.com.juja.yeryery.model;

import ua.com.juja.yeryery.controller.commands.Exceptions.ConnectException;

import java.sql.*;
import java.util.*;

public class JdbcManager implements DatabaseManager {

    private Connection connection;
    private boolean isConnected = false;

    @Override
    public void connect(String database, String username, String password) {
        findJdbcDriver();
        closeOpenedConnection();

        try {
            String url = String.format("jdbc:postgresql://localhost:5432/%s", database);
            connection = DriverManager.getConnection(url, username, password);
            isConnected = true;
        } catch (SQLException e) {
            connection = null;
            throw new ConnectException(e.getMessage());
        }
    }

    private void findJdbcDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new JdbcDriverException();
        }
    }

    private void closeOpenedConnection() {
        if (connection != null) {
            try {
                connection.close();
                isConnected = false;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> result = new TreeSet<>();
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                result.add(resultSet.getString("table_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Set<String> getDatabases() {
        Set<String> result = new LinkedHashSet<>();
        String sql = "SELECT datname FROM pg_database WHERE datistemplate = false;";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> result = new LinkedHashSet<>();
        String sql = String.format("SELECT * FROM information_schema.columns WHERE table_name='%s'", tableName);

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                result.add(rs.getString("column_name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public void clear(String tableName) {
        String sql = "DELETE FROM " + tableName;

        try (Statement st = connection.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void create(String tableName, DataEntry primaryKey, DataSet dataSet) throws SQLException {
        String dataTypes = getDataSetFormatted(dataSet);
        String sql = String.format("CREATE TABLE %s(%s %s PRIMARY KEY NOT NULL, %s)",
                tableName, primaryKey.getColumn(), primaryKey.getValue(), dataTypes);

        try (Statement st = connection.createStatement()) {
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
        String sql = String.format("DROP TABLE %s", tableName);

        try (Statement st = connection.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void dropDB(String dataBaseName) {

        String disconnectDB = "SELECT pg_terminate_backend(pg_stat_activity.pid)\n" +
                "    FROM pg_stat_activity\n" +
                "    WHERE pg_stat_activity.datname = '" + dataBaseName + "'\n" +
                "      AND pid <> pg_backend_pid();";
        String sql = String.format("DROP DATABASE %s", dataBaseName);

        try (Statement st = connection.createStatement()) {
            st.executeQuery(disconnectDB);
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void insert(String tableName, DataSet input) throws SQLException {
        String columnNames = getColumnNamesFormatted("%s,", input);
        String values = getValuesFormatted("'%s',", input);
        String sql = String.format("INSERT INTO %s (%s)VALUES (%s)", tableName, columnNames, values);

        try (Statement st = connection.createStatement()) {
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
    public void delete(String tableName, DataEntry definingEntry) throws SQLException {
        String definingColumn = definingEntry.getColumn();
        Object definingValue = definingEntry.getValue();
        String sql = String.format("DELETE FROM %s WHERE %s = '%s'", tableName, definingColumn, definingValue);

        try (Statement st = connection.createStatement()) {
            st.executeUpdate(sql);
        }
    }

    @Override
    public List<DataSet> getDataContent(String tableName) {
        List<DataSet> result = new LinkedList<>();
        String sql = String.format("SELECT * FROM %s", tableName);

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData rsmd = rs.getMetaData();

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

        return getTruncatedString(result);
    }

    private String getTruncatedString(String string) {
        return string.substring(0, string.length() - 1);
    }

    private String getValuesFormatted(String format, DataSet input) {
        String result = "";
        List<Object> values = input.getValues();

        for (Object value : values) {
            result += String.format(format, value);
        }

        return getTruncatedString(result);
    }

    private String getDataSetFormatted(DataSet input) {
        String result = "";
        Set<String> columnNames = input.getColumnNames();

        for (String columnName : columnNames) {
            result += columnName + " ";
            result += input.get(columnName) + ",";
        }

        return getTruncatedString(result);
    }
}