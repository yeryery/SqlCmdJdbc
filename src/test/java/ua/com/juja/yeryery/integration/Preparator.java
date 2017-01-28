package ua.com.juja.yeryery.integration;

import ua.com.juja.yeryery.model.*;

import java.sql.SQLException;
import java.util.Set;

public class Preparator {

    public static final String DATABASE = "testbase";
    //put here your own username and password
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "postgrespass";
    public static final String DATABASE_TO_DROP = "databasetodrop";
    //todo выделить manager как отдельное поле

    public static void setupDB(DatabaseManager manager) {
        Set<String> databases;

        try {
            disconnectFromDB(manager);
        } catch (RuntimeException e) {
            throw new RuntimeException("Set your username and password in Preparator class");
        }

        databases = manager.getDatabases();

        if (databases.contains(DATABASE_TO_DROP)) {
            manager.dropDB(DATABASE_TO_DROP);
        }

        if (!databases.contains(DATABASE)) {
            manager.createDB(DATABASE);
            manager.connect(DATABASE, USERNAME, PASSWORD);
            setupTables(manager);
        }
    }

    public static void createTableTest(DatabaseManager manager) {
        DataSet testColumns = new DataSetImpl();
        testColumns.put("login", "text");
        testColumns.put("password", "text");

        DataEntry testKey = new DataEntryImpl();
        testKey.setEntry("id", "int");

        DataSet testRow1 = new DataSetImpl();
        testRow1.put("id", 12);
        testRow1.put("login", "username1");
        testRow1.put("password", "pass1");

        DataSet testRow2 = new DataSetImpl();
        testRow2.put("id", 22);
        testRow2.put("login", "username2");
        testRow2.put("password", "pass2");

        try {
            manager.create("test", testKey, testColumns);
            manager.insert("test", testRow1);
            manager.insert("test", testRow2);
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    public static void createTableUsers(DatabaseManager manager) {

        DataSet usersColumns = new DataSetImpl();
        usersColumns.put("name", "text");
        usersColumns.put("age", "int");

        DataEntry usersKey = new DataEntryImpl();
        usersKey.setEntry("code", "int");

        try {
            manager.create("users", usersKey, usersColumns);
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private static void setupTables(DatabaseManager manager) {
        createTableTest(manager);
        createTableUsers(manager);
    }

    public static void deleteDB(DatabaseManager manager) {
        try {
            disconnectFromDB(manager);
            manager.dropDB(DATABASE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void disconnectFromDB(DatabaseManager manager) {
        manager.connect("", USERNAME, PASSWORD);
    }
}
