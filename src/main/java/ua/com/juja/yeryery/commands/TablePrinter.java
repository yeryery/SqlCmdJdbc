package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.TableConstructor;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.List;
import java.util.Set;

public class TablePrinter {

    private View view;
    private DatabaseManager manager;
    private String tableName;
    private TableConstructor constructor;

    public TablePrinter(View view, DatabaseManager manager, String tableName) {
        this.view = view;
        this.manager = manager;
        this.tableName = tableName;
        constructor = constructTable();
    }

    public void print() {
        view.write(constructor.getTableString());
    }

    private TableConstructor constructTable() {
        Set<String> columns = manager.getTableColumns(tableName);
        List<DataSet> tableContent = manager.getDataContent(tableName);
        return new TableConstructor(columns, tableContent);
    }
}
