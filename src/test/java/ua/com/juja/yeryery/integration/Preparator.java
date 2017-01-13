package ua.com.juja.yeryery.integration;

import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DataSetImpl;
import ua.com.juja.yeryery.model.DatabaseManager;

import java.sql.SQLException;
import java.util.Set;

public class Preparator {

    public static final String DATABASE = "testbase";
    //put here your own username and password
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "postgrespass";

    public static void setupDB(DatabaseManager manager) {
        Set<String> databases;

        try {
            disconnectFromDB(manager);
        } catch (RuntimeException e) {
            throw new RuntimeException("Set your username and password in Preparator class");
        }
        databases = manager.getDatabases();

        if (!databases.contains(DATABASE)) {
            manager.createDB(DATABASE);
            manager.connect(DATABASE, USERNAME, PASSWORD);
            setupTables(manager);
        }
    }

    private static void setupTables(DatabaseManager manager) {
        DataSet testColumns = new DataSetImpl();
        DataSet ttableColumns = new DataSetImpl();
        DataSet testRow1 = new DataSetImpl();
        DataSet testRow2 = new DataSetImpl();

        testColumns.put("login", "text");
        testColumns.put("password", "text");

        ttableColumns.put("name", "text");
        ttableColumns.put("age", "int");

        testRow1.put("id", 12);
        testRow1.put("login", "username1");
        testRow1.put("password", "pass1");
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

    public static void deleteDB(DatabaseManager manager) {
        try {
            disconnectFromDB(manager);
            manager.dropDB(DATABASE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void disconnectFromDB(DatabaseManager manager) {
        manager.connect("", USERNAME, PASSWORD);
    }
}
