package ua.com.juja.yeryery.service;

public class ServiceException extends RuntimeException {
    ServiceException(String message, Exception e) {
        super(message, e);
    }
}
