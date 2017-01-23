package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DataSetImpl;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TestTable {

    private static final String COLUMN_1 = "id";
    private static final String COLUMN_2 = "name";

    public static Set<String> getTableColumns() {
        Set<String> tableColumns = new LinkedHashSet<>();
        tableColumns.add(COLUMN_1);
        tableColumns.add(COLUMN_2);

        return tableColumns;
    }

    public static List<DataSet> getTableContent() {
        DataSet dataSet1 = new DataSetImpl();
        dataSet1.put(COLUMN_1, 1);
        dataSet1.put(COLUMN_2, "John");

        DataSet dataSet2 = new DataSetImpl();
        dataSet2.put(COLUMN_1, 2);
        dataSet2.put(COLUMN_2, "Mike");

        List<DataSet> tableContent = new LinkedList<>();
        tableContent.add(dataSet1);
        tableContent.add(dataSet2);

        return tableContent;
    }

    public static List<DataSet> getEmptyTable() {
        return new LinkedList<>();
    }
}
