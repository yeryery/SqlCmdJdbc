package ua.com.juja.yeryery.manager;

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
                    "jdbc:postgresql://127.0.0.1:5432/" + database, username, password);
        } catch (SQLException e) {
            connection = null;
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<String> getTableNames() {
        Set<String> tableNames = new TreeSet<String>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'")) {
            while (resultSet.next()) {
                tableNames.add(resultSet.getString("table_name"));
            }
            return tableNames;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return tableNames;
        }
    }

    @Override
    public Set<String> getTableColumns(String tableName) {
        Set<String> columnNames = new LinkedHashSet<String>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM information_schema.columns" +
                     " WHERE table_name='" + tableName + "'")) {

            while (rs.next()) {
                columnNames.add(rs.getString("column_name"));
            }
            return columnNames;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return columnNames;
        }
    }

    @Override
    public void clear(String tableName) throws SQLException {
        try (Statement st = connection.createStatement()) {
            String sql = "DELETE FROM " + tableName;
            st.executeUpdate(sql);
        }
    }

    @Override
    public void create(String tableName, DataSet dataSet) throws SQLException {
        try (Statement st = connection.createStatement()) {
            String dataTypes = getDataSetFormatted(dataSet);
            String sql = "CREATE TABLE " + tableName + "(ID INT PRIMARY KEY NOT NULL, " + dataTypes + ")";
            st.executeUpdate(sql);
        }
    }

    @Override
    public void drop(String tableName) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("DROP TABLE " + tableName);
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
            String sql = "INSERT INTO " + tableName + " (" + columnNames + ")" + "VALUES (" + values + ")";
            st.executeUpdate(sql);
        }
    }

    @Override
    public void update(String tableName, DataSet newDataSet, String definingColumn, Object definingValue) throws SQLException {
        String updatedColumns = getColumnNamesFormatted("%s=?,", newDataSet);

        try (PreparedStatement ps = connection.prepareStatement("UPDATE " + tableName + " SET " + updatedColumns +
                " WHERE " + definingColumn + "= ?")) {
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
    public void delete(String tableName, String columnName, Object value) throws SQLException {
        try (Statement st = connection.createStatement()) {
            String sql = "DELETE FROM " + tableName + " WHERE " + columnName + " = '" + value + "'";
            st.executeUpdate(sql);
        }
    }

    @Override
    public List<DataSet> getDataContent(String tableName) {
        List<DataSet> result = new LinkedList<DataSet>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM " + tableName)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSetImpl();
                result.add(dataSet);
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return result;
        }
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
