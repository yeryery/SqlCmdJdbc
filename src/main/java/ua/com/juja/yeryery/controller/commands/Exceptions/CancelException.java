package ua.com.juja.yeryery.controller.commands.Exceptions;

public class CancelException extends RuntimeException {
    public CancelException() {
        super("Command execution is canceled");
    }
}
