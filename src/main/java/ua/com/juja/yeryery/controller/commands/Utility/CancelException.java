package ua.com.juja.yeryery.controller.commands.Utility;

public class CancelException extends RuntimeException {
    public CancelException() {
        super("Command execution is canceled");
    }
}
