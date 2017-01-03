package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Display implements Command {
    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "display";

    public Display(View view, DatabaseManager manager) {
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
        TablePrinter tablePrinter = new TablePrinter(view, manager, currentTableName);
        tablePrinter.print();
    }
}
