package ua.com.juja.yeryery.manager;

import java.util.Arrays;

public class DataSet {

    public Data[] data = new Data[100];
    public int index = 0;

    static class Data {
        private String columnName;
        private Object value;

        public Data(String columnName, Object value) {
            this.columnName = columnName;
            this.value = value;
        }

        public String getColumnName() {
            return columnName;
        }

        public Object getValue() {
            return value;
        }
    }

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

    public Object[] getValues() {
        Object[] result = new Object[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getValue();
        }
        return result;
    }

    public String[] getColumnNames() {
        String [] result = new String[index];
        for (int i = 0; i < index; i++) {
            result[i] = data[i].getColumnName();
        }
        return result;
    }
}
