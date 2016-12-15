package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.view.View;

import java.util.*;

public class SelectTable implements Dialog {

    @Override
    public String askUser(Set<String> names, View view, String action) {

        Map<Integer, String> tableNames = new HashMap<>();

        Iterator iterator = names.iterator();
        int i = 1;

        while (iterator.hasNext()) {
            tableNames.put(i, (String) iterator.next());
            i++;
        }

        String tableName;

        while (true) {
            view.write(String.format("Please enter the name or select number of table you want to %s", action));

            tableNames.remove(0);
            for (Map.Entry<Integer, String> entry : tableNames.entrySet()) {
                view.write(entry.getKey() + ". " + entry.getValue());
            }

            view.write(0 + ". " + "cancel (to go back)");
            tableNames.put(0, "cancel");

            String input = view.read();

            if (isParsable(input)) {
                int tableNumber = Integer.parseInt(input);

                if (tableNumber >= 0 && tableNumber <= names.size()) {
                    tableName = tableNames.get(tableNumber);
                    break;
                } else {
                    view.write("There is no table with this number! Try again.");
                }
            } else {

                if (tableNames.containsValue(input)) {
                    tableName = input;
                    break;
                } else {
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

    private boolean isParsable(Object read) {
        boolean parsable = true;
        try {
            Integer.parseInt((String) read);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }
}
