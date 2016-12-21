package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.TableConstructor;
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
    private static final String ACTION = "update";
    private static final String COMMAND_SAMPLE = "columnName|value";

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
        String message = String.format("Please enter the name or select number of table you want to %s", ACTION);
        String currentTableName = dialog.SelectTable(message);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {

            while (true) {

                String[] splitInput;
                try {
                    splitInput = dialog.findRow(currentTableName, ACTION, COMMAND_SAMPLE);
                } catch (CancelException e) {
                    cancel = true;
                    break;
                } catch (IllegalArgumentException e) {
                    view.write(e.getMessage());
                    view.write("Try again.");
                    continue;
                }


                DataSet newValues = new DataSetImpl();
                try {
                    newValues = getNewValues(currentTableName, splitInput);
                } catch (CancelException e) {
                    cancel = true;
                    break;
                }

                try {
                    Set<String> tableColumns = manager.getTableColumns(currentTableName);
                    List<DataSet> originRows = manager.getDataContent(currentTableName);
                    String columnName = splitInput[0];
                    Object value = splitInput[1];

                    manager.update(currentTableName, newValues, columnName);
                    view.write(String.format("You have successfully updated table '%s' at %s = %s", currentTableName, columnName, value));

                    TableConstructor tableConstructor = new TableConstructor(tableColumns, originRows);
                    view.write(tableConstructor.getTableString());
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

    private List<DataSet> getUpdatedRows(String tableName, String columnName, Object value) {
        List<DataSet> dataSets = manager.getDataContent(tableName);
        List<DataSet> result = new LinkedList<>();

        for (DataSet row : dataSets) {
            if (row.get(columnName).equals(value)) {
                result.add(row);
            }
        }
        return result;
    }

    private DataSet getNewValues(String tableName, String[] input) {

        while (true) {
            Parser parser = new Parser();
            String columnName = input[0];
            Object value = parser.defineType(input[1]);

            DataSet newValues = new DataSetImpl();
            try {
                String[] splitInput = getNewData();
                newValues = getDataSet(tableName, splitInput);
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
                view.write("Try again.");
                continue;
            }

            List<DataSet> updatedRows = getUpdatedRows(tableName, columnName, value);

            if (!checkNewValues(newValues, updatedRows)) {
                view.write("The new values are equivalent to the updated");
                continue;
            }

            newValues.put(columnName, value);
            return newValues;
        }
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

    private void checkColumn(String tableName, String columnName) {
        Set<String> tableColumns = manager.getTableColumns(tableName);

        if (!tableColumns.contains(columnName)) {
            throw new IllegalArgumentException(String.format("Table '%s' doesn't contain column '%s'!", tableName, columnName));
        }
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
}

