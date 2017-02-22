package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.controller.commands.Exceptions.UnknownCommandException;

public class Unknown implements Command {

    @Override
    public boolean canProcess(String input) {
        return true;
    }

    @Override
    public void process(String input) {
        throw new UnknownCommandException("Unknown command: " + input);
    }
}
