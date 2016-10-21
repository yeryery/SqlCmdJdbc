package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

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
        String currentTableName = dialog.askUser(manager, view);

        manager.drop(currentTableName);
        view.write("Table " + currentTableName + " successfully dropped");
    }
}
