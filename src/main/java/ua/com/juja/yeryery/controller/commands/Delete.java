package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.controller.commands.Utility.TablePrinter;
import ua.com.juja.yeryery.model.DataEntry;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Delete implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "delete";

    public Delete(View view, DatabaseManager manager) {
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
        DataEntry definingEntry = dialog.findRow(currentTableName, ACTION);

        manager.delete(currentTableName, definingEntry);
        view.write(String.format("You have successfully deleted data from '%s'", currentTableName));

        TablePrinter tablePrinter = new TablePrinter(view, manager, currentTableName);
        tablePrinter.print();
    }

}
