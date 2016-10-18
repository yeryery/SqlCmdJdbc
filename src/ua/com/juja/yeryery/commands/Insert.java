package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Insert implements Command{

    private View view;
    private DatabaseManager manager;
    private static String COMMAND_SAMPLE = "clear tableName";

    public Insert(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.startsWith("insert");
    }

    @Override
    public void process(String input) {
        view.write("Please select number of table where you want to insert a new row");

        String[] tableNames = manager.getTableNames(); // TODO
        int size = tableNames.length;
        for (int i = 0; i < size; i++) {
            view.write((i + 1) + " " + tableNames[i]);
        }

        int tableNumber = Integer.parseInt(view.read());;

        String currentTableName = "";
        for (int i = 0; i < size; i++) {
            if (tableNumber == i) {
                currentTableName = tableNames[i - 1];
            }
        }

        view.write("Enter the values you require");
    }

    private int count() {
        String[] data = COMMAND_SAMPLE.split("\\s+");
        return data.length;
    }

}
