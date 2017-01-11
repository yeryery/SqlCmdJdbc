package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.Set;

public class Create implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "create";

    public Create(View view, DatabaseManager manager) {
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

        try {
            String currentTableName = nameTable();
            String setColumnsMessage = "Enter the columnNames and its datatypes of the table you want to create:\n" +
                    "columnName1|dataType1|columnName2|dataType2|...\n" +
                    "or type 'cancel' to go back";

            DataSet inputColumns = getNewColumns(setColumnsMessage, dialog);
            manager.create(currentTableName, inputColumns);
            view.write(String.format("Your table '%s' have successfully created!", currentTableName));
        } catch (SQLException e) {
            view.write(e.getMessage());
            view.write("Try again.");
            process(input);
        }
    }

    private DataSet getNewColumns(String message, Dialog dialog) {
        while (true) {
            try {
                DataSet inputColumns = dialog.getEntries(message);
                checkNewColumns(inputColumns);
                return inputColumns;
            } catch (IllegalArgumentException e) {
                view.write(e.getMessage());
            }
        }
    }

    private void checkNewColumns(DataSet dataSet) {
        Set<String> checkedColumns = dataSet.getColumnNames();

        for (String columnName : checkedColumns) {
            checkFirstLetter(columnName);
        }
    }

    private String nameTable() {
        Set<String> names = manager.getTableNames();
        String tableName;

        while (true) {
            String message = "Enter the name of your table or 'cancel' to go back";
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

    private void checkNewName(Set<String> names, String tableName) {
        checkFirstLetter(tableName);

        if (tableName.equals("cancel")) {
            throw new CancelException();
        }
        existName(names, tableName);
    }

    private void checkFirstLetter(String name) {
        char firstLetter = name.charAt(0);
        boolean isFirstLetter = (firstLetter >= 'a' && firstLetter <= 'z') || (firstLetter >= 'A' && firstLetter <= 'Z');
        if (!isFirstLetter) {
            throw new IllegalArgumentException(String.format("You have entered '%s' and name must begin with a letter", name));
        }
    }

    private void existName(Set<String> names, String tableName) {
        if (names.contains(tableName)) {
            throw new IllegalArgumentException(String.format("Table with name '%s' already exists\n%s", tableName, names.toString()));
        }
    }
}