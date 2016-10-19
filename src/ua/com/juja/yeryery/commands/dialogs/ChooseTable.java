package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.commands.ExitException;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class ChooseTable implements Dialog {
    @Override
    public String askUser(DatabaseManager manager, View view) {
        String[] tableNames = manager.getTableNames();

        int size = 0;
        int tableNumber = -1;
        while (tableNumber < 0 || tableNumber > size) {
            view.write("Please select number of table you need or '0' to exit");

            size = tableNames.length;
            for (int i = 0; i < size; i++) {
                view.write((i + 1) + ". " + tableNames[i]);
            }
            view.write("0. Exit");

            try {
                tableNumber = Integer.parseInt(view.read());
                if (tableNumber == 0) {
                    view.write("See you!");
                    throw new ExitException();
                }
                if (tableNumber < 0 || tableNumber > size) {
                    view.write("There is no table with this number! Try again.");
                }
            } catch (NumberFormatException e) {
                view.write("This is not a number! Try again.");
            }
        }

        String tableName = "";
        for (int i = 0; i < size; i++) {
            if (i == tableNumber - 1) {
                tableName = tableNames[i];
            }
        }
        return tableName;
    }
}
