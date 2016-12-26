package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class TestTable {

    private String column1 = "id";
    private String column2 = "name";

    public Set<String> getTableColumns() {
        Set<String> tableColumns = new LinkedHashSet<>();
        tableColumns.add(column1);
        tableColumns.add(column2);

        return tableColumns;
    }

    public List<DataSet> getTableContent() {
        DataSet dataSet1 = new DataSetImpl();
        int value11 = 1;
        String value21 = "John";
        dataSet1.put(column1, value11);
        dataSet1.put(column2, value21);

        DataSet dataSet2 = new DataSetImpl();
        int value12 = 2;
        String value22 = "Mike";
        dataSet2.put(column1, value12);
        dataSet2.put(column2, value22);

        List<DataSet> tableContent = new LinkedList<DataSet>();
        tableContent.add(dataSet1);
        tableContent.add(dataSet2);

        return tableContent;
    }
}
