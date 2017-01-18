package ua.com.juja.yeryery.model;

import java.util.*;

public class DataSetImpl implements DataSet {

    private Map<String, Object> data = new LinkedHashMap<>();

    @Override
    public void put(String columnName, Object value) {
        data.put(columnName, value);
    }

    @Override
    public List<Object> getValues() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Set<String> getColumnNames() {
        return data.keySet();
    }

    @Override
    public Object get(String columnName) {
        return data.get(columnName);
    }

    @Override
    public String toString() {
        return "{columnNames: " + getColumnNames() + ", " +
                "values: " + getValues() + "}";
    }
}
