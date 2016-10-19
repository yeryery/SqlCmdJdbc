package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class NameTable implements Dialog {
    @Override
    public String askUser(DatabaseManager manager, View view) {
        String[] names = manager.getTableNames();

        String uniqueName = "";
        while (uniqueName.equals("")) {
            view.write("Please enter the name of table you want to create");
            String tableName = view.read();

            int size = names.length;
            for (String name : names) {
                if (tableName.equals(name)) {
                    view.write("Table " + tableName + "already exists. Choose another name");
                    break;
                }
                uniqueName = tableName;
            }
        }
        return uniqueName;
    }
}
