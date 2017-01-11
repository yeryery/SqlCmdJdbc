package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;

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
        Dialog dialog = new Dialog(view, manager);
        String newEntriesMessage = "Enter the columnNames and its values of the row you want to insert:\n" +
                "columnName1|newValue1|columnName2|newValue2|...\n" +
                "or type 'cancel' to go back";

        String currentTableName = dialog.selectTable(ACTION);
        DataSet insertedRow = dialog.getInputEntries(currentTableName, newEntriesMessage);
        TablePrinter tablePrinter = new TablePrinter(view, manager, currentTableName);

        try {
            manager.insert(currentTableName, insertedRow);
            view.write(String.format("You have successfully entered new data into the table '%s'", currentTableName));
            tablePrinter.print();
        } catch (SQLException e) {
            view.write(e.getMessage());
            view.write("Try again.");
            process(input);
        }
    }
}
