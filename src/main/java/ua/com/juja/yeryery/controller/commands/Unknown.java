package ua.com.juja.yeryery.controller.commands;

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
        view.write("Unknown command: " + input);
        view.write("Try again");
    }
}
