package ua.com.juja.yeryery.service;

import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.model.JdbcManager;

import java.util.Arrays;
import java.util.List;

public class ServiceImpl implements Service {

    private DatabaseManager manager;

    public ServiceImpl() {
        manager = new JdbcManager();
    }

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "menu", "connect");
    }

    @Override
    public void connect(String databaseName, String userName, String password) {
        manager.connect(databaseName, userName, password);
    }
}
