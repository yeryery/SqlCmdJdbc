package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataEntry;
import ua.com.juja.yeryery.manager.DataEntryImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.*;

public class Dialog {

    private View view;
    private DatabaseManager manager;

    public Dialog(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    public String selectTable(String action) {
        Map<Integer, String> tableList = getTableList();
        String tableName;

        while (true) {
            printTableList(action, tableList);

            try {
                tableName = getTableName(tableList);
                break;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
        return tableName;
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

    private void printTableList(String action, Map<Integer, String> tableList) {
        String message = String.format("Select the table you need for '%s' command", action);
        view.write(message);
        tableList.remove(0);

        for (Map.Entry<Integer, String> entry : tableList.entrySet()) {
            view.write(entry.getKey() + ". " + entry.getValue());
        }

        view.write(0 + ". " + "cancel (to go back)");
    }

    private String getTableName(Map<Integer, String> tableList) {
        String input = view.read();
        checkCancel(input);

        String tableName;
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

    private void checkCancel(String input) {
        if (input.equals("cancel") || input.equals("0")) {
            throw new CancelException();
        }
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

    public void confirmAction(String action, String tableName) {
        String confirm = "";
        String warning = String.format("Are you sure you want to %s table '%s'?", action, tableName);

        while (!confirm.equals("y") && !confirm.equals("n")) {
            view.write(warning + " (y/n)");
            confirm = view.read();
        }

        if (confirm.equals("n")) {
            throw new CancelException();
        }
    }

    public DataEntry defineRow(String tableName, String action) {
        while (true) {
            try {
                DataEntry input = getInput(action);
                checkEntry(tableName, input);
                return input;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
    }

    private DataEntry getInput(String action) throws IllegalArgumentException {
        String message = "Enter the columnName and defining value of the row you want to %s: %s\nor type 'cancel' to go back";
        String sample = "columnName|value";
        view.write(String.format(message, action, sample));

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

    public DataSet getNewEntries(String tableName, DataEntry entry) {
        while (true) {
            DataSet newValues;
            String message = "Enter the columnNames and its new values for updated row: \n" +
                    "updatedColumn1|newValue1|updatedColumn2|newValue2|...\nor type 'cancel' to go back";
            try {
                newValues = getInputByTwo(message);
                checkColumns(tableName, newValues);
                return newValues;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
    }

    public DataSet getInputByTwo(String message) {
        view.write(message);
        String inputData = view.read();
        String delimiter = "\\|";
        DataSet splitInput = Parser.splitByTwo(inputData, delimiter);
        return splitInput;
    }

    private void checkColumns(String tableName, DataSet dataSet) {
        Set<String> checkedColumns = dataSet.getColumnNames();

        for (String columnName : checkedColumns) {
            checkColumn(tableName, columnName);
        }
    }
}
