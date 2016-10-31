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
            Set<String> tableColumns = manager.getTableColumns(currentTableName);
            int size = 0;

            while (size == 0) {
                view.write("Enter new values you require");
                size = tableColumns.size();
                String[] columnNames = tableColumns.toArray(new String[size]);
                String[] values = new String[size];
                DataSet newRow = new DataSetImpl();

                for (int i = 0; i < size; i++) {
                    view.write(columnNames[i]);
                    values[i] = view.read();
                    newRow.put(columnNames[i], values[i]);
                }

                try {
                    manager.insert(currentTableName, newRow);
                    view.write("You have successfully entered new data into table '" + currentTableName + "'");
                } catch (SQLException e) {
                    size = 0;
                    view.write("SQL " + e.getMessage());
                }
            }
        } else {
            view.write("Table inserting canceled");
        }
    }
}
