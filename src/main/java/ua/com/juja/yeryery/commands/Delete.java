package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Delete implements Command {

    private View view;
    private DatabaseManager manager;
    private final String ACTION = "delete";

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
        Set<String> tableNames = manager.getTableNames();
        Dialog dialog = new DialogImpl(view);
        String message = String.format("Please enter the name or select number of table where you want to %s rows", ACTION);
        String currentTableName = dialog.SelectTable(tableNames, message);

        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {
            while (true) {

                String[] splitInput;
                try {
                    splitInput = findRow(currentTableName);
                } catch (CancelCommandException e) {
                    cancel = true;
                    break;
                } catch (IllegalArgumentException e) {
                    view.write(e.getMessage());
                    view.write("Try again.");
                    continue;
                }

                Parser parser = new Parser();
                String columnName = splitInput[0];
                Object value = parser.defineType(splitInput[1]);

                List<DataSet> tableContent = manager.getDataContent(currentTableName);

                manager.delete(currentTableName, columnName, value);
                view.write(String.format("You have successfully deleted data from '%s' at %s = %s", currentTableName, columnName, value));

                Display display = new Display(view, manager);
                Set<String> tableColumns = manager.getTableColumns(currentTableName);
                display.printColumnNames(tableColumns);
                display.printValues(tableContent);
                break;
            }
        }

        if (cancel) {
            view.write("Table deleting canceled");
        }

    }

    private String[] findRow(String tableName) {
        String[] input = getInput();

        Parser parser = new Parser();
        String columnName = input[0];
        Object value = parser.defineType(input[1]);

        checkColumn(tableName, columnName);
        checkValue(tableName, columnName, value);

        return input;
    }

    private String[] getInput() {
        final String COMMAND_SAMPLE = "columnName|value";
        view.write("Enter columnName and defining value of deleted row: " + COMMAND_SAMPLE + "\n" +
                "or type 'cancel' to go back.");

        final String inputData = view.read();
        final String delimiter = "\\|";

        Parser parser = new Parser();
        String[] splitInput = parser.splitData(inputData, COMMAND_SAMPLE, delimiter);

        return splitInput;
    }

    private void checkColumn(String currentTableName, String columnName) {
        Set<String> tableColumns = manager.getTableColumns(currentTableName);

        if (!tableColumns.contains(columnName)) {
            throw new IllegalArgumentException(String.format("Table '%s' doesn't contain column '%s'!", currentTableName, columnName));
        }
    }

    private void checkValue(String currentTableName, String columnName, Object value) {
        List<DataSet> tableContent = manager.getDataContent(currentTableName);
        List<Object> columnValues = getColumnValues(tableContent, columnName);

        if (!columnValues.contains(value)) {
            throw new IllegalArgumentException(String.format("Column '%s' doesn't contain value '%s'!", columnName, value));
        }
    }

    private List<Object> getColumnValues(List<DataSet> dataSets, String columnName) {
        List<Object> result = new LinkedList<>();

        for (DataSet dataSet : dataSets) {
            result.add(dataSet.get(columnName));
        }
        return result;
    }
}
