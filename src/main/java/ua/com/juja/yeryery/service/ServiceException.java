package ua.com.juja.yeryery.service;

public class ServiceException extends RuntimeException {
    public ServiceException(String message, Exception e) {
        super(message, e);
    }
}
