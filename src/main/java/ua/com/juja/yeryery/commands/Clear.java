package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Set;

public class Clear implements Command {

    private View view;
    private DatabaseManager manager;
    private final String ACTION = "clear";

    public Clear(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.startsWith(ACTION);
    }

    @Override
    public void process(String input) {
        Set<String> names = manager.getTableNames();
        Dialog dialog = new DialogImpl(view);
        String message = String.format("Please enter the name or select number of table you want to %s", ACTION);
        String currentTableName = dialog.SelectTable(names, message);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {
            String warning = String.format("Table '%s' will be cleared! Continue?", currentTableName);
            boolean confirmed = dialog.isConfirmed(warning);

            if (confirmed) {
                manager.clear(currentTableName);
                view.write(String.format("Table '%s' successfully cleared!", currentTableName));
            } else {
                cancel = true;
            }
        }

        if (cancel) {
            view.write("Table clearing canceled");
        }
    }
}