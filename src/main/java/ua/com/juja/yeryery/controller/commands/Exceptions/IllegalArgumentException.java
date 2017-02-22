package ua.com.juja.yeryery.controller.commands.Exceptions;

public class IllegalArgumentException extends RuntimeException {

    private String message;

    public IllegalArgumentException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getExtendedMessage() {
        return String.format("Error! %s\nTry again", message);
    }
}

