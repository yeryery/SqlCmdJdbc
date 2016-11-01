package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.Set;

public class Update implements Command {

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;

    public Update(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        dialog = new SelectTable();
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("update");
    }

    @Override
    public void process(String input) {
        Set<String> names = manager.getTableNames();
        String currentTableName = dialog.askUser(names, view);

        if (!currentTableName.equals("cancel")) {
            int size = -1;

            while (size < 0) {
                view.write("Enter id you want to update and its new values: " +
                        "id|columnName1|newValue1|columnName2|newValue2...\n" +
                        "or type 'cancel' to go back.");

                String inputValues = view.read();
                String[] newValues = inputValues.split("\\|");
                size = newValues.length;

                if (size % 2 == 0 || size == 1) {
                    if (inputValues.equals("cancel")) {
                        view.write("The updating of table '" + currentTableName + "' is cancelled");
                        size = 0;
                    } else {
                        view.write("You should enter an odd number of parameters (3 or more)!\n" +
                                "Try again.");
                        size = -1;
                    }
                } else {
                    DataSet updatedRow = new DataSetImpl();

                    for (int i = 0; i < size / 2; i++) {
                        updatedRow.put(newValues[2 * i + 1], newValues[2 * i + 2]);
                    }

                    int id = Integer.parseInt(newValues[0]);

                    try {
                        manager.update(currentTableName, updatedRow, id);
                        view.write("You have successfully updated table '" + currentTableName + "' at id = " + id);
                    } catch (SQLException e) {
                        String errorMessage = editErrorMessage(e);
                        view.write(errorMessage);
                        size = -1;
                    }
                        // TODO NumberFormatException
                }
            }
        } else {
            view.write("Table updating canceled");
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

