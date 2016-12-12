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
    private final String ACTION = "insert";

    public Insert(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        Set<String> tableNames = manager.getTableNames();
        Dialog dialog = new SelectTable();
        String currentTableName = dialog.askUser(tableNames, view, ACTION);

        if (!currentTableName.equals("cancel")) {
            Set<String> tableColumns = manager.getTableColumns(currentTableName);

            int tableSize = tableColumns.size();
            String[] columnNames = tableColumns.toArray(new String[tableSize]);
            String[] values = new String[tableSize];
            DataSet newRow = new DataSetImpl();

            while (true) {
                view.write("Enter new values you require");

                for (int i = 0; i < tableSize; i++) {
                    view.write(columnNames[i]);
                    values[i] = view.read();
                    newRow.put(columnNames[i], values[i]);
                }

                try {
                    manager.insert(currentTableName, newRow);
                    view.write(String.format("You have successfully entered new data into the table '%s'", currentTableName));
                    break;
                } catch (SQLException e) {
                    printSQLError(e);
                }
            }
        } else {
            view.write("Table inserting canceled");
        }
    }

    private void printSQLError(SQLException e) {
        String result = "SQL " + e.getMessage();

        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '\n') {
                result = result.substring(0, i);
                break;
            }
        }
        view.write(result + "!");
        view.write("Try again.");
        //TODO merge methods printSQLError from insert update create
    }
}
