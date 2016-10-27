package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.view.View;

import java.util.Arrays;
import java.util.Set;

public class NameTable implements Dialog {
    @Override
    public String askUser(Set<String> names, View view) {
        view.write("Please enter the name of table you want to create or 'cancel' to go back");
        String uniqueName = "";

        while (uniqueName.equals("")) {
            String tableName = view.read();
            String[] tableNames = names.toArray(new String[names.size()]);

            for (String name : names) {
                if (tableName.equals(name)) {
                    view.write("Table with name '" + tableName + "' already exists.");
                    view.write(Arrays.toString(tableNames));
                    view.write("Try again.");
                    tableName = "";
                }
                uniqueName = tableName;
            }
        }
        return uniqueName;
    }
}
