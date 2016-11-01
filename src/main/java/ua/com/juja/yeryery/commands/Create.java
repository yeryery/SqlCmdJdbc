package ua.com.juja.yeryery.commands;

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
    private Dialog dialog;

    public Create(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        dialog = new NameTable();
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("create");
    }

    @Override
    public void process(String input) {
        Set<String> names = manager.getTableNames();
        String currentTableName = dialog.askUser(names, view);

        if (!currentTableName.equals("cancel")) {
            int tableSize = -1;

            while (tableSize < 0) {
                view.write("Please enter the number of columns of your table or '0' to go back");
                try {
                    tableSize = parseInt(view.read());

                    if (tableSize < 0) {
                        view.write("Number must be positive!");
                    }
                } catch (NumberFormatException e) {
                    view.write("This is not number!");
                }


                if (tableSize == 0) {
                    view.write("The creating of table '" + currentTableName + "' is cancelled");
                }

                if (tableSize > 0) {
                    DataSet dataTypes = new DataSetImpl();

                    for (int i = 0; i < tableSize; i++) {
                        view.write("name of column " + (i + 1) + ":");
                        String columnName = view.read();

                        view.write("type of column " + (i + 1) + ":");
                        String dataType = view.read();

                        dataTypes.put(columnName, dataType);
                    }

                    try {
                        manager.create(currentTableName, dataTypes);
                        view.write("Your table '" + currentTableName + "' have successfully created!");
                    } catch (SQLException e) {
                        String errorMessage = editErrorMessage(e);
                        view.write(errorMessage);
                        tableSize = -1;
                    }
                }
            }
        } else {
            view.write("Table creating canceled");
        }
    }

    private String editErrorMessage(SQLException e) {
        String result = "SQL " + e.getMessage();
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '\n') {
                result = result.substring(0, i);
                break;
            }
        }
        return result + "!\nTry again.";
    }
}

