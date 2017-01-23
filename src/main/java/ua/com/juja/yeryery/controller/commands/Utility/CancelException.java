package ua.com.juja.yeryery.controller.commands.Utility;

public class CancelException extends RuntimeException {

    public String getMessage() {
        return "Command execution is canceled";
    }
}
