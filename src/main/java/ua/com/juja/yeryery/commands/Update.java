package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataEntry;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
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
        List<DataSet> updatedRows = getUpdatedRows(tableName, columnName, value);
        for (String column : newValues.getColumnNames()) {
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
        List<DataSet> result = new LinkedList<>();

        for (DataSet row : dataSets) {
            if (row.get(columnName).equals(value)) {
                result.add(row);
            }
        }
        return result;
    }
}

