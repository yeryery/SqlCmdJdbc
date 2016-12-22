package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.TableConstructor;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.List;
import java.util.Set;

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
        Dialog dialog = new DialogImpl(view, manager);
        String selectMessage = String.format("Please enter the name or select number of table you want to %s", ACTION);

        try {
            String currentTableName = dialog.selectTable(selectMessage);
            Set<String> tableColumns = manager.getTableColumns(currentTableName);
            List<DataSet> tableContent = manager.getDataContent(currentTableName);
            TableConstructor tableConstructor = new TableConstructor(tableColumns, tableContent);
            view.write(tableConstructor.getTableString());
        } catch (CancelException e) {
            view.write("Table displaying canceled");
        }
    }
}
