package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.SQLErrorPrinter;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DataSetImpl;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class Update implements Command {

    private View view;
    private DatabaseManager manager;
    private final String ACTION = "update";

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
        Dialog dialog = new SelectTable();
        String currentTableName = dialog.askUser(names, view, ACTION);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {

            while (true) {
                view.write("Enter columnName and defining value of updated row: " +
                        "columnName|definingValue\n" +
                        "or type 'cancel' to go back.");
                String inputColumnNameValue = view.read();
                String[] columnNameValue = inputColumnNameValue.split("\\|");

                if (columnNameValue[0].equals("cancel")) {
                    cancel = true;
                    break;
                }

                if (columnNameValue.length != 2) {
                    view.write("You should enter two parameters!\n" +
                            "Try again.");
                    continue;
                }

                String columnName = columnNameValue[0];
                Object value = columnNameValue[1];
                //TODO выделить разделитель в отдельный метод

                Set<String> tableColumns = manager.getTableColumns(currentTableName);

                if (!tableColumns.contains(columnName)) {
                    view.write(String.format("Table '%s' doesn't contain column '%s'!", currentTableName, columnName));
                    view.write("Try again.");
                    continue;
                }

                List<DataSet> tableContent = manager.getDataContent(currentTableName);
                List<Object> columnValues = getColumnValues(tableContent, columnName);

                if (isParsable((String) value)) {
                    value = parseInt((String) value);
                }

                if (!columnValues.contains(value)) {
                    view.write(String.format("Column '%s' doesn't contain value '%s'!", columnName, value));
                    view.write("Try again.");
                    continue;
                }

                DataSet newValues = new DataSetImpl();
                newValues = getNewValues(currentTableName);

                if (newValues.isEmpty()) {
                    cancel = true;
                    break;
                }

                try {
                    manager.update(currentTableName, newValues, columnName, value);
                    view.write(String.format("You have successfully updated table '%s' at %s = %s", currentTableName, columnName, value));
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
            view.write("Table updating canceled");
        }
    }

    private DataSet getNewValues(String currentTableName) {
        DataSet newValues = new DataSetImpl();

        while (true) {
            view.write("Enter columnNames and its new values for updated row: \n" +
                    "updatedColumn1|newValue1|updatedColumn2|newValue2|...\n" +
                    "or type 'cancel' to go back.");
            String inputDataSet = view.read();
            String[] split = inputDataSet.split("\\|");

            if (split[0].equals("cancel")) {
                break;
            }

            int size = split.length;

            if (size % 2 != 0) {
                view.write("You should enter an even number of parameters (2, 4 and so on): \n" +
                        "Try again.");
                continue;
            }

            Set<String> tableColumns = manager.getTableColumns(currentTableName);
            List<DataSet> tableContent = manager.getDataContent(currentTableName);

            for (int i = 0; i < size; i++) {
                String updatedColumn = split[i++];
                Object newValue = split[i];

                if (!tableColumns.contains(updatedColumn)) {
                    view.write(String.format("Table '%s' doesn't contain column '%s'!", currentTableName, updatedColumn));
                    view.write("Try again.");
                    newValues = new DataSetImpl();
                    break;
                }

                if (isParsable((String) newValue)) {
                    newValue = parseInt((String) newValue);
                }

                List<Object> columnValues = getColumnValues(tableContent, updatedColumn);

//                if (columnValues.contains(newValue)) {
//                    view.write(String.format("Column '%s' already contains value '%s' in required row!", updatedColumn, newValue));
//                    view.write("Try again.");
//                    newValues = new DataSetImpl();
//                    break;
//                }
                //TODO
                newValues.put(updatedColumn, newValue);
            }
            if (!newValues.isEmpty()) {
                break;
            }
        }
        return newValues;
    }

    private List<Object> getColumnValues(List<DataSet> dataSets, String columnName) {
        List<Object> result = new LinkedList<>();

        for (DataSet dataSet : dataSets) {
            result.add(dataSet.get(columnName));
        }
        return result;
    }

    private boolean isParsable(String read) {
        boolean parsable = true;
        try {
            parseInt(read);
        } catch (NumberFormatException e) {
            parsable = false;
        }
        return parsable;
    }
}

