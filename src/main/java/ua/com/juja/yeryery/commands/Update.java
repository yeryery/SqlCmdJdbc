package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.TableConstructor;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class Update implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "update";

    public Update(View view, DatabaseManager manager) {
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
        Dialog dialog = new DialogImpl(view, manager);
        String selectMessage = String.format("Please enter the name or select number of table you want to %s", ACTION);
        String findMessage = "Enter columnName and defining value of updated row";
        String commandSample = "columnName|value";

        try {
            String currentTableName = dialog.selectTable(selectMessage);
            String[] splitInput = dialog.findRow(currentTableName, findMessage, commandSample);
            DataSet newValues = dialog.setValues(currentTableName, splitInput);

            Set<String> tableColumns = manager.getTableColumns(currentTableName);
            List<DataSet> originRows = manager.getDataContent(currentTableName);
            TableConstructor tableConstructor = new TableConstructor(tableColumns, originRows);

            String columnName = splitInput[0];
            Object value = Parser.defineType(splitInput[1]);
            manager.update(currentTableName, newValues, columnName, value);

            view.write(String.format("You have successfully updated table '%s' at %s = %s", currentTableName, columnName, value));
            view.write(tableConstructor.getTableString());
        } catch (SQLException e1) {
            view.write(e1.getMessage());
            view.write("Try again.");
            process(input);
        } catch (CancelException e2) {
            view.write("Table updating canceled");
        }
    }
}

