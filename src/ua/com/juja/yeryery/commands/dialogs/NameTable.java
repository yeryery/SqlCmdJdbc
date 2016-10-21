package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class NameTable implements Dialog {
    @Override
    public String askUser(DatabaseManager manager, View view) {
        String[] names = manager.getTableNames();
        view.write("Please enter the name of table you want to create or type 'back' to go back");

        String uniqueName = "";
        while (uniqueName.equals("")) {
            String tableName = view.read();

            for (String name : names) {
                if (tableName.equals(name)) {
                    view.write("Table '" + tableName + "' already exists. Try again.");
                    tableName = "";
                }
                uniqueName = tableName;
            }
        }
        return uniqueName;
    }
}
