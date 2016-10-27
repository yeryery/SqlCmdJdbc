package ua.com.juja.yeryery.manager;

public interface DataSet {

    void put(String columnName, Object value);

    Object[] getValues();

    String[] getColumnNames();

    Object get(String columnName);

    void updateFrom(DataSet newValue);
}
