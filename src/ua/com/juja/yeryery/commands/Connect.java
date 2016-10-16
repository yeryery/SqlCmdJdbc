package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Connect implements Command {
    private View view;
    private DatabaseManager manager;
    private static String COMMAND_SAMPLE = "connect|yeryery|postgres|postgrespass";

    public Connect(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.startsWith("connect|");
    }

    @Override
    public void process(String input) {
        String[] data = input.split("\\|");

        if (data.length != count()) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected %s, and you have entered %s", 4, data.length));
        }

        String database = data[1];
        String username = data[2];
        String password = data[3];

        manager.connect(database, username, password);
        view.write("Success!");
    }

    private int count() {
        String[] data = COMMAND_SAMPLE.split("\\|");
        return data.length;
    }
}
