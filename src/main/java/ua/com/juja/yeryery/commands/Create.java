package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.SQLErrorPrinter;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.NameTable;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.Set;

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

        Dialog dialog = new NameTable();
        String currentTableName = dialog.askUser(names, view, ACTION);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {

            while (true) {
                view.write("Please enter the number of columns of your table or 'cancel' to go back");
                String read = view.read();
                int tableSize;

                try {
                    tableSize = parseInt(read);

                    if (tableSize <= 0) {
                        view.write("Number must be positive!");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    if (read.equals("cancel")) {
                        cancel = true;
                        break;
                    } else {
                        view.write("This is not a number!");
                        continue;
                    }
                }

                DataSet dataTypes = putColumnNames(tableSize);

                try {
                    manager.create(currentTableName, dataTypes);
                    view.write("Your table '" + currentTableName + "' have successfully created!");
                    break;
                } catch (SQLException e) {
                    SQLErrorPrinter error = new SQLErrorPrinter(e);
                    error.printSQLError();
                }
            }
        }

        if (cancel){
            view.write("Table creating canceled");
        }
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

