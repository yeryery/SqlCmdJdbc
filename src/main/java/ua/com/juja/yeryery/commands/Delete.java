package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.SQLErrorPrinter;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Delete implements Command {

    private View view;
    private DatabaseManager manager;
    private final String ACTION = "delete";

    public Delete(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        Set<String> tableNames = manager.getTableNames();
        Dialog dialog = new SelectTable();
        String currentTableName = dialog.askUser(tableNames, view, ACTION);

        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {

            while (true) {
                final String COMMAND_SAMPLE = "columnName|value";
                view.write("Enter columnName and value of the row you want to delete: " + COMMAND_SAMPLE + "\n" +
                        "or type 'cancel' to go back.");
                String inputDataSet = view.read();
                final String delimiter = "\\|";
                String[] split = inputDataSet.split(delimiter);

                if (split[0].equals("cancel")) {
                    cancel = true;
                    break;
                }

                Parser parser = new Parser();
                int count = parser.count(COMMAND_SAMPLE, delimiter);
                int size = split.length;

                if (size != count) {
                    view.write("You should enter two parameters!\n" +
                            "Try again.");
                    continue;
                }

                String columnName = split[0];
                Object value = split[1];

                Set<String> tableColumns = manager.getTableColumns(currentTableName);

                if (!tableColumns.contains(columnName)) {
                    view.write(String.format("Table '%s' doesn't contain column '%s'!", currentTableName, columnName));
                    view.write("Try again.");
                    continue;
                }

                List<DataSet> tableContent = manager.getDataContent(currentTableName);
                List<Object> columnValues = getColumnValues(tableContent, columnName);

                value = parser.defineType((String) value);

                if (!columnValues.contains(value)) {
                    view.write(String.format("Column '%s' doesn't contain value '%s'!", columnName, value));
                    view.write("Try again.");
                    continue;
                }

                try {
                    manager.delete(currentTableName, columnName, value);
                    view.write(String.format("You have successfully deleted data from '%s' at %s = %s", currentTableName, columnName, value));
                    Display display = new Display(view, manager);
                    display.printColumnNames(tableColumns);
                    display.printValues(tableContent);
                    break;
                } catch (SQLException e) {
                    SQLErrorPrinter error = new SQLErrorPrinter(e);
                    error.printSQLError();
                }
            }
        }

        if (cancel) {
            view.write("Table deleting canceled");
        }
    }

    private List<Object> getColumnValues(List<DataSet> dataSets, String columnName) {
        List<Object> result = new LinkedList<>();

        for (DataSet dataSet : dataSets) {
            result.add(dataSet.get(columnName));
        }
        return result;
    }
}
