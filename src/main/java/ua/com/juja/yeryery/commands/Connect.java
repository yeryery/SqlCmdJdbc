package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.Parser;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Connect implements Command {

    private View view;
    private DatabaseManager manager;

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
        final String delimiter = "\\|";
        String[] data = input.split(delimiter);

        final String COMMAND_SAMPLE = "connect|yeryery|postgres|postgrespass";
        Parser parser = new Parser();
        int count = parser.count(COMMAND_SAMPLE, delimiter);

        if (data.length != count) {
            throw new IllegalArgumentException(String.format("Wrong number of parameters. " +
                    "Expected %s, and you have entered %s", count, data.length));
        }

        String database = data[1];
        String username = data[2];
        String password = data[3];

        manager.connect(database, username, password);
        view.write("Success!");
    }
}
