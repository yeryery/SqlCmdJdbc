package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.view.View;

import java.util.Set;

public class NameTable implements Dialog {
    @Override
    public String askUser(Set<String> names, View view) {
        view.write("Please enter the name of table you want to create or 'cancel' to go back");
        String uniqueName = "";

        while (uniqueName.equals("")) {
            String tableName = view.read();

            for (String name : names) {
                if (tableName.equals(name)) {
                    view.write("Table with name '" + tableName + "' already exists.");
                    view.write(names.toString());
                    view.write("Try again.");
                    tableName = "";
                }
                uniqueName = tableName;
            }
        }
        return uniqueName;
    }
}
