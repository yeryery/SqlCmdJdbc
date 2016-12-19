package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.SQLErrorPrinter;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.CancellationException;

import static java.lang.Integer.parseInt;

public class Create implements Command {

    private View view;
    private DatabaseManager manager;
    private final String ACTION = "create";

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
        Set<String> names = manager.getTableNames();

        Dialog dialog = new DialogImpl(view);
        String message = String.format("Please enter the name of table you want to %s or 'cancel' to go back", ACTION);
        String currentTableName = dialog.NameTable(names, message);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {

            while (true) {

                int tableSize = 0;
                try {
                    tableSize = getTableSize();
                } catch (CancellationException e) {
                    cancel = true;
                    break;
                } catch (IllegalArgumentException e) {
                    view.write(e.getMessage());
                    view.write("Try again.");
                    continue;
                }

                DataSet dataTypes = putColumnNames(tableSize);

                try {
                    manager.create(currentTableName, dataTypes);
                    view.write(String.format("Your table '%s' have successfully created!", currentTableName));
                    break;
                } catch (SQLException e) {
                    SQLErrorPrinter error = new SQLErrorPrinter(e);
                    error.printSQLError();

                    boolean confirmed = dialog.isConfirmed("Do you want to try again?");
                    if (!confirmed) {
                        cancel = true;
                        break;
                    }
                }
            }
        }

        if (cancel){
            view.write("Table creating canceled");
        }
    }

    private int getTableSize() {
        view.write("Please enter the number of columns of your table or 'cancel' to go back");
        String read = view.read();
        int tableSize;
        try {
            tableSize = parseInt(read);
        } catch (NumberFormatException e) {
            if (read.equals("cancel")) {
                throw new CancellationException();
            } else {
                throw new IllegalArgumentException("This is not a number!");
            }
        }
        if (tableSize <= 0) {
            throw new IllegalArgumentException("Number must be positive!");
        }
        return tableSize;
    }

    private DataSet putColumnNames(int tableSize) {
        DataSet dataTypes = new DataSetImpl();

        for (int i = 0; i < tableSize; i++) {
            view.write("Please enter name|type of column " + (i + 1) + ":");
            String inputNameType = view.read();
            String[] nameType = inputNameType.split("\\|");

            if (nameType.length != 2) {
                view.write("You should enter two parameters: name|type\n" +
                        "Try again.");
                i--;
                continue;
            }

            String columnName = nameType[0];
            String dataType = nameType[1];
            dataTypes.put(columnName, dataType);
        }
        return dataTypes;
    }
}

