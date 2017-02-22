package ua.com.juja.yeryery.controller.commands;

import ua.com.juja.yeryery.controller.commands.Exceptions.UnknownCommandException;
import ua.com.juja.yeryery.view.View;

public class Unknown implements Command {

    private View view;

    public Unknown(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String input) {
        return true;
    }

    @Override
    public void process(String input) {
        throw new UnknownCommandException("Unknown command: " + input);
    }
}
