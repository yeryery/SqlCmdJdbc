package ua.com.juja.yeryery.commands;

import ua.com.juja.yeryery.view.View;

public class Exit implements Command {

    private View view;
    private static final String ACTION = "exit";

    public Exit(View view) {
        this.view = view;
    }

    @Override
    public boolean canProcess(String input) {
        return input.equals(ACTION);
    }

    @Override
    public void process(String input) {
        view.write("See you!");

        throw new ExitException();
    }
}
