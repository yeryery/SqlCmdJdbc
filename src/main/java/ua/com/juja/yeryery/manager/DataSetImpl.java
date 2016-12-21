package ua.com.juja.yeryery.manager;

import java.util.*;

public class DataSetImpl implements DataSet {

    private Map<String, Object> data = new LinkedHashMap<String, Object>();
    private int index = 0;

    @Override
    public void put(String columnName, Object value) {
        data.put(columnName, value);
    }

    @Override
    public List<Object> getValues() {
        return new ArrayList<Object>(data.values());
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
        return "{columnNames: " + getColumnNames().toString() + ", " +
                "values: " + getValues().toString() + "}";
    }
}
