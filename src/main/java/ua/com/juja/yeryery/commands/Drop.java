package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Set;

public class Drop implements Command {
    private View view;
    private DatabaseManager manager;
    private Dialog dialog;

    public Drop(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        dialog = new SelectTable();
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("drop");
    }

    @Override
    public void process(String input) {
        Set<String> names = manager.getTableNames();
        String confirm = "";

        do {
            String currentTableName = dialog.askUser(names, view);

            if (!currentTableName.equals("cancel")) {
                while (!confirm.equals("y") && !confirm.equals("n")) {
                    view.write(String.format("Are you sure you want to drop table '%s'? (y/n)", currentTableName));
                    confirm = view.read();

                    if (confirm.equals("y")) {
                        manager.drop(currentTableName);
                        view.write(String.format("Table '%s' successfully dropped!", currentTableName));
                    } else if (confirm.equals("n")){
                        view.write("The dropping of table '" + currentTableName + "' is cancelled");
                    }
                }
            } else {
                view.write("Table dropping canceled");
                confirm = "";
            }
        } while (confirm.equals("n"));
    }
}
