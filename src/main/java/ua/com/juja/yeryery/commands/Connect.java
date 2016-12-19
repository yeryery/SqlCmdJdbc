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
        final String COMMAND_SAMPLE = "connect|yeryery|postgres|postgrespass";

        Parser parser = new Parser();
        String[] splitInput = parser.splitData(input, COMMAND_SAMPLE, delimiter);

        String database = splitInput[1];
        String username = splitInput[2];
        String password = splitInput[3];

        manager.connect(database, username, password);
        view.write("Success!");
    }
}
