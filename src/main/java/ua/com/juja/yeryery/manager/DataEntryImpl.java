package ua.com.juja.yeryery.manager;

public class DataEntryImpl implements DataEntry {

    private String columnName;
    private Object value;

    public DataEntryImpl(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
    }

    @Override
    public String getColumn() {
        return columnName;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
