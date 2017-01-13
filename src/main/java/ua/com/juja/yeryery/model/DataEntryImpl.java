package ua.com.juja.yeryery.model;

public class DataEntryImpl implements DataEntry {

    private String columnName;
    private Object value;

    public DataEntryImpl() {
    }

    @Override
    public String getColumn() {
        return columnName;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setEntry(String columnName, Object value) {
        this.columnName = columnName;
        this.value = value;
    }
}
