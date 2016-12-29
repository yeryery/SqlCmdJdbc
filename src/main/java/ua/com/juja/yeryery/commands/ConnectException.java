package ua.com.juja.yeryery.commands;

public class ConnectException extends RuntimeException {

    private String message;

    public ConnectException(String message) {
        this.message = message;
    }

    public ConnectException(Exception e) {
    }

    public String getMessage() {
        return String.format("Error! %s\nTry again", message);
    }
}
