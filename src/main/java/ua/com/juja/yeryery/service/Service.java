package ua.com.juja.yeryery.service;

import ua.com.juja.yeryery.model.DatabaseManager;

import java.util.List;

public interface Service {

    List<String> commandsList();

    DatabaseManager connect(String databaseName, String userName, String password);

    List<List<String>> display(DatabaseManager manager, String tableName);
}
