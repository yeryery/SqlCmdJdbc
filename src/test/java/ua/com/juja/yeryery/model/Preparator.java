package ua.com.juja.yeryery.model;

import ua.com.juja.yeryery.model.*;

import java.sql.SQLException;
import java.util.Set;

public class Preparator {

    public static final DatabaseManager TEST_MANAGER = new JdbcManager();

    //put here your own username and password
    private static final String DEFAULT_DB = "postgres";
    public static final String TEST_DB = "testbase";
    public static final String USERNAME = "postgres";
    public static final String PASSWORD = "postgrespass";

    public static void setupDB() throws SQLException {
        try {
            connectToDefaultDB();
        } catch (RuntimeException e) {
            throw new RuntimeException("Set your username and password in Preparator class");
        }

        Set<String> databases = TEST_MANAGER.getDatabases();

        if (databases.contains(TEST_DB)) {
            TEST_MANAGER.dropDB(TEST_DB);
        }

        TEST_MANAGER.createDB(TEST_DB);
        TEST_MANAGER.connect(TEST_DB, USERNAME, PASSWORD);
        setupTables();
    }

    public static void createTableTest() {
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
            TEST_MANAGER.create("test", testKey, testColumns);
            TEST_MANAGER.insert("test", testRow1);
            TEST_MANAGER.insert("test", testRow2);
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private static void createTableUsers() {
        DataSet usersColumns = new DataSetImpl();
        usersColumns.put("name", "text");
        usersColumns.put("age", "int");

        DataEntry usersKey = new DataEntryImpl();
        usersKey.setEntry("code", "int");

        try {
            TEST_MANAGER.create("users", usersKey, usersColumns);
        } catch (SQLException e) {
            e.getMessage();
        }
    }

    private static void setupTables() {
        createTableTest();
        createTableUsers();
    }

    public static void deleteDB() {
        try {
            connectToDefaultDB();
            TEST_MANAGER.dropDB(TEST_DB);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void connectToDefaultDB() throws SQLException {
        TEST_MANAGER.connect(DEFAULT_DB, USERNAME, PASSWORD);
    }
}
