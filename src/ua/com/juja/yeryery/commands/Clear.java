package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Clear implements Command {

    private View view;
    private DatabaseManager manager;
    private static String COMMAND_SAMPLE = "clear tableName";

    public Clear(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.startsWith("clear");
    }

    @Override
    public void process(String input) {
        manager.getTableNames();
        String[] data = input.split("\\s+");

        if (data.length != count()) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected %s, and you have entered %s", count(), data.length));
        }

        view.write(String.format("Are you sure you want to clear table '%s'? (y/n)", data[1]));
        String confirm = view.read();

        if (confirm.equals("y")) {
            manager.clear(data[1]);
            view.write(String.format("Table '%s' successfully cleared!", data[1]));
        } else {
            view.write("Cleaning tables canceled");
        }
    }

    private int count() {
        String[] data = COMMAND_SAMPLE.split("\\s+");
        return data.length;
    }
}
