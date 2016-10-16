package ua.com.juja.yeryery.manager;

import java.sql.*;
import java.util.Arrays;

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
            throw new RuntimeException("Can`t get connection! You have entered incorrect data.");
        }
    }

    @Override
    public String[] getTableNames() {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT table_name FROM information_schema.tables WHERE table_schema='public' AND table_type='BASE TABLE'")) {
            String[] tableNames = new String[100];
            int index = 0;

            while (resultSet.next()) {
                tableNames[index++] = resultSet.getString("table_name");
            }
            tableNames = Arrays.copyOf(tableNames, index, String[].class);
            return tableNames;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return new String[0];
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
    public boolean isConnected() {
        return connection != null;
    }
}
