package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.Parser;
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
    public String SelectTable(String message) {

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
        return tableName;
    }

    @Override
    public String NameTable(String message) {
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

    private void checkName(Set<String> names, String tableName) {
        if (!checkFirstChar(tableName)) {
            throw new IllegalArgumentException(String.format("You have entered '%s' and table name must begin with a letter!", tableName));
        }
        if (existName(names, tableName)) {
            throw new IllegalArgumentException(String.format("Table with name '%s' already exists!\n%s", tableName, names.toString()));
        }
    }

    private boolean checkFirstChar(String tableName) {
        char firstLetter = tableName.charAt(0);
        return (firstLetter >= 'a' && firstLetter <= 'z') && !(firstLetter >= 'A' && firstLetter <= 'Z');
    }

    private boolean existName(Set<String> names, String tableName) {
        return names.contains(tableName);
    }

    @Override
    public boolean isConfirmed(String warning) {
        String confirm = "";

        while (!confirm.equals("y") && !confirm.equals("n")) {
            view.write(warning + " (y/n)");
            confirm = (String) view.read();
        }

        boolean result = false;
        if (confirm.equals("y")) {
            result = true;
        }
        return result;
    }

    @Override
    public String[] findRow(String tableName, String action, String sample) {
        String[] input = getInput(action, sample);

        Parser parser = new Parser();
        String columnName = input[0];
        Object value = parser.defineType(input[1]);

        if (!existColumn(tableName, columnName)) {
            throw new IllegalArgumentException(String.format("Table '%s' doesn't contain column '%s'!", tableName, columnName));
        }
        if (!existValue(tableName, columnName, value)) {
            throw new IllegalArgumentException(String.format("Column '%s' doesn't contain value '%s'!", columnName, value));
        }

        return input;
    }

    private String[] getInput(String action, String sample) {
        view.write(String.format("Enter columnName and defining value of %sd row: %s\nor type 'cancel' to go back.", action, sample));

        final String inputData = view.read();
        final String delimiter = "\\|";

        Parser parser = new Parser();
        String[] splitInput = parser.splitData(inputData, sample, delimiter);

        return splitInput;
    }

    private boolean existColumn(String tableName, String columnName) {
        Set<String> tableColumns = manager.getTableColumns(tableName);
        return tableColumns.contains(columnName);
    }

    private boolean existValue(String tableName, String columnName, Object value) {
        List<DataSet> tableContent = manager.getDataContent(tableName);
        List<Object> columnValues = getColumnValues(tableContent, columnName);

        return columnValues.contains(value);
    }

    private List<Object> getColumnValues(List<DataSet> dataSets, String columnName) {
        List<Object> result = new LinkedList<>();

        for (DataSet dataSet : dataSets) {
            result.add(dataSet.get(columnName));
        }
        return result;
    }
}
