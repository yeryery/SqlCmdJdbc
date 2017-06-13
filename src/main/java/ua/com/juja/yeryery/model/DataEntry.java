package ua.com.juja.yeryery.model;

public interface DataEntry {

    String getColumn();

    Object getValue();

    void setEntry(String columnName, Object value);
}
