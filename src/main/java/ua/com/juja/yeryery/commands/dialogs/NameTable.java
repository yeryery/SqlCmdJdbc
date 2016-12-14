package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.view.View;

import java.util.Set;

public class NameTable implements Dialog {

    @Override
    public String askUser(Set<String> names, View view, String act) {
        String tableName = "";

        while (tableName.equals("")) {
            view.write(String.format("Please enter the name of table you want to %s or 'cancel' to go back", act));
            tableName = view.read();

            char firstLetter = tableName.charAt(0);
            if (!(firstLetter >= 'a' && firstLetter <= 'z') && !(firstLetter >= 'A' && firstLetter <= 'Z')) {
                view.write("Table name must begin with a letter! Try again.");
                tableName = "";
                continue;
            }

            for (String name : names) {
                if (tableName.equals(name)) {
                    view.write(String.format("Table with name '%s' already exists!", tableName));
                    view.write(names.toString());
                    view.write("Try again.");
                    tableName = "";
                }
                //TODO method contain
            }
        }
        return tableName;
    }

    @Override
    public boolean isConfirmed(String name, View view, String act) {
        return true;
    }
}
