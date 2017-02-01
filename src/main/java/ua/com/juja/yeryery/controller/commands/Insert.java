package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.controller.commands.Utility.TablePrinter;
import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DatabaseManager;
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
        String currentTableName = dialog.selectTable(ACTION);
        DataSet insertedRow = dialog.getNewEntries(currentTableName, ACTION);

        try {
            manager.insert(currentTableName, insertedRow);
        } catch (SQLException e) {
            view.write(e.getMessage());
            view.write("Try again.");
            process(input);
            return;
        }

        view.write(String.format("You have successfully entered new data into the table '%s'", currentTableName));
        TablePrinter tablePrinter = new TablePrinter(view, manager, currentTableName);
        tablePrinter.print();
    }
}
