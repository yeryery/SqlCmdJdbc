package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.commands.CancelException;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.*;

import static java.lang.Integer.parseInt;

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
    public String[] findRow(String tableName, String message, String sample) throws CancelException {
        String[] input = new String[0];

        while (true) {
            try {
                input = getInput(message, sample);
                String columnName = input[0];
                Object value = Parser.defineType(input[1]);
                checkColumn(tableName, columnName);
                checkValue(tableName, columnName, value);
                break;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
                view.write("Try again.");
            }
        }
        return input;
    }

    private String[] getInput(String message, String sample) throws CancelException {
        view.write(String.format("%s: %s\nor type 'cancel' to go back.", message, sample));

        final String inputData = view.read();
        final String delimiter = "\\|";

        String[] splitInput = Parser.splitData(inputData, sample, delimiter);

        return splitInput;
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
    public DataSet setValues(String tableName, String[] input) {

        while (true) {
            String columnName = input[0];
            Object value = Parser.defineType(input[1]);
            DataSet newValues = new DataSetImpl();
            try {
                newValues = getInputByTwo(tableName);
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
                view.write("Try again.");
                continue;
            }

            List<DataSet> updatedRows = getUpdatedRows(tableName, columnName, value);

            if (!isNewValues(newValues, updatedRows)) {
                view.write("The new values are equivalent to the updated");
                continue;
            }

            newValues.put(columnName, value);
            // TODO убрать
            return newValues;
        }
    }

    private DataSet getInputByTwo(String tableName) {
        view.write("Enter columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                "or type 'cancel' to go back.");

        String inputData = view.read();
        String delimiter = "\\|";
        DataSet splitInput = Parser.splitByTwo(inputData, delimiter);
        checkColumns(tableName, splitInput);

        return splitInput;
    }

    private void checkColumns(String tableName, DataSet checkedDataSet) {
        Set<String> tableColumns = manager.getTableColumns(tableName);
        Set<String> checkedColumns = checkedDataSet.getColumnNames();

        for (String columnName : checkedColumns) {
            if (!tableColumns.contains(columnName)) {
                throw new IllegalArgumentException(String.format("Table '%s' doesn't contain column '%s'!", tableName, columnName));
            }
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

    @Override
    public DataSet setColumnNames(String message, String sample) {
        int tableSize = getTableSize();
        DataSet dataTypes = new DataSetImpl();

        for (int i = 0; i < tableSize; i++) {
            String setMessage = message + (i + 1);
            String[] input = new String[0];
            try {
                input = getInput(setMessage, sample);
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
                i--;
                continue;
            }

            String columnName = input[0];
            String dataType = input[1];
            dataTypes.put(columnName, dataType);
        }
        return dataTypes;
    }

    private int getTableSize() {
        int tableSize = 0;

        while (true) {
            view.write("Please enter the number of columns of your table or 'cancel' to go back");
            String read = view.read();
            try {
                tableSize = parseInt(read);
            } catch (NumberFormatException e) {
                if (read.equals("cancel")) {
                    throw new CancelException();
                } else {
                    view.write(String.format("You have entered '%s' and this is not a number!", read));
                    view.write("Try again.");
                    continue;
                }
            }
            if (tableSize <= 0) {
                view.write(String.format("You have entered '%s' and number of columns must be positive!", tableSize));
                view.write("Try again.");
                continue;
            }
            return tableSize;
        }
    }
}
