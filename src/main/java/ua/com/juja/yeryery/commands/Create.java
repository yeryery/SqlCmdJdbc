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
            DataSet inputColumns = getInputColumns();
            manager.create(currentTableName, inputColumns);
            view.write(String.format("Your table '%s' have successfully created!", currentTableName));
        } catch (SQLException e) {
            view.write(e.getMessage());
            view.write("Try again.");
            process(input);
        }
    }

    private DataSet getInputColumns() {
        String sample = "columnName1|dataType1|columnName2|dataType2|...";
        String message = String.format("Enter the columnNames and its datatypes of the table you want to %s: \n%s\nor type 'cancel' to go back", ACTION, sample);
        view.write(message);
        String inputData = view.read();

        return Parser.splitByPairs(inputData);
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
}

