package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.view.View;

import java.util.Set;

public class SelectTable implements Dialog {
    @Override
    public String askUser(Set<String> names, View view) {

        String tableName = "";
        names.iterator();

        while (tableName.equals("")) {
            view.write("Please enter the name or select number of table you need");
            String[] tableNames = names.toArray(new String[names.size()]);

            for (int i = 0; i < names.size(); i++) {
                view.write((i + 1) + ". " + tableNames[i]);
            }
            view.write("0. cancel (to go back)");

            String input = view.read();

            if (isParsable(input)) {
                int tableNumber = Integer.parseInt(input);

                if (tableNumber == 0) {
                    tableName = "cancel";
                } else if (tableNumber > 0 && tableNumber <= names.size()) {
                    tableName = tableNames[tableNumber - 1];
                } else {
                    view.write("There is no table with this number! Try again.");
                }
            } else {
                String check = "";

                for (String name : names) {
                    if (input.equals(name) || input.equals("cancel")) {
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
