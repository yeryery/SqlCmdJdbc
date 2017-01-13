package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Drop implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "drop";

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
        Dialog dialog = new Dialog(view, manager);
        String currentTableName = dialog.selectTable(ACTION);

        dialog.confirmAction(ACTION, currentTableName);
        manager.drop(currentTableName);
        view.write(String.format("Table '%s' successfully dropped!", currentTableName));
    }
}
