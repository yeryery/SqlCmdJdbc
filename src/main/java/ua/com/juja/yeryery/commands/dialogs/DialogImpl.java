package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.commands.CancelException;
import ua.com.juja.yeryery.commands.Parser;
import ua.com.juja.yeryery.manager.DataEntry;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.*;

public class DialogImpl implements Dialog {

    private View view;
    private DatabaseManager manager;

    public DialogImpl(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public DialogImpl(View view) {
        this.view = view;
    }

    @Override
    public String selectTable(String message) {
        Set<String> names = manager.getTableNames();
        Map<Integer, String> tableNames = new HashMap<>();

        Iterator iterator = names.iterator();
        int i = 1;

        while (iterator.hasNext()) {
            tableNames.put(i, (String) iterator.next());
            i++;
        }

        String tableName;

        while (true) {
            view.write(message);

            tableNames.remove(0);

            for (Map.Entry<Integer, String> entry : tableNames.entrySet()) {
                view.write(entry.getKey() + ". " + entry.getValue());
            }

            view.write(0 + ". " + "cancel (to go back)");
            tableNames.put(0, "cancel");

            String input = view.read();
            Parser parser = new Parser();

            if (parser.isParsable(input)) {
                int tableNumber = parser.getParsedInt();

                if (tableNumber >= 0 && tableNumber <= names.size()) {
                    tableName = tableNames.get(tableNumber);
                    break;
                } else {
                    view.write("There is no table with this number! Try again.");
                    //TODO Exceptions
                }
            } else {

                if (tableNames.containsValue(input)) {
                    tableName = input;
                    break;
                } else {
                    view.write(String.format("Table with name '%s' doesn't exist! Try again.", input));
                }
            }
        }
        checkCancel(tableName);

        return tableName;
    }

    @Override
    public String nameTable(String message) {
        String tableName;
        Set<String> names = manager.getTableNames();

        while (true) {
            view.write(message);
            tableName = view.read();

            try {
                checkName(names, tableName);
                break;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
                view.write("Try again.");
            }
        }
        return tableName;
    }

    private void checkCancel(String input) {
        if (input.equals("cancel")) {
            throw new CancelException();
        }
    }

    private void checkName(Set<String> names, String tableName) {
        if (tableName.equals("cancel")) {
            throw new CancelException();
        }
        if (!isFirstLetter(tableName)) {
            throw new IllegalArgumentException(String.format("You have entered '%s' and table name must begin with a letter!", tableName));
        }
        if (existName(names, tableName)) {
            throw new IllegalArgumentException(String.format("Table with name '%s' already exists!\n%s", tableName, names.toString()));
        }
    }

    private boolean isFirstLetter(String tableName) {
        char firstLetter = tableName.charAt(0);
        return (firstLetter >= 'a' && firstLetter <= 'z') && !(firstLetter >= 'A' && firstLetter <= 'Z');
    }

    private boolean existName(Set<String> names, String tableName) {
        return names.contains(tableName);
    }

    @Override
    public void confirmAction(String warning) {
        String confirm = "";

        while (!confirm.equals("y") && !confirm.equals("n")) {
            view.write(warning + " (y/n)");
            confirm = (String) view.read();
        }

        if (confirm.equals("n")) {
            throw new CancelException();
        }
    }

    @Override
    public DataEntry findRow(String tableName, String message, String sample) throws CancelException {
        DataEntry input = null;

        try {
            input = getInput(message, sample);
            checkEntry(tableName, input);
        } catch (IllegalArgumentException e) {
            view.write(e.getMessage());
            view.write("Try again.");
            findRow(tableName, message, sample);
        }
        return input;
    }

    private DataEntry getInput(String message, String sample) throws CancelException {
        view.write(String.format("%s: %s\nor type 'cancel' to go back.", message, sample));

        String inputData = view.read();
        String delimiter = "\\|";
        String[] split = Parser.splitData(inputData, sample, delimiter);
        String columnName = split[0];
        Object value = Parser.defineType(split[1]);
        DataEntry splitInput = new DataEntry(columnName, value);

        return splitInput;
    }

    private void checkEntry(String tableName, DataEntry entry) {
        String columnName = entry.getColumn();
        checkColumn(tableName, columnName);
        Object value = entry.getValue();
        checkValue(tableName, columnName, value);
    }

    private void checkColumn(String tableName, String columnName) {
        Set<String> tableColumns = manager.getTableColumns(tableName);
        if (!tableColumns.contains(columnName)) {
            throw new IllegalArgumentException(String.format("Table '%s' doesn't contain column '%s'!", tableName, columnName));
        }
    }

    private void checkValue(String tableName, String columnName, Object value) {
        List<DataSet> tableContent = manager.getDataContent(tableName);
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

    @Override
    public DataSet setValues(String tableName, String message, DataEntry entry) {

        while (true) {
            DataSet newValues;
            try {
                newValues = getInputByTwo(message);
                checkColumns(tableName, newValues);
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
                view.write("Try again.");
                continue;
            }

            String columnName = entry.getColumn();
            Object value = entry.getValue();
            List<DataSet> updatedRows = getUpdatedRows(tableName, columnName, value);

            if (!isNewValues(newValues, updatedRows)) {
                view.write("The new values are equivalent to the updated");
                continue;
            }
            return newValues;
        }
    }

    @Override
    public DataSet getInputByTwo(String message) {
        view.write(String.format("%s\nor type 'cancel' to go back.", message));

        String inputData = view.read();
        String delimiter = "\\|";
        DataSet splitInput = Parser.splitByTwo(inputData, delimiter);

        return splitInput;
    }

    private void checkColumns(String tableName, DataSet checkedDataSet) {
        Set<String> checkedColumns = checkedDataSet.getColumnNames();

        for (String columnName : checkedColumns) {
            checkColumn(tableName, columnName);
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

    private boolean isNewValues(DataSet newValues, List<DataSet> updatedRows) {
        for (String columnName : newValues.getColumnNames()) {
            for (DataSet row : updatedRows) {
                Object newValue = newValues.get(columnName);
                Object updatedValue = row.get(columnName);
                if (!newValue.equals(updatedValue)) {
                    return true;
                }
            }
        }
        return false;
    }
}
