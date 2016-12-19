package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.view.View;

import java.util.*;

public class DialogImpl implements Dialog {

    private View view;

    public DialogImpl(View view) {
        this.view = view;
    }

    @Override
    public String SelectTable(Set<String> names, String message) {

        Map<Integer, String> tableNames = new HashMap<>();

        Iterator iterator = names.iterator();
        int i = 1;

        while (iterator.hasNext()) {
            tableNames.put(i, (String) iterator.next());
            i++;
        }

        String tableName;

        while (true) {
            view.write(message);

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
    public String NameTable(Set<String> names, String message) {
        String tableName;

        while (true) {
            view.write(message);
            tableName = view.read();

            char firstLetter = tableName.charAt(0);
            if (!(firstLetter >= 'a' && firstLetter <= 'z') && !(firstLetter >= 'A' && firstLetter <= 'Z')) {
                view.write("Table name must begin with a letter! Try again.");
                continue;
            }

            if (names.contains(tableName)) {
                view.write(String.format("Table with name '%s' already exists!", tableName));
                view.write(names.toString());
                view.write("Try again.");
            } else {
                break;
            }
        }
        return tableName;
    }

    @Override
    public boolean isConfirmed(String warning) {
        String confirm = "";

        while (!confirm.equals("y") && !confirm.equals("n")) {
            view.write(warning + " (y/n)");
            confirm = (String) view.read();
        }

        boolean result = false;
        if (confirm.equals("y")) {
            result = true;
        }
        return result;
    }
}
