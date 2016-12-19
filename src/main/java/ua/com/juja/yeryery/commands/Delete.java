package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.TableConstructor;
import ua.com.juja.yeryery.commands.dialogs.Dialog;
import ua.com.juja.yeryery.commands.dialogs.DialogImpl;
import ua.com.juja.yeryery.manager.DataSet;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

import java.util.List;
import java.util.Set;

public class Delete implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "delete";
    private static final String COMMAND_SAMPLE = "columnName|value";

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
        Dialog dialog = new DialogImpl(view, manager);
        String message = String.format("Please enter the name or select number of table where you want to %s rows", ACTION);
        String currentTableName = dialog.SelectTable(message);

        boolean cancel = currentTableName.equals("cancel");

        if (!cancel) {
            while (true) {

                String[] splitInput;
                try {
                    splitInput = dialog.findRow(currentTableName, ACTION, COMMAND_SAMPLE);
                } catch (CancelException e) {
                    cancel = true;
                    break;
                } catch (IllegalArgumentException e) {
                    view.write(e.getMessage());
                    view.write("Try again.");
                    continue;
                }

                Parser parser = new Parser();
                String columnName = splitInput[0];
                Object value = parser.defineType(splitInput[1]);
                //TODO

                Set<String> tableColumns = manager.getTableColumns(currentTableName);
                List<DataSet> originRows = manager.getDataContent(currentTableName);

                manager.delete(currentTableName, columnName, value);
                view.write(String.format("You have successfully deleted data from '%s' at %s = %s", currentTableName, columnName, value));

                TableConstructor tableConstructor = new TableConstructor(tableColumns, originRows);
                view.write(tableConstructor.getTableString());
                break;
            }
        }

        if (cancel) {
            view.write("Table deleting canceled");
        }
    }
}
