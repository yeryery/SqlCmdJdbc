package ua.com.juja.yeryery.manager;

public class DataEntry {

    private String columnName;
    private Object value;

    public DataEntry(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
    }

    public String getColumn() {
        return columnName;
    }

    public Object getValue() {
        return value;
    }
}
