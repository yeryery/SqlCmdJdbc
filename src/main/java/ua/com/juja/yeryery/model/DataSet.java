package ua.com.juja.yeryery.model;

import java.util.List;
import java.util.Set;

public interface DataSet {

    void put(String columnName, Object value);

    List<Object> getValues();

    Set<String> getColumnNames();

    Object get(String columnName);
}
