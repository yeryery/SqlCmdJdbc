package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.commands.CancelException;
import ua.com.juja.yeryery.commands.IllegalArgumentException;
import ua.com.juja.yeryery.commands.Parser;
import ua.com.juja.yeryery.manager.DataEntry;
import ua.com.juja.yeryery.manager.DataEntryImpl;
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

    @Override
    public String selectTable(String message) {
        Map<Integer, String> tableList = getTableList();
        String tableName;

        while (true) {
            printTableList(message, tableList);
            String input = view.read();

            try {
                tableName = getTableName(input, tableList);
                break;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
        return tableName;
    }

    @Override
    public String nameTable(String message) {
        Set<String> names = manager.getTableNames();
        String tableName;

        while (true) {
            view.write(message);
            tableName = view.read();

            try {
                checkNewName(names, tableName);
                break;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
        return tableName;
    }

    private void printTableList(String message, Map<Integer, String> tableList) {
        view.write(message);
        tableList.remove(0);

        for (Map.Entry<Integer, String> entry : tableList.entrySet()) {
            view.write(entry.getKey() + ". " + entry.getValue());
        }

        view.write(0 + ". " + "cancel (to go back)");
    }

    private Map<Integer, String> getTableList() {
        Set<String> names = manager.getTableNames();
        Map<Integer, String> tableList = new HashMap<>();
        Iterator iterator = names.iterator();
        int i = 1;

        while (iterator.hasNext()) {
            tableList.put(i, (String) iterator.next());
            i++;
        }
        return tableList;
    }

    private String getTableName(String input, Map<Integer, String> tableList) {
        String tableName;
        checkCancel(input);

        if (Parser.isParsable(input)) {
            int tableNumber = Parser.parsedInt;
            checkTableNumber(tableNumber, tableList);
            tableName = tableList.get(tableNumber);
        } else {
            checkTableName(input, tableList);
            tableName = input;
        }
        return tableName;
    }

    private void checkTableNumber(int tableNumber, Map<Integer, String> tableList) {
        if (!tableList.containsKey(tableNumber)) {
            throw new IllegalArgumentException(String.format("There is no table with number %d", tableNumber));
        }
    }

    private void checkTableName(String tableName, Map<Integer, String> tableList) {
        if (!tableList.containsValue(tableName)) {
            throw new IllegalArgumentException(String.format("Table with name '%s' doesn't exist", tableName));
        }
    }

    private void checkCancel(String input) {
        if (input.equals("cancel") || input.equals("0")) {
            throw new CancelException();
        }
    }

    private void checkNewName(Set<String> names, String tableName) {
        if (tableName.equals("cancel")) {
            throw new CancelException();
        }
        if (!isFirstLetter(tableName)) {
            throw new IllegalArgumentException(String.format("You have entered '%s' and tablename must begin with a letter", tableName));
        }
        if (existName(names, tableName)) {
            throw new IllegalArgumentException(String.format("Table with name '%s' already exists\n%s", tableName, names.toString()));
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
    public DataEntry defineRow(String tableName, String message, String sample) {
        while (true) {
            try {
                DataEntry input = getInput(message, sample);
                checkEntry(tableName, input);
                return input;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
    }

    private DataEntry getInput(String message, String sample) throws IllegalArgumentException {
        view.write(String.format(message, sample));

        String inputData = view.read();
        String delimiter = "\\|";
        String[] split = Parser.splitData(inputData, sample, delimiter);
        String columnName = split[0];
        Object value = Parser.defineType(split[1]);

        return new DataEntryImpl(columnName, value);
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
            throw new IllegalArgumentException(String.format("Table '%s' doesn't contain column '%s'", tableName, columnName));
        }
    }

    private void checkValue(String tableName, String columnName, Object value) {
        List<DataSet> tableContent = manager.getDataContent(tableName);
        List<Object> columnValues = getColumnValues(tableContent, columnName);
        if (!columnValues.contains(value)) {
            throw new IllegalArgumentException(String.format("Column '%s' doesn't contain value '%s'", columnName, value));
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
    public DataSet getNewValues(String tableName, String message, DataEntry entry) {
        while (true) {
            DataSet newValues;
            try {
                newValues = getInputByTwo(message);
                checkColumns(tableName, newValues);
                checkNewValues(tableName, entry, newValues);
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
                continue;
            }

            return newValues;
        }
    }

    private void checkNewValues(String tableName, DataEntry entry, DataSet newValues) {
        String columnName = entry.getColumn();
        Object value = entry.getValue();
        List<DataSet> updatedRows = getUpdatedRows(tableName, columnName, value);

        if (!isNewValues(newValues, updatedRows)) {
            throw new IllegalArgumentException("Your entries are equivalent to the updated");
        }
    }

    @Override
    public DataSet getInputByTwo(String message) {
        view.write(message);
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
