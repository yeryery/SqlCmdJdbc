package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DataSetImpl;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TestTable {

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";

    public static Set<String> getTableColumns() {
        Set<String> tableColumns = new LinkedHashSet<>();
        tableColumns.add(ID_COLUMN);
        tableColumns.add(NAME_COLUMN);

        return tableColumns;
    }

    public static List<DataSet> getTableContent() {
        DataSet dataSet1 = new DataSetImpl();
        dataSet1.put(ID_COLUMN, 1);
        dataSet1.put(NAME_COLUMN, "John");

        DataSet dataSet2 = new DataSetImpl();
        dataSet2.put(ID_COLUMN, 2);
        dataSet2.put(NAME_COLUMN, "Mike");

        List<DataSet> tableContent = new LinkedList<>();
        tableContent.add(dataSet1);
        tableContent.add(dataSet2);

        return tableContent;
    }

    public static List<DataSet> getEmptyTable() {
        return new LinkedList<>();
    }
}
