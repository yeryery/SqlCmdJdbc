package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Update implements Command {

    private View view;
    private DatabaseManager manager;
    private final String ACTION = "update";

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
        Dialog dialog = new DialogImpl(view);
        String message = String.format("Please enter the name or select number of table you want to %s", ACTION);
        String currentTableName = dialog.SelectTable(names, message);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {

            while (true) {

                String[] splitInput;
                try {
                    splitInput = findRow(currentTableName);
                } catch (CancelException e) {
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
                List<DataSet> updatedRows = getRows(tableContent, columnName, value);

                DataSet newValues = null;
                try {
                    newValues = getNewValues(currentTableName, updatedRows);
                } catch (CancelException e) {
                    cancel = true;
                    break;
                }
                newValues.put(columnName, value);

                try {
                    manager.update(currentTableName, newValues, columnName);
                    view.write(String.format("You have successfully updated table '%s' at %s = %s", currentTableName, columnName, value));
                    Display display = new Display(view, manager);
                    Set<String> tableColumns = manager.getTableColumns(currentTableName);
                    display.printColumnNames(tableColumns);
                    display.printValues(tableContent);
                    break;
                } catch (SQLException e) {
                    view.write(e.getMessage());

                    boolean confirmed = dialog.isConfirmed("Do you want to try again?");
                    if (!confirmed) {
                        cancel = true;
                        break;
                    }
                }
            }
        }

        if (cancel) {
            view.write("Table updating canceled");
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
        view.write("Enter columnName and defining value of updated row: " + COMMAND_SAMPLE + "\n" +
                "or type 'cancel' to go back.");

        final String inputData = view.read();
        final String delimiter = "\\|";

        Parser parser = new Parser();
        String[] splitInput = parser.splitData(inputData, COMMAND_SAMPLE, delimiter);

        return splitInput;
    }

    private void checkValue(String currentTableName, String columnName, Object value) {
        List<DataSet> tableContent = manager.getDataContent(currentTableName);
        List<Object> columnValues = getColumnValues(tableContent, columnName);

        if (!columnValues.contains(value)) {
            throw new IllegalArgumentException(String.format("Column '%s' doesn't contain value '%s'!", columnName, value));
        }
    }

    private void checkColumn(String currentTableName, String columnName) {
        Set<String> tableColumns = manager.getTableColumns(currentTableName);

        if (!tableColumns.contains(columnName)) {
            throw new IllegalArgumentException(String.format("Table '%s' doesn't contain column '%s'!", currentTableName, columnName));
        }
    }

    private List<DataSet> getRows(List<DataSet> rows, String columnName, Object value) {
        List<DataSet> result = new LinkedList<>();

        for (DataSet row : rows) {
            if (row.get(columnName).equals(value)) {
                result.add(row);
            }
        }
        return result;
    }

    private boolean checkNewValues(DataSet newValues, List<DataSet> updatedRows) {
        for (String columnName : newValues.getColumnNames()) {
            for (DataSet row : updatedRows) {
                if (!newValues.get(columnName).equals(row.get(columnName))) {
                    return true;
                }
            }
        }
        return false;
    }

    private DataSet getNewValues(String tableName, List<DataSet> updatedRows) {
        while (true) {
            DataSet newValues = null;
            try {
                String[] splitInput = getNewData();
                newValues = getDataSet(tableName, splitInput);
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
                view.write("Try again.");
                continue;
            }

            if (!checkNewValues(newValues, updatedRows)) {
                view.write("The new values are equivalent to the updated");
                continue;
            }
            return newValues;
        }
    }

    private DataSet getDataSet(String currentTableName, String[] splitInput) {
        DataSet newValues = new DataSetImpl();

        for (int i = 0; i < splitInput.length; i++) {
            Parser parser = new Parser();
            String updatedColumn = splitInput[i++];
            Object newValue = parser.defineType(splitInput[i]);
            checkColumn(currentTableName, updatedColumn);

            newValues.put(updatedColumn, newValue);
        }
        return newValues;
    }

    private String[] getNewData() throws IllegalArgumentException {
        view.write("Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.");

        final String inputData = view.read();
        final String delimiter = "\\|";

        Parser parser = new Parser();
        String[] splitInput = parser.splitByTwo(inputData, delimiter);

        return splitInput;
    }

    private List<Object> getColumnValues(List<DataSet> dataSets, String columnName) {
        List<Object> result = new LinkedList<>();

        for (DataSet dataSet : dataSets) {
            result.add(dataSet.get(columnName));
        }
        return result;
    }
}

