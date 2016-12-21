package ua.com.juja.yeryery;

import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;

import java.sql.SQLException;
import java.util.Set;

public class Preparation {

    private static final String DATABASE = "testbase";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "postgrespass";

    public static void setupDatabase(DatabaseManager manager) {
        manager.connect("", USERNAME, PASSWORD);
        Set<String> databases = manager.getDatabases();

        if (!databases.contains(DATABASE)) {
        manager.createDB(DATABASE);
        manager.connect(DATABASE, USERNAME, PASSWORD);

        setupTables(manager);
        manager.connect("", USERNAME, PASSWORD);
        }
    }

    private static void setupTables(DatabaseManager manager) {
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
