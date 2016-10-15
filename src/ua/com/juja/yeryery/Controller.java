package ua.com.juja.yeryery;

import ua.com.juja.yeryery.commands.Command;
import ua.com.juja.yeryery.commands.Connect;
import ua.com.juja.yeryery.manager.DatabaseManager;
import ua.com.juja.yeryery.view.View;

public class Controller {

    private View view;
    private Command command;

    public Controller(View view, DatabaseManager manager) {
        this.view = view;
        this.command = new Connect(view, manager);
    }

    public void run() {
        view.write("Hello, user!");
        view.write("Please, enter: " +
                        "connect|database|username|password");

        String input = view.read();
        command.process(input);
        view.write("Success!");
    }
}
