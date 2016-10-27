package ua.com.juja.yeryery.manager;

import java.util.Arrays;

public class DataSetImpl implements DataSet {

    private Data[] data = new Data[100];
    private int index = 0;

    static class Data {
        private String columnName;
        private Object value;

        public Data(String columnName, Object value) {
            this.columnName = columnName;
            this.value = value;
        }

        public String getName() {
            return columnName;
        }

        public Object getValue() {
            return value;
        }
    }

    @Override
    public void put(String columnName, Object value) {
        for (int i = 0; i < index; i++) {
            if (data[i].columnName.equals(columnName)) {
                data[i].value = value;
                return;
            }
        }
        data[index++] = new Data(columnName, value);
    }

    @Override
    public String toString() {
        return "{columnNames: " + Arrays.toString(getColumnNames()) + ", " +
                "values: " + Arrays.toString(getValues()) + "}";
    }

    @Override
    public Object[] getValues() {
        Object[] result = new Object[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    @Override
    public String[] getColumnNames() {
        String [] result = new String[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getName();
        }
        return result;
    }

    @Override
    public Object get(String columnName) {
        for (int i = 0; i < index; i++) {
            if (data[i].getName().equals(columnName)) {
                return data[i].getValue();
            }
        }
        return null;
    }

    @Override
    public void updateFrom(DataSet newValue) {
        for (String columnName : newValue.getColumnNames()) {
            Object value = newValue.get(columnName);
            this.put(columnName, value);
        }
    }
}
