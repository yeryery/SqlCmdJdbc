package ua.com.juja.yeryery.model;

public class JdbcDriverException extends RuntimeException {
    public JdbcDriverException() {
        super("Please, add jdbc jar to lib!");
    }
}
