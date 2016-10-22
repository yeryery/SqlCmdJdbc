package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.NameTable;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;

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
        String[] names = manager.getTableNames();
        String currentTableName = dialog.askUser(names, view);

        if (!currentTableName.equals("cancel")) {
            int tableSize = -1;
            do {
                view.write("Please enter the number of columns of your table or '0' to go back");
                try {
                    tableSize = parseInt(view.read());
                    if (tableSize < 0) {
                        view.write("Number must be positive! Try again (or type '0' to go back).");
                    }
                } catch (NumberFormatException e) {
                    view.write("This is not number! Try again (or type '0' to go back).");

                }
            }
            while (tableSize < 0);

            if (tableSize != 0) {
                DataSet dataTypes = new DataSet();

                for (int i = 0; i < tableSize; i++) {
                    view.write("name of column " + (i + 1));
                    String columnName = view.read();

                    view.write("datatype of column " + (i + 1));
                    String dataType = view.read();

                    dataTypes.put(columnName, dataType);
                }

                try {
                    manager.create(currentTableName, dataTypes);
                    view.write("Your table '" + currentTableName + "' have successfully created!");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}

