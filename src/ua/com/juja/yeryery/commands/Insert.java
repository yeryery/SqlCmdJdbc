package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataSet;
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
        String[] tableNames = manager.getTableNames(); // TODO

        view.write("Please select number of table where you want to insert a new row");

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
        String[] columnNames = manager.getTableColumns(currentTableName);
        int tableSize = columnNames.length;

        String[] values = new String[tableSize];
        DataSet newRow = new DataSet();

        for (int i = 0; i < tableSize; i++) {
            view.write(columnNames[i]);
            values[i] = view.read();
            newRow.put(columnNames[i], values[i]);
        }

        manager.insert(currentTableName, newRow);
        view.write("You have successfully entered new data!");
    }

}
