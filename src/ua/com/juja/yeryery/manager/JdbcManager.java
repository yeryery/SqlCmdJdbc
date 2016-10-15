package ua.com.juja.yeryery.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            throw new RuntimeException("Can`t get connection! You have entered incorrect data. Please try again...");
        }
    }
}
