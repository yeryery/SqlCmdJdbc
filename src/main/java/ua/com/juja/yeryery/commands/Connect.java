package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Connect implements Command {

    private View view;
    private DatabaseManager manager;
    private static final String ACTION = "connect|";

    public Connect(View view, DatabaseManager manager) {
        this.view = view;
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return input.startsWith(ACTION);
    }

    @Override
    public void process(String input) throws ConnectException {
        String delimiter = "\\|";
        String commandSample = "connect|yeryery|postgres|postgrespass";

        String[] splitInput;
        try {
            splitInput = Parser.splitData(input, commandSample, delimiter);
        } catch (IllegalArgumentException e) {
            throw new ConnectException(e.getCutMessage());
        }
        String database = splitInput[1];
        String username = splitInput[2];
        String password = splitInput[3];

        manager.connect(database, username, password);
        view.write("Success!");
    }
}
