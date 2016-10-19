package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.ChooseTable;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Insert implements Command{

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;

    public Insert(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        dialog = new ChooseTable();
    }

    @Override
    public boolean canProcess(String input) {
        return input.startsWith("insert");
    }

    @Override
    public void process(String input) {
        String currentTableName = dialog.askUser(manager, view);

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
