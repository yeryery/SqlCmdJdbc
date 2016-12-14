package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.view.View;

import java.util.*;

public class SelectTable implements Dialog {

    @Override
    public String askUser(Set<String> names, View view, String action) {

        String tableName = "";

        Map<Integer, String> tableNames = new HashMap<>();
        Iterator iterator = names.iterator();
        int i = 1;

        while (iterator.hasNext()) {
            tableNames.put(i, (String) iterator.next());
            i++;
        }

        while (tableName.equals("")) {
            view.write(String.format("Please enter the name or select number of table you want to %s", action));

            tableNames.remove(0);
            for (Map.Entry<Integer, String> entry : tableNames.entrySet()) {
                view.write(entry.getKey() + ". " + entry.getValue());
            }

            tableNames.put(0, "cancel");
            view.write(0 + ". " + "cancel (to go back)");

            String input = view.read();

            if (isParsable(input)) {
                int tableNumber = Integer.parseInt(input);

                if (tableNumber >= 0 && tableNumber <= names.size()) {
                    tableName = tableNames.get(tableNumber);
                } else {
                    view.write("There is no table with this number! Try again.");
                }
            } else {

                for (Map.Entry<Integer, String> entry : tableNames.entrySet()) {
                    if (input.equals(entry.getValue())) {
                        tableName = input;
                    }
                }
                //TODO contain method

                if (tableName.equals("")) {
                    view.write(String.format("Table with name '%s' doesn't exist! Try again.", input));
                }
            }
        }
        return tableName;
    }

    @Override
    public boolean isConfirmed(String name, View view, String act) {
        String confirm = "";

        while (!confirm.equals("y") && !confirm.equals("n")) {
            view.write(String.format("Are you sure you want to %s table '%s'? (y/n)", act, name));
            confirm = view.read();
        }

        boolean result = false;
        if (confirm.equals("y")) {
            result = true;
        }
        return result;
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
