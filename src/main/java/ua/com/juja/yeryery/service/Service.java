package ua.com.juja.yeryery.service;

import ua.com.juja.yeryery.model.DatabaseManager;

import java.util.List;
import java.util.Set;

public interface Service {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> display(DatabaseManager manager, String tableName);

    Set<String> listTables(DatabaseManager manager);
}
