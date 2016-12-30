package ua.com.juja.yeryery.commands;

public class IllegalArgumentException extends RuntimeException {
    private String message;

    public IllegalArgumentException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return String.format("Error! %s\nTry again", message);
    }

    public String getCutMessage() {
        return message;
    }
}

