package ua.com.juja.yeryery.commands.dialogs;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class ChooseTable implements Dialog {
    @Override
    public String askUser(DatabaseManager manager, View view) {
        String[] tableNames = manager.getTableNames();

        view.write("Please select number of table where you want to insert a new row");

        int size = tableNames.length;
        for (int i = 0; i < size; i++) {
            view.write((i + 1) + " " + tableNames[i]);
        }

        int tableNumber = Integer.parseInt(view.read());
        ;

        String tableName = "";
        for (int i = 0; i < size; i++) {
            if (i == tableNumber - 1) {
                tableName = tableNames[i];
            }
        }
        return tableName;
    }
}
