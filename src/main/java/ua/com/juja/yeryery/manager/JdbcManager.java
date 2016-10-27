package ua.com.juja.yeryery.manager;

import java.sql.*;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

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
                     "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'"))
        {
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
    public String[] getTableColumns(String tableName) {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM information_schema.columns" +
                     " WHERE table_name='" + tableName + "'")) {
            String[] columnNames = new String[100];
            int index = 0;

            while (rs.next()) {
                columnNames[index++] = rs.getString("column_name");
            }
            columnNames = Arrays.copyOf(columnNames, index, String[].class);
            return columnNames;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new String[0];
        }
    }

    @Override
    public void clear(String tableName) {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("DELETE FROM " + tableName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void create(String tableName, DataSet dataSet) throws SQLException {
        try (Statement st = connection.createStatement()) {
            String dataTypes = getDataSetFormatted(dataSet);
            st.executeUpdate("CREATE TABLE " + tableName + "(ID INT PRIMARY KEY NOT NULL, " + dataTypes + ")");
        }
    }

    @Override
    public void drop(String tableName) {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate("DROP TABLE " + tableName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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

            st.executeUpdate("INSERT INTO " + tableName + " (" + columnNames + ")" + "VALUES (" + values + ")");
        }
    }

    @Override
    public void update(String tableName, DataSet input, int id) throws SQLException {
        String columnNames = getColumnNamesFormatted("%s=?,", input);
        try (PreparedStatement ps = connection.prepareStatement("UPDATE " + tableName + " SET " + columnNames + " WHERE ID = ?")) {
            int sqlIndex = 1;
            Object[] newValues = input.getValues();
            for (Object newValue : newValues) {
                ps.setObject(sqlIndex, newValue);
                sqlIndex++;
            }
            ps.setInt(sqlIndex, id);
            ps.executeUpdate();
        }
    }

    @Override
    public DataSet[] getDataContent(String tableName) {
        try (
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM " + tableName)) {
            int size = getSize(tableName);

            ResultSetMetaData rsmd = rs.getMetaData();
            DataSet[] result = new DataSet[size];
            int index = 0;
            while (rs.next()) {
                DataSet dataSet = new DataSet();
                result[index++] = dataSet;
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    dataSet.put(rsmd.getColumnName(i), rs.getObject(i));
                }
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new DataSet[0];
        }
    }

    private String getColumnNamesFormatted(String format, DataSet input) {
        String result = "";
        String[] columnNames = input.getColumnNames();
        for (String columnName : columnNames) {
            result += String.format(format, columnName);
        }
        return result.substring(0, result.length() - 1);
    }

    private String getValuesFormatted(String format, DataSet input) {
        String result = "";
        Object[] values = input.getValues();
        for (Object value : values) {
            result += String.format(format, value);
        }
        return result.substring(0, result.length() - 1);
    }

    private String getDataSetFormatted(DataSet input) {
        String result = "";
        String[] columnNames = input.getColumnNames();
        Object[] values = input.getValues();
        for (int i = 0; i < columnNames.length; i++) {
            result += columnNames[i] + " ";
            result += values[i] + ",";
        }
        return result.substring(0, result.length() - 1);
    }

    private int getSize(String tableName) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rsCount = st.executeQuery("SELECT COUNT (*) FROM " + tableName);
        rsCount.next();
        return rsCount.getInt(1);
    }
}
