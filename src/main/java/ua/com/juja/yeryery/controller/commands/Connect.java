package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.controller.commands.Utility.ConnectException;
import ua.com.juja.yeryery.controller.commands.Utility.Dialog;
import ua.com.juja.yeryery.controller.commands.Utility.IllegalArgumentException;
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
        Dialog dialog = new Dialog(view, manager);
        String commandSample = "connect|database|username|password";
        String[] splitInput;

        try {
            splitInput = dialog.splitConnectInput(input, commandSample);
        } catch (IllegalArgumentException e) {
            throw new ConnectException(e.getMessage());
        }

        String database = splitInput[1];
        String username = splitInput[2];
        String password = splitInput[3];

        manager.connect(database, username, password);
        view.write("Success!");
    }
}
