package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.TableConstructor;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.List;
import java.util.Set;

public class Display implements Command {
    private View view;
    private DatabaseManager manager;
    private final String ACTION = "display";

    public Display(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        Dialog dialog = new DialogImpl(view, manager);
        String message = String.format("Please enter the name or select number of table you want to %s", ACTION);
        String currentTableName = dialog.SelectTable(message);
        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {

            Set<String> tableColumns = manager.getTableColumns(currentTableName);
            List<DataSet> tableContent = manager.getDataContent(currentTableName);
            TableConstructor tableConstructor = new TableConstructor(tableColumns, tableContent);
            view.write(tableConstructor.getTableString());
        } else {
            view.write("Table displaying canceled");
        }
    }

    public void printColumnNames(Set<String> tableColumns) {
        String result = "| ";
        for (String column : tableColumns) {
            result += column + " | ";
        }
        view.write(result);
        view.write("-------------------------");
    }

    public void printValues(List<DataSet> dataSets) {
        String result = "";
        for (DataSet dataSet : dataSets) {
            result += getStringRow(dataSet);
            result += "\n";
        }
        result += "-------------------------";
        view.write(result.substring(0, result.length() - 1));
    }

    private String getStringRow(DataSet dataSet) {
        List<Object> values = dataSet.getValues();

        String result = "| ";
        for (Object value : values) {
            result += value + " | ";
        }
        return result;
    }
}
