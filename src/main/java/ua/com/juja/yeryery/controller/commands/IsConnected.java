package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.controller.commands.Exceptions.ConnectException;
import ua.com.juja.yeryery.model.DatabaseManager;

public class IsConnected implements Command {

    private DatabaseManager manager;

    public IsConnected(DatabaseManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean canProcess(String input) {
        return !manager.isConnected();
    }

    @Override
    public void process(String input) {
        throw new ConnectException(String.format("You can`t use '%s' unless you are not connected", input));
    }
}
