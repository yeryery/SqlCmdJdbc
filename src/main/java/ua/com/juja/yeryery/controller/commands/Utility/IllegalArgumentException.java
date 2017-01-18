package ua.com.juja.yeryery.controller.commands.Utility;

public class IllegalArgumentException extends RuntimeException {
    private String message;

    public IllegalArgumentException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return String.format("Error! %s\nTry again", message);
    }

    public String getShortMessage() {
        return message;
    }
}

