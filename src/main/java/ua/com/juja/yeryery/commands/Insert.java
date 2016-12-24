package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.Set;

public class Insert implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "insert";

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
        Dialog dialog = new DialogImpl(view, manager);
        String selectMessage = String.format("Enter the name or select number of table where you want to %s new rows", ACTION);

        try {
            String currentTableName = dialog.selectTable(selectMessage);
            DataSet insertedRow = getNewValues(currentTableName);

            manager.insert(currentTableName, insertedRow);
            view.write(String.format("You have successfully entered new data into the table '%s'", currentTableName));
        } catch (SQLException e1) {
            view.write(e1.getMessage());
            view.write("Try again.");
            process(input);
        } catch (CancelException e2) {
            view.write("Table inserting canceled");
        }
    }

    private DataSet getNewValues(String currentTableName) {
        Set<String> tableColumns = manager.getTableColumns(currentTableName);
        view.write("Enter new values you require");

        DataSet dataSet = new DataSetImpl();
        for (String columnName : tableColumns) {
            view.write(columnName);
            Object value = view.read();
            dataSet.put(columnName, value);
        }
        return dataSet;
    }
}
