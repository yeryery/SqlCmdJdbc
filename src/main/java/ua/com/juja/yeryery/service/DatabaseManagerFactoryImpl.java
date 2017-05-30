package ua.com.juja.yeryery.service;

import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.model.JdbcManager;

//@Component
public class DatabaseManagerFactoryImpl implements DatabaseManagerFactory {
    @Override
    public DatabaseManager createDatabaseManager() {
        return new JdbcManager();
    }
}
