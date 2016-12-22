package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.manager.DataSet;

public interface Dialog {
    String selectTable(String message);

    String nameTable(String message);

    void confirmAction(String warning);

    String[] findRow(String tableName, String action, String sample);

    DataSet setValues(String tableName, String[] input);

    DataSet setColumnNames(String message, String sample);
}
