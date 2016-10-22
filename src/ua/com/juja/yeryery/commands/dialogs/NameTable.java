package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.Arrays;

public class NameTable implements Dialog {
    @Override
    public String askUser(DatabaseManager manager, View view) {
        String[] names = manager.getTableNames();
        view.write("Please enter the name of table you want to create or 'cancel' to go back");
        String uniqueName = "";

        while (uniqueName.equals("")) {
            String tableName = view.read();

            for (String name : names) {
                if (tableName.equals(name)) {
                    view.write("Table with name '" + tableName + "' already exists.");
                    view.write(Arrays.toString(names));
                    view.write("Try again.");
                    tableName = "";
                }
                uniqueName = tableName;
            }
        }
        return uniqueName;
    }
}
