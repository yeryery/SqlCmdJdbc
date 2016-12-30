package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.TableConstructor;
import ua.com.juja.yeryery.manager.DataEntry;
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
        Dialog dialog = new Dialog(view, manager);
        String findRowMessage = "Enter the columnName and defining value of deleted row: %s\nor type 'cancel' to go back";
        String commandSample = "columnName|value";

        String currentTableName;
        DataEntry definingEntry;

        currentTableName = dialog.selectTable(ACTION);
        definingEntry = dialog.defineRow(currentTableName, findRowMessage, commandSample);

        Set<String> tableColumns = manager.getTableColumns(currentTableName);
        List<DataSet> originRows = manager.getDataContent(currentTableName);
        TableConstructor tableConstructor = new TableConstructor(tableColumns, originRows);

        manager.delete(currentTableName, definingEntry);
        view.write(String.format("You have successfully deleted data from '%s'", currentTableName));

        view.write(tableConstructor.getTableString());
    }

}
