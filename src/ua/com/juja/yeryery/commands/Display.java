package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Display implements Command {
    private View view;
    private DatabaseManager manager;
    private static String COMMAND_SAMPLE = "clear tableName";

    public Display(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("display");
    }

    @Override
    public void process(String input) {
        view.write("Please select number of table which you want to display");

        String[] tableNames = manager.getTableNames(); // TODO
        int size = tableNames.length;
        for (int i = 0; i < size; i++) {
            view.write((i + 1) + " " + tableNames[i]);
        }

        int tableNumber = Integer.parseInt(view.read());
        ;

        String currentTableName = "";
        for (int i = 0; i < size; i++) {
            if (tableNumber == i) {
                currentTableName = tableNames[i - 1];
            }
        }

        String[] tableColumns = manager.getTableColumns(currentTableName);
        printColumnNames(tableColumns);
        DataSet[] rows = manager.getDataContent(currentTableName);
        printValues(rows);
    }

    private void printValues(DataSet[] dataSets) {
        String result = "";

        for (DataSet dataSet : dataSets) {
            result += getStringRow(dataSet);
            result += "\n";
        }
        view.write(result);
        view.write("-------------------------");
    }

    private String getStringRow(DataSet dataSet) {
        Object[] values = dataSet.getValues();
        String result = "| ";

        for (Object value : values) {
            result += value + " | ";
        }
        return result;
    }

    private void printColumnNames(String[] tableColumns) {
        String result = "| ";
        for (String column : tableColumns) {
            result += column + " | ";
        }
        view.write(result);
        view.write("-----------------------------");
    }
}
