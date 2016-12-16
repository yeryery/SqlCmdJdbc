package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.view.View;

import java.util.*;

public class SelectTable implements Dialog {

    @Override
    public String askUser(Set<String> names, View view, String action) {

        Map<Integer, String> tableNames = new HashMap<>();

//        for (int i = 1; names.iterator().hasNext(); i++) {
//            tableNames.put(i, names.iterator().next());
//        }

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
            Parser parser = new Parser();

            if (parser.isParsable(input)) {
                int tableNumber = parser.getParsedInt();

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
    public boolean isConfirmed(String warning, View view) {
        String confirm = "";

        while (!confirm.equals("y") && !confirm.equals("n")) {
            view.write(warning + " (y/n)");
            confirm =(String) view.read();
        }

        boolean result = false;
        if (confirm.equals("y")) {
            result = true;
        }
        return result;
    }
}
