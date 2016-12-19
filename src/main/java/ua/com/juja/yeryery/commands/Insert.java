package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.SQLErrorPrinter;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
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
        Dialog dialog = new DialogImpl(view);
        String message = String.format("Please enter the name or select number of table where you want to %s new rows", ACTION);
        String currentTableName = dialog.SelectTable(tableNames, message);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {
            Set<String> tableColumns = manager.getTableColumns(currentTableName);
            DataSet newRow = new DataSetImpl();

            while (true) {

                newRow = getNewValues(tableColumns);

                try {
                    manager.insert(currentTableName, newRow);
                    view.write(String.format("You have successfully entered new data into the table '%s'", currentTableName));
                    break;
                } catch (SQLException e) {
                    SQLErrorPrinter error = new SQLErrorPrinter(e);
                    error.printSQLError();

                    boolean confirmed = dialog.isConfirmed("Do you want to try again?");
                    if (!confirmed) {
                        cancel = true;
                        break;
                    }
                }
            }
        }

        if (cancel) {
            view.write("Table inserting canceled");
        }
    }

    private DataSet getNewValues(Set<String> tableColumns) {
        view.write("Enter new values you require");
        DataSet newRow = new DataSetImpl();
        for (String columnName : tableColumns) {
            view.write(columnName);
            Object value = view.read();
            newRow.put(columnName, value);
        }

        return newRow;
    }
}
