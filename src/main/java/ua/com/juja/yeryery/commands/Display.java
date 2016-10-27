package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.commands.dialogs.SelectTable;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.List;
import java.util.Set;

public class Display implements Command {
    private View view;
    private DatabaseManager manager;
    private Dialog dialog;

    public Display(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
        dialog = new SelectTable();
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals("display");
    }

    @Override
    public void process(String input) {
        Set<String> names = manager.getTableNames();
        String currentTableName = dialog.askUser(names, view);

        if (!currentTableName.equals("cancel")) {
            Set<String> tableColumns = manager.getTableColumns(currentTableName);
            printColumnNames(tableColumns);
            List<DataSet> rows = manager.getDataContent(currentTableName);
            printValues(rows);
        }
    }

    private void printValues(List<DataSet> dataSets) {
        String result = "";
        for (DataSet dataSet : dataSets) {
            result += getStringRow(dataSet);
            result += "\n";
        }
        result += "-------------------------";
        view.write(result.substring(0, result.length() - 1));
    }

    private String getStringRow(DataSet dataSet) {
        Object[] values = dataSet.getValues();

        String result = "| ";
        for (Object value : values) {
            result += value + " | ";
        }
        return result;
    }

    private void printColumnNames(Set<String> tableColumns) {
        String result = "| ";
        for (String column : tableColumns) {
            result += column + " | ";
        }
        view.write(result);
        view.write("-------------------------");
    }
}
