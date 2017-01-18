package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.controller.commands.Util.CancelException;
import ua.com.juja.yeryery.controller.commands.Util.Dialog;
import ua.com.juja.yeryery.controller.commands.Util.TablePrinter;
import ua.com.juja.yeryery.model.DataEntry;
import ua.com.juja.yeryery.model.DataSet;
import ua.com.juja.yeryery.model.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

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
        Dialog dialog = new Dialog(view, manager);
        String currentTableName = dialog.selectTable(ACTION);
        DataEntry definingEntry = dialog.findRow(currentTableName, ACTION);
        DataSet updatingEntries = dialog.getNewEntries(currentTableName, ACTION);
        checkNewValues(currentTableName, definingEntry, updatingEntries);
        TablePrinter tablePrinter = new TablePrinter(view, manager, currentTableName);

        try {
            manager.update(currentTableName, updatingEntries, definingEntry);
            view.write(String.format("You have successfully updated table '%s'", currentTableName));
            tablePrinter.print();
        } catch (SQLException e) {
            view.write(e.getMessage());
            view.write("Try again.");
            process(input);
        }
    }

    private void checkNewValues(String tableName, DataEntry entry, DataSet inputValues) {
        String columnName = entry.getColumn();
        Object value = entry.getValue();

        if (!isNewValues(inputValues, tableName, columnName, value)) {
            view.write("Your entries are equivalent to the updated");
            throw new CancelException();
        }
    }

    private boolean isNewValues(DataSet newValues, String tableName, String columnName, Object value) {
        for (String column : newValues.getColumnNames()) {
            List<DataSet> updatedRows = getUpdatedRows(tableName, columnName, value);

            for (DataSet row : updatedRows) {
                Object newValue = newValues.get(column);
                Object updatedValue = row.get(column);
                if (!newValue.equals(updatedValue)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<DataSet> getUpdatedRows(String tableName, String columnName, Object value) {
        List<DataSet> dataSets = manager.getDataContent(tableName);
        List<DataSet> updatedRows = new LinkedList<>();

        for (DataSet row : dataSets) {
            if (row.get(columnName).equals(value)) {
                updatedRows.add(row);
            }
        }
        return updatedRows;
    }
}

