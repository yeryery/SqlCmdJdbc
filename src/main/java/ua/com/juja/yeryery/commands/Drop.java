package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

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

        if (!currentTableName.equals("cancel")) {
            String confirm = "";
            while (!confirm.equals("y") && !confirm.equals("n")) {
                view.write(String.format("Are you sure you want to drop table '%s'? (y/n)", currentTableName));
                confirm = view.read();

                if (confirm.equals("y")) {
                    manager.drop(currentTableName);
                    view.write(String.format("Table '%s' successfully dropped!", currentTableName));
                } else if (confirm.equals("n")) {
                    view.write("The dropping of table '" + currentTableName + "' is cancelled");
                }
            }
        } else {
            view.write("Table dropping canceled");
        }
    }
}
