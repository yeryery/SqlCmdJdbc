package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.TableConstructor;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.List;
import java.util.Set;

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
        Dialog dialog = new DialogImpl(view, manager);
        String selectMessage = String.format("Please enter the name or select number of table where you want to %s rows", ACTION);
        String findMessage = "Enter columnName and defining value of deleted row";
        String commandSample = "columnName|value";

        String currentTableName;
        String[] splitInput;

        try {
            currentTableName = dialog.selectTable(selectMessage);
            splitInput = dialog.findRow(currentTableName, findMessage, commandSample);
        } catch (CancelException e) {
            view.write("Row removal canceled");
            return;
        }

        String columnName = splitInput[0];
        Object value = Parser.defineType(splitInput[1]);
        //TODO

        Set<String> tableColumns = manager.getTableColumns(currentTableName);
        List<DataSet> originRows = manager.getDataContent(currentTableName);
        TableConstructor tableConstructor = new TableConstructor(tableColumns, originRows);

        manager.delete(currentTableName, columnName, value);
        view.write(String.format("You have successfully deleted data from '%s' at %s = %s", currentTableName, columnName, value));

        view.write(tableConstructor.getTableString());
    }

}
