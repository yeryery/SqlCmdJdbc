package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.SelectTable;
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

        Dialog dialog = new SelectTable();
        String currentTableName = dialog.askUser(names, view, ACTION);

        if (!currentTableName.equals("cancel")) {
            boolean confirmed = dialog.isConfirmed(currentTableName, view, ACTION);

            if (confirmed) {
                manager.clear(currentTableName);
                view.write(String.format("Table '%s' successfully cleared!", currentTableName));
            } else {
                view.write(String.format("The clearing of table '%s' is cancelled", currentTableName));
            }

        } else {
            view.write("Table clearing canceled");
        }
    }
}