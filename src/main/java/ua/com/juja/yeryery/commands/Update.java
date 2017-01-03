package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataEntry;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.Set;

public class Update implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "update";

    public Update(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        Set<String> names = manager.getTableNames();
        Dialog dialog = new Dialog(view, manager);
        String findMessage = "Enter the columnName and defining value of updated row: %s\nor type 'cancel' to go back";
        String setValuesMessage = "Enter the columnNames and its new values for updated row: \n" +
                "updatedColumn1|newValue1|updatedColumn2|newValue2|...\nor type 'cancel' to go back";
        String commandSample = "columnName|value";

        String currentTableName = "";
        DataEntry definingEntry = null;
        DataSet newValues = null;

        currentTableName = dialog.selectTable(ACTION);
        definingEntry = dialog.defineRow(currentTableName, findMessage, commandSample);
        newValues = dialog.getNewValues(currentTableName, setValuesMessage, definingEntry);

        try {
            TablePrinter tablePrinter = new TablePrinter(view, manager, currentTableName);
            manager.update(currentTableName, newValues, definingEntry);
            view.write(String.format("You have successfully updated table '%s'", currentTableName));
            tablePrinter.print();
        } catch (SQLException e) {
            view.write(e.getMessage());
            view.write("Try again.");
            process(input);
        }

    }
}

