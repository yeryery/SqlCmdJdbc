package ua.com.juja.yeryery.model;

public interface DataEntry {
    //TODO возможно убрать

    String getColumn();

    Object getValue();

    void setEntry(String columnName, Object value);
}
