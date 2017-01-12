package ua.com.juja.yeryery.integration;

import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DataSetImpl;
import ua.com.juja.yeryery.model.DatabaseManager;

import java.sql.SQLException;
import java.util.Set;

public class Preparator {

    private String database;
    private String username;
    private String password;

    public Preparator(String database, String username, String password) {
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public void setupDatabase(DatabaseManager manager) {
        manager.connect("", username, password);
        Set<String> databases = manager.getDatabases();

        if (!databases.contains(database)) {
        manager.createDB(database);
        manager.connect(database, username, password);

        setupTables(manager);
        manager.connect("", username, password);
        }
    }

    private void setupTables(DatabaseManager manager) {
        DataSet testColumns = new DataSetImpl();
        testColumns.put("login", "text");
        testColumns.put("password", "text");

        DataSet ttableColumns = new DataSetImpl();
        ttableColumns.put("name", "text");
        ttableColumns.put("age", "int");

        DataSet testRow1 = new DataSetImpl();
        testRow1.put("id", 12);
        testRow1.put("login", "username1");
        testRow1.put("password", "pass1");
        DataSet testRow2 = new DataSetImpl();
        testRow2.put("id", 22);
        testRow2.put("login", "username2");
        testRow2.put("password", "pass2");

        try {
            manager.create("test", testColumns);
            manager.insert("test", testRow1);
            manager.insert("test", testRow2);
            manager.create("ttable", ttableColumns);
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
