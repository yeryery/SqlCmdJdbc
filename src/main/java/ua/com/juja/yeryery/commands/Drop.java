package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

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
        Dialog dialog = new DialogImpl(view, manager);
        String message = String.format("Please enter the name or select number of table you want to %s", ACTION);
        String currentTableName = dialog.SelectTable(message);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {
            String warning = String.format("Table '%s' will be dropped! Continue?", currentTableName);
            boolean confirmed = dialog.isConfirmed(warning);

            if (confirmed) {
                manager.drop(currentTableName);
                view.write(String.format("Table '%s' successfully dropped!", currentTableName));
            } else {
                cancel = true;
            }
        }

        if (cancel) {
            view.write("Table dropping canceled");
        }
    }
}
