package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.NameTable;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import static java.lang.Integer.parseInt;

public class Create implements Command{

    private View view;
    private DatabaseManager manager;
    private Dialog dialog;

    public Create(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        dialog = new NameTable();
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("create");
    }

    @Override
    public void process(String input) {
        String currentTableName = dialog.askUser(manager, view);

        if (!currentTableName.equals("back")) {
            view.write("Please enter the number of columns of your table");
            int tableSize = parseInt(view.read());

            DataSet dataTypes = new DataSet();

            for (int i = 0; i < tableSize; i++) {
                view.write("name of column " + (i + 1));
                String columnName = view.read();

                view.write("datatype of column " + (i + 1));
                String dataType = view.read();

                dataTypes.put(columnName, dataType);
            }

            manager.create(currentTableName, dataTypes);
            view.write("Your table '" + currentTableName + "' have successfully created!");
        }
    }
}

