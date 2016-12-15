package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.SQLErrorPrinter;
import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.Set;

public class Drop implements Command {
    private View view;
    private DatabaseManager manager;
    private final String ACTION = "drop";

    public Drop(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        Set<String> names = manager.getTableNames();

        Dialog dialog = new SelectTable();
        String currentTableName = dialog.askUser(names, view, ACTION);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {
            String warning = "Table '" + currentTableName + "' will be dropped! Continue?";
            boolean confirmed = dialog.isConfirmed(warning, view);

            if (confirmed) {
                try {
                    manager.drop(currentTableName);
                    view.write(String.format("Table '%s' successfully dropped!", currentTableName));
                } catch (SQLException e) {
                    SQLErrorPrinter error = new SQLErrorPrinter(e);
                    error.printSQLError();
                }
            } else {
                cancel = true;
            }
        }

        if (cancel) {
            view.write("Table dropping canceled");
        }
    }
}
