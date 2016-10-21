package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;

public class ChooseTable implements Dialog {
    @Override
    public String askUser(DatabaseManager manager, View view) {
        String[] names = manager.getTableNames();

        int size = 0;
        int tableNumber = -1;


        String tableName = "";

        while (tableName.equals("")) {
        view.write("Please enter the name or select number of table you need");

        size = names.length;
        Arrays.sort(names);
        for (int i = 0; i < size; i++) {
            view.write((i + 1) + ". " + names[i]);
        }
        view.write("0. back (go back to the menu)");
            String input = view.read();

            if (isParsable(input)) {
                tableNumber = Integer.parseInt(input);
                String check = "";
                if (tableNumber == 0) {
                    check = "back";
                }
                if (tableNumber < 0 || tableNumber > size) {
                    view.write("There is no table with this number! Try again.");
                }

                for (int i = 0; i < size; i++) {
                    if (i == tableNumber - 1) {
                        check = names[i];
                    }
                }
                tableName = check;
            } else {
                String check = "";
                for (String name : names) {
                    if (input.equals(name) || input.equals("back")) {
                        check = input;
                    }
                }
                if (check.equals("")) {
                    view.write("Table with name '" + input + "' doesn't exists. Try again.");
                }
                tableName = check;
            }
        }
        return tableName;
    }


    private boolean isParsable(String read) {
        boolean parsable = true;
        try {
            Integer.parseInt(read);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }
}
