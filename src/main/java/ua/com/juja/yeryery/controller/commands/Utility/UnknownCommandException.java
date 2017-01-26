package ua.com.juja.yeryery.controller.commands.Utility;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
