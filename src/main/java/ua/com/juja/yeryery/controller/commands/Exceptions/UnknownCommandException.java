package ua.com.juja.yeryery.controller.commands.Exceptions;

public class UnknownCommandException extends RuntimeException {
    public UnknownCommandException(String message) {
        super(message);
    }
}
