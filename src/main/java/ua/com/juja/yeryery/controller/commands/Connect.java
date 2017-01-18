package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.controller.commands.Util.ConnectException;
import ua.com.juja.yeryery.controller.commands.Util.Dialog;
import ua.com.juja.yeryery.controller.commands.Util.IllegalArgumentException;
import ua.com.juja.yeryery.model.DatabaseManager;
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
        String commandSample = "connect|yeryery|postgres|postgrespass";
        Dialog dialog = new Dialog(view, manager);
        String[] splitInput;
        String database;
        String username;
        String password;

        try {
            splitInput = dialog.splitConnectInput(input, commandSample);
        } catch (IllegalArgumentException e) {
            throw new ConnectException(e.getShortMessage());
        }

        database = splitInput[1];
        username = splitInput[2];
        password = splitInput[3];

        manager.connect(database, username, password);
        view.write("Success!");
    }
}
