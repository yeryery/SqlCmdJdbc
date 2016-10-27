package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.*;

public class Insert implements Command {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;

    public Insert(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        dialog = new SelectTable();
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("insert");
    }

    @Override
    public void process(String input) {
        Set<String> tableNames = manager.getTableNames();
        String currentTableName = dialog.askUser(tableNames, view);

        if (!currentTableName.equals("cancel")) {
            view.write("Enter the values you require");

            Set<String> tableColumns = manager.getTableColumns(currentTableName);
            int tableSize = tableColumns.size();

            String[] columnNames = tableColumns.toArray(new String[tableSize]);;
            String[] values = new String[tableSize];
            DataSet newRow = new DataSetImpl();

            for (int i = 0; i < tableSize; i++) {
                view.write(columnNames[i]);
                values[i] = view.read();
                newRow.put(columnNames[i], values[i]);
            }

            try {
                manager.insert(currentTableName, newRow);
                view.write("You have successfully entered new data!");
            } catch (SQLException e) {
                view.write(e.getMessage());
            }
        } else {
            view.write("Table inserting canceled");
        }
    }
}
