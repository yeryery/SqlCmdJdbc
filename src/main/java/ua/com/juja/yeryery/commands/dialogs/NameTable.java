package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.view.View;

import java.util.Set;

public class NameTable implements Dialog {

    @Override
    public String askUser(Set<String> names, View view, String action) {
        String tableName = "";

        while (tableName.equals("")) {
            view.write("Please enter the name of table you want to " + action + " or 'cancel' to go back");
            tableName = view.read();

            char firstLetter = tableName.charAt(0);
            if (!(firstLetter >= 'a' && firstLetter <= 'z') && !(firstLetter >= 'A' && firstLetter <= 'Z')) {
                view.write("Table name must begin with a letter! Try again.");
                tableName = "";
                continue;
            }

            for (String name : names) {
                if (tableName.equals(name)) {
                    view.write("Table with name '" + tableName + "' already exists!");
                    view.write(names.toString());
                    view.write("Try again.");
                    tableName = "";
                }
            }
        }
        return tableName;
    }
}
