package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Clear implements Command {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;

    public Clear(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        dialog = new SelectTable();
    }

    @Override
    public boolean canProcess(String input) {
        return input.startsWith("clear");
    }

    @Override
    public void process(String input) {
        String currentTableName = dialog.askUser(manager, view);

        if (!currentTableName.equals("cancel")) {
            view.write(String.format("Are you sure you want to clear table '%s'? (y/n)", currentTableName));
            String confirm = view.read();

            if (confirm.equals("y")) {
                manager.clear(currentTableName);
                view.write(String.format("Table '%s' successfully cleared!", currentTableName));
            } else {
                view.write("Table cleaning canceled");
            }
        }
    }
}
