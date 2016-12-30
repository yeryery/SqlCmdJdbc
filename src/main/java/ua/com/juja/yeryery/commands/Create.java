package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;

public class Create implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "create";

    public Create(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        Dialog dialog = new Dialog(view, manager);
        String setValuesMessage = "Enter the name of columns and its type for new table: \n" +
                "columnName1|columnType1|columnName2|columnType2|...\nor type 'cancel' to go back";

        try {
            String currentTableName = dialog.nameTable();
            DataSet dataTypes = dialog.getInputByTwo(setValuesMessage);
            manager.create(currentTableName, dataTypes);
            view.write(String.format("Your table '%s' have successfully created!", currentTableName));
        } catch (SQLException e) {
            view.write(e.getMessage());
            view.write("Try again.");
            process(input);
        }
    }
}

