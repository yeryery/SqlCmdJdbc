package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.view.View;

import java.util.Set;

public class NameTable implements Dialog {

    @Override
    public String askUser(Set<String> names, View view, String act) {
        String tableName;

        while (true) {
            view.write(String.format("Please enter the name of table you want to %s or 'cancel' to go back", act));
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
    public boolean isConfirmed(String warning, View view) {
        return true;
    }
}
